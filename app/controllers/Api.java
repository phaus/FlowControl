/**
 * NotifierApis
 * 14.05.2012
 * @author Philipp Haussleiter
 *
 */
package controllers;

import models.Notice;
import play.Logger;
import play.data.validation.Validation;
import play.mvc.Controller;
import play.mvc.Http.Header;
import play.mvc.Http.Request;
import static play.modules.api.RenderXml.*;

public class Api extends Controller {

    public static void notices(generated.Notice noticeMessage) {
        Notice notice = new Notice(noticeMessage);
        if (!isContentXML(request)) {
            error("MIME Type not valid. Please use 'application/xml' ");
        } else if (!Validation.current().valid(noticeMessage).ok) {
            Logger.info("errors: " + Validation.errors());
            error("request not valid");
        } else {
            notice.save();
            renderXML(noticeMessage);
        }
    }

    private static boolean isContentXML(Request r) {
        for (String key : r.headers.keySet()) {
            if ("content-type".equals(key)) {
                Header h = r.headers.get(key);
                if (h != null && h.value().startsWith("application/xml")) {
                    return true;
                }
            }
        }
        return false;
    }
}
