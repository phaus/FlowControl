package controllers;

import java.io.File;
import java.util.List;
import models.Account;
import models.Error;
import models.Notice;
import models.Project;
import play.*;
import play.mvc.*;
import static render.RenderXml.*;

import play.data.validation.Validation;
import play.libs.IO;

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
        Logger.info("count errors: "+errorList.size());
        List<Project> projects = currentAccount.getProjects();
        render(projects, errorList);
    }

    public static void about() {
        render();
    }

    public static void notice(Notice notice) {
        Logger.debug(notice.toString());
        if (!Validation.current().valid(notice).ok) {
            Logger.info("errors: " + Validation.errors());
            badRequest();
        } else {
            notice.save();
            renderXML(notice);
        }
    }

    public static void form() {
        render();
    }

    // TODO move to Helper
    public static String loadExample(String example) {
        Logger.info("loading example " + example);
        return IO.readContentAsString(new File("test/" + example)).trim();
    }
}
