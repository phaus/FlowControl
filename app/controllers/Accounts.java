package controllers;

import java.util.List;
import models.Account;
import play.mvc.Controller;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;

public class Accounts extends Controller {
    public static void index() {
        List<Account> entities = models.Account.all().fetch();
        render(entities);
    }

    public static void create(Account entity) {
        render(entity);
    }

    public static void show(java.lang.Long id) {
        Account entity = Account.findById(id);
        render(entity);
    }

    public static void edit(java.lang.Long id) {
        Account entity = Account.findById(id);
        render(entity);
    }

    public static void delete(java.lang.Long id) {
        Account entity = Account.findById(id);
        entity.delete();
        index();
    }
    
    public static void save(@Valid Account entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@create", entity);
        }
        entity.save();
        flash.success(Messages.get("scaffold.created", "Account"));
        index();
    }

    public static void update(@Valid Account entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@edit", entity);
        }
        
              entity = entity.merge();
        
        entity.save();
        flash.success(Messages.get("scaffold.updated", "Account"));
        index();
    }
}
