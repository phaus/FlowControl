package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Notice;
import models.Project;
import play.cache.Cache;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;

public class Notices extends Application {

    public static void index() {
        Integer resolved = getResolved();
        List<Project> projects = currentAccount.getProjects();
        List<Notice> entities = currentAccount.getNotices(resolved);
        render(entities, projects, resolved);
    }

    public static void filter(Long project_id) {
        Integer resolved = getResolved();
        Project project = Project.findById(project_id);
        List<Project> projects = currentAccount.getProjects();
        List<Notice> entities = currentAccount.getNoticesForProject(project, resolved);
        renderTemplate("Notices/index.html", entities, projects, project, project_id, resolved);
    }

    public static void create(Notice entity) {
        render(entity);
    }

    public static void show(java.lang.Long id) {
        Long project_id = params.get("project_id", Long.class);
        Notice entity = Notice.findById(id);
        render(entity, project_id);
    }

    public static void edit(java.lang.Long id) {
        Notice entity = Notice.findById(id);
        render(entity);
    }

    public static void delete(java.lang.Long id) {
        Notice entity = Notice.findById(id);
        entity.delete();
        index();
    }

    public static void save(@Valid Notice entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@create", entity);
        }
        entity.save();
        flash.success(Messages.get("scaffold.created", "Notice"));
        index();
    }

    public static void update(@Valid Notice entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@edit", entity);
        }

        entity = entity.merge();

        entity.save();
        flash.success(Messages.get("scaffold.updated", "Notice"));
        index();
    }

    private static Integer getResolved() {
        Integer resolved = Cache.get(session.getId() + "-resolved", Integer.class);
        if (resolved == null) {
            resolved = Notice.UNRESOLVED;
        }
        return resolved;
    }

    public static void setResolved(Integer resolved) {
        Cache.set(session.getId() + "-resolved", resolved, "24h");
        Long project_id = params.get("project_id", Long.class);
        if (project_id != null) {
            filter(project_id);
        } else {
            index();
        }

    }

    // TODO there might be a better way to do this.
    public static Map getStates() {
        Map states = new HashMap<Integer, String>();
        states.put(Notice.ALL, "all");
        states.put(Notice.RESOLVED, "resolved");
        states.put(Notice.UNRESOLVED, "unresolved");
        return states;
    }
}
