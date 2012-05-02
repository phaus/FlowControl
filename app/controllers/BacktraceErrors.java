package controllers;

import java.util.List;
import models.Backtrace;
import models.Error;
import models.Project;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;

public class BacktraceErrors extends Application {

    public static void index() {
        Long traceId = params.get("trace_id", Long.class);
        Backtrace trace = null;
        List<Error> entities = Error.findErrorsByAccount(currentAccount);
        List<Project> projects = currentAccount.getProjects();
        if (traceId != null) {
            trace = Backtrace.findById(traceId);
        }
        render(entities, projects, trace, traceId);
    }

    public static void projectIndex(java.lang.Long projectId) {
        Long traceId = params.get("trace_id", Long.class);
        Backtrace trace = null;
        List<Error> entities = null;
        List<Project> projects = currentAccount.getProjects();
        Project project = Project.findById(projectId);
        entities = project.getErrorsForAccount(currentAccount);
        if (traceId != null) {
            trace = Backtrace.findById(traceId);
        }
        render(entities, projects, trace, project, traceId);
    }

    public static void create(Error entity) {
        render(entity);
    }

    public static void show(java.lang.Long id) {
        Error entity = Error.findById(id);
        render(entity);
    }

    public static void edit(java.lang.Long id) {
        Error entity = Error.findById(id);
        render(entity);
    }

    public static void delete(java.lang.Long id) {
        Error entity = Error.findById(id);
        entity.delete();
        index();
    }

    public static void save(@Valid Error entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@create", entity);
        }
        entity.save();
        flash.success(Messages.get("scaffold.created", "Error"));
        index();
    }

    public static void update(@Valid Error entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@edit", entity);
        }

        entity = entity.merge();

        entity.save();
        flash.success(Messages.get("scaffold.updated", "Error"));
        index();
    }
}
