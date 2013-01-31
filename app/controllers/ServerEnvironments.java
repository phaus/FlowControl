package controllers;

import java.util.List;
import models.ServerEnvironment;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.i18n.Messages;

public class ServerEnvironments extends Application {
    public static void index() {
        List<ServerEnvironment> entities = models.ServerEnvironment.all().fetch();
        render(entities);
    }

    public static void create(ServerEnvironment entity) {
        render(entity);
    }

    public static void show(java.lang.Long id) {
        ServerEnvironment entity = ServerEnvironment.findById(id);
        render(entity);
    }

    public static void edit(java.lang.Long id) {
        ServerEnvironment entity = ServerEnvironment.findById(id);
        render(entity);
    }

    public static void delete(java.lang.Long id) {
        ServerEnvironment entity = ServerEnvironment.findById(id);
        entity.delete();
        index();
    }
    
    public static void save(@Valid ServerEnvironment entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@create", entity);
        }
        entity.save();
        flash.success(Messages.get("scaffold.created", "ServerEnvironment"));
        index();
    }

    public static void update(@Valid ServerEnvironment entity) {
        if (Validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@edit", entity);
        }
        
              entity = entity.merge();
        
        entity.save();
        flash.success(Messages.get("scaffold.updated", "ServerEnvironment"));
        index();
    }
}
