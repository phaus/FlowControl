package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Notice;
import models.Project;
import play.cache.Cache;
import play.i18n.Messages;
import play.data.validation.Valid;

public class Notices extends Application {

    public static void index() {
        Long project_id = getActiveProjectId();
        Integer resolved = getResolved();
        List<Project> projects = currentAccount.getProjects();
        List<Notice> entities;
        if (project_id == null) {
            entities = currentAccount.getNotices(resolved);
            render(entities, projects, resolved);
        } else {
            Project project = Project.findById(project_id);
            entities = currentAccount.getNoticesForProject(project, resolved);
            render(entities, projects, project, project_id, resolved);
        }
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
        Notices.index();
    }

    public static void save(@Valid Notice entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@create", entity);
        }
        entity.save();
        flash.success(Messages.get("scaffold.created", "Notice"));
        Notices.index();
    }

    public static void update(@Valid Notice entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@edit", entity);
        }

        entity = entity.merge();

        entity.save();
        flash.success(Messages.get("scaffold.updated", "Notice"));
        Notices.index();
    }
    
    private static Long getActiveProjectId() {
        Long activeProjectId = Cache.get(session.getId() + "-activeProjectId", Long.class);
        return activeProjectId;
    }

    public static void setActiveProjectId(Long activeProjectId) {
        if (activeProjectId == 0) {
            Cache.delete(session.getId() + "-activeProjectId");
        } else {
            Cache.set(session.getId() + "-activeProjectId", activeProjectId, "24h");
        }
        Notices.index();
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
        Notices.index();
    }

    // TODO there might be a better way to do this.
    public static Map getStates() {
        Map states = new HashMap<Integer, String>();
        states.put(Notice.UNRESOLVED, "unresolved");
        states.put(Notice.RESOLVED, "resolved");
        states.put(Notice.ALL, "all");
        return states;
    }
}
