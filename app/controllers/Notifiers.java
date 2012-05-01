package controllers;

import java.util.List;
import models.Notifier;
import play.mvc.Controller;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;

public class Notifiers extends Controller {
    public static void index() {
        List<Notifier> entities = models.Notifier.all().fetch();
        render(entities);
    }

    public static void create(Notifier entity) {
        render(entity);
    }

    public static void show(java.lang.Long id) {
        Notifier entity = Notifier.findById(id);
        render(entity);
    }

    public static void edit(java.lang.Long id) {
        Notifier entity = Notifier.findById(id);
        render(entity);
    }

    public static void delete(java.lang.Long id) {
        Notifier entity = Notifier.findById(id);
        entity.delete();
        index();
    }
    
    public static void save(@Valid Notifier entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@create", entity);
        }
        entity.save();
        flash.success(Messages.get("scaffold.created", "Notifier"));
        index();
    }

    public static void update(@Valid Notifier entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@edit", entity);
        }
        
              entity = entity.merge();
        
        entity.save();
        flash.success(Messages.get("scaffold.updated", "Notifier"));
        index();
    }
}
