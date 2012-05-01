package controllers;

import java.io.File;
import java.util.List;
import play.*;
import play.mvc.*;
import static render.RenderXml.*;

import models.*;
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
        List<Project> projects = currentAccount.getProjects();
        render(projects);
    }

    public static void about() {
        String sampleContent = IO.readContentAsString(new File("test/request1.xml")).trim();
        render(sampleContent);
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
}
