/**
 * Feed
 * 28.04.2012
 * @author Philipp Haussleiter
 *
 */
package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import models.Notice;
import models.Project;
import play.libs.Codec;

public class Feeds extends Application {

    private final static SimpleDateFormat FORMATTER = new SimpleDateFormat("dd MMM yyyy HH:mm:ss Z");

    public static void index() {
        List<Notice> entities = currentAccount.getNotices(Notice.UNRESOLVED);
        String updated = FORMATTER.format(new Date());
        String uuid = Codec.UUID();
        render(uuid, updated, entities);
    }

    public static void show(String apiKey) {
        Project project = Project.findByApiKey(apiKey);
        List<Notice> entities = currentAccount.getNoticesForProject(project, Notice.UNRESOLVED);
        String updated = FORMATTER.format(new Date());
        String uuid = Codec.UUID();
        render(project, uuid, updated, entities);
    }
}
