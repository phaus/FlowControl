package controllers;

import java.util.List;
import models.ApiKey;
import models.Notice;
import models.Project;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.i18n.Messages;

public class Projects extends Application {

    public static void index() {
        List<Project> entities;
        entities = currentAccount.getProjects();
        render(entities);
    }

    public static void create(Project entity) {
        render(entity);
    }

    public static void show(java.lang.Long id) {
        Project project = Project.findById(id);
        List<Notice> notices = currentAccount.getNoticesForProject(project, Notice.UNRESOLVED);
        render(project, notices);
    }

    public static void edit(java.lang.Long id) {
        Project entity = Project.findById(id);
        render(entity);
    }

    public static void delete(java.lang.Long id) {
        Project entity = Project.findById(id);
        currentAccount.removeProject(entity);
        entity.delete();
        index();
    }

    public static void save(@Valid Project entity) {
        if (Validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@create", entity);
        }
        entity.save();
        currentAccount.addProject(entity);
        flash.success(Messages.get("scaffold.created", "Project"));
        index();
    }

    public static void update(@Valid Project entity) {
        if (Validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@edit", entity);
        }

        entity = entity.merge();
        entity.save();
        flash.success(Messages.get("scaffold.updated", "Project"));
        index();
    }

    public static void addApiKey(java.lang.Long id) {
        Project entity = Project.findById(id);
        if (entity != null && entity.apiKey == null) {
            ApiKey key = new ApiKey();
            key.save();
            entity.apiKey = key;
            entity.save();
        }
        index();
    }

    public static void removeApiKey(java.lang.Long id) {
        Project entity = Project.findById(id);
        if (entity != null && entity.apiKey != null) {
            ApiKey key = entity.apiKey;
            entity.apiKey = null;
            entity.save();
            key.delete();
        }
        index();
    }
}
