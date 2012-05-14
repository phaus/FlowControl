package controllers;

import java.io.File;
import java.util.List;
import models.Account;
import models.Error;
import models.Notice;
import models.Project;
import play.Logger;
import play.Play;
import play.data.validation.Validation;
import play.libs.IO;
import play.mvc.Before;
import play.mvc.Controller;

public class Application extends Controller {

    // TODO replace with LDAP FOO
    public static String defaultUser = Play.configuration.getProperty("flowcontrol.default_user");
    public static String[] admins = Play.configuration.getProperty("flowcontrol.admins").split(",");
    public static Account currentAccount;

    @Before
    public static void before() {
        Application.currentAccount = Account.findOrCreateByUid(defaultUser);
    }

    public static void index() {
        List<Error> errorList = Error.findErrorsByAccount(currentAccount);
        List<Project> projects = currentAccount.getProjects();
        render(projects, errorList);
    }

    public static void about() {
        render();
    }

    public static void notice(Notice notice) {
        if (request.headers.get("Content-Type") == null
                || !request.headers.get("Content-Type").value().startsWith("application/xml")) {
            error("MIME Type not valid. Please use 'application/xml' ");
        } else if(!Validation.current().valid(notice).ok) {
            Logger.info("errors: " + Validation.errors());
            error("request not valid");
        } else {
            Logger.debug(notice.toString());
            notice.save();
            ok();
        }
    }

    public static void form() {
        render();
    }

    // TODO move to Helper
    public static String loadExample(String example) {
        Logger.info("loading example " + example);
        return IO.readContentAsString(new File(Play.applicationPath + "/test/" + example)).trim();
    }
}
