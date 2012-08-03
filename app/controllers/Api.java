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
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http.Header;
import play.mvc.Http.Request;

public class Api extends Controller {


    // TODO Refactore all the Secure/Application.java Stuff :-).
    @Before
    public static void debugRequest(){
        StringBuilder sb = new StringBuilder("\n");
        Header h;
        sb.append("url: ").append(request.url).append("\n");
        sb.append("host: ").append(request.host).append("\n");
        if(request.secure){
            sb.append("secure Request\n");
        }
        for( String key : request.headers.keySet()){
            h = request.headers.get(key);
            sb.append("\t").append(key).append(":").append(h.toString()).append("\n");
        }
        Logger.debug("headers: "+sb.toString());
    }

    public static void notices(generated.Notice noticeMessage) {
        Notice notice = new Notice(noticeMessage);
        if (!isContentXML(request)) {
            error("MIME Type not valid. Please use 'application/xml' ");
        } else if (!Validation.current().valid(noticeMessage).ok) {
            Logger.info("errors: " + Validation.errors());
            error("request not valid");
        } else {
            notice.save();
            renderText("Notice @"+notice.id+" "+notice.toString());
        }
    }

    public static void verify() {
        ok();
    }

    private static boolean isContentXML(Request r) {

        for (String key : r.headers.keySet()) {
            if ("content-type".equals(key)) {
                Header h = r.headers.get(key);
                String contentType = h.value();
                Logger.debug("content-type: "+contentType);
                if (h != null && (contentType.startsWith("text/xml") || contentType.startsWith("application/xml"))) {
                    return true;
                }
            }
        }
        return false;
    }
}
