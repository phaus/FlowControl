package controllers;

import java.util.List;
import models.Notice;
import models.Project;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;

public class Notices extends Application {

    public static void index() {
        String resolved = params.get("resolved", String.class);
        List<Project> projects = currentAccount.getProjects();
        List<Notice> entities = currentAccount.getNotices("true".equals(resolved));
        render(entities, projects, resolved);
    }

    public static void filter(Long project_id) {
        Long id = params.get("project_id", Long.class);
        String resolved = params.get("resolved", String.class);
        Project project = Project.findById(id);
        List<Project> projects = currentAccount.getProjects();
        List<Notice> entities = currentAccount.getNoticesForProject(project, "true".equals(resolved));
        renderTemplate("Notices/index.html", entities, projects, project, resolved);
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
}
