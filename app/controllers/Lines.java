package controllers;

import java.util.List;
import models.Line;
import play.mvc.Controller;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;

public class Lines extends Controller {
    public static void index() {
        List<Line> entities = models.Line.all().fetch();
        render(entities);
    }

    public static void create(Line entity) {
        render(entity);
    }

    public static void show(java.lang.Long id) {
        Line entity = Line.findById(id);
        render(entity);
    }

    public static void edit(java.lang.Long id) {
        Line entity = Line.findById(id);
        render(entity);
    }

    public static void delete(java.lang.Long id) {
        Line entity = Line.findById(id);
        entity.delete();
        index();
    }
    
    public static void save(@Valid Line entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@create", entity);
        }
        entity.save();
        flash.success(Messages.get("scaffold.created", "Line"));
        index();
    }

    public static void update(@Valid Line entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@edit", entity);
        }
        
              entity = entity.merge();
        
        entity.save();
        flash.success(Messages.get("scaffold.updated", "Line"));
        index();
    }
}
