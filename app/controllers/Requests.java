package controllers;

import java.util.List;
import models.Request;
import play.i18n.Messages;
import play.data.validation.Valid;

public class Requests extends Application {

    public static void index() {
        List<Request> entities = models.Request.all().fetch();
        render(entities);
    }

    public static void create(Request entity) {
        render(entity);
    }

    public static void show(java.lang.Long id) {
        Request entity = Request.findById(id);
        render(entity);
    }

    public static void edit(java.lang.Long id) {
        Request entity = Request.findById(id);
        render(entity);
    }

    public static void delete(java.lang.Long id) {
        Request entity = Request.findById(id);
        entity.delete();
        index();
    }

    public static void save(@Valid Request entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@create", entity);
        }
        entity.save();
        flash.success(Messages.get("scaffold.created", "Request"));
        index();
    }

    public static void update(@Valid Request entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@edit", entity);
        }
        entity = entity.merge();
        entity.save();
        flash.success(Messages.get("scaffold.updated", "Request"));
        index();
    }
}
