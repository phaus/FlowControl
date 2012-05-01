package controllers;

import java.util.List;
import models.Backtrace;
import play.mvc.Controller;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;

public class Backtraces extends Controller {
    public static void index() {
        List<Backtrace> entities = models.Backtrace.all().fetch();
        render(entities);
    }

    public static void create(Backtrace entity) {
        render(entity);
    }

    public static void show(java.lang.Long id) {
        Backtrace entity = Backtrace.findById(id);
        models.Error error = models.Error.find("backtrace = ?", entity).first();
        render(entity, error);
    }

    public static void edit(java.lang.Long id) {
        Backtrace entity = Backtrace.findById(id);
        render(entity);
    }

    public static void delete(java.lang.Long id) {
        Backtrace entity = Backtrace.findById(id);
        entity.delete();
        index();
    }
    
    public static void save(@Valid Backtrace entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@create", entity);
        }
        entity.save();
        flash.success(Messages.get("scaffold.created", "Backtrace"));
        index();
    }

    public static void update(@Valid Backtrace entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@edit", entity);
        }
        
              entity = entity.merge();
        
        entity.save();
        flash.success(Messages.get("scaffold.updated", "Backtrace"));
        index();
    }
}
