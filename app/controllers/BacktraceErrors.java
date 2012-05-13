package controllers;

import play.Logger;
import java.util.List;
import models.Backtrace;
import models.Error;
import models.Project;
import play.cache.Cache;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;
import static play.modules.api.RenderXml.*;

public class BacktraceErrors extends Application {

    public static void index() {
        Logger.info("BacktraceErrors->index" + request.format);
        Long project_id = getActiveProjectId();
        Long traceId = params.get("trace_id", Long.class);
        Backtrace trace = null;
        List<Project> projects = currentAccount.getProjects();
        List<Error> entities;
        if (traceId != null) {
            trace = Backtrace.findById(traceId);
        }
        if (project_id == null) {
            entities = Error.findErrorsByAccount(currentAccount);
            render(entities, projects, trace, traceId);
        } else {
            Project project = Project.findById(project_id);
            entities = Error.findErrorsByAccountAndProject(currentAccount, project);
            //project.getErrorsForAccount(currentAccount);

            render(entities, projects, trace, project, traceId);
        }
    }

    public static void create(Error entity) {
        render(entity);
    }

    public static void show(java.lang.Long id) {
        Error entity = Error.findById(id);
        if("xml".equals(request.format)){
            renderXML(entity);
        }else{
            render(entity);
        }
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
        index();
    }
}
