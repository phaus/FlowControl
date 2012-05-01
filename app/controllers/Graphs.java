/**
 * Graphs
 * 29.04.2012
 * @author Philipp Haussleiter
 *
 */
package controllers;

import helpers.GraphsHelper;
import java.util.List;
import java.util.Map;
import models.Notice;
import models.Project;

public class Graphs extends Application {

    public static void notices() {
        Integer period = params.get("period", Integer.class);
        Long project_id = params.get("project_id", Long.class);
        List<Notice> resolved;
        List<Notice> unresolved;

        if (period == null) {
            period = GraphsHelper.HOUR;
        }
        if (project_id != null) {
            Project project = Project.findById(project_id);
            resolved = currentAccount.getNoticesForPeriodAndProject(period, project, Notice.RESOLVED);
            unresolved = currentAccount.getNoticesForPeriodAndProject(period, project, Notice.UNRESOLVED);

        } else {
            resolved = currentAccount.getNoticesForPeriod(period, Notice.RESOLVED);
            unresolved = currentAccount.getNoticesForPeriod(period, Notice.UNRESOLVED);

        }
        Map openEntries = GraphsHelper.createMapFromNotices(unresolved, period);
        Map resolvedEntries = GraphsHelper.createMapFromNotices(resolved, period);

        int width = 480;
        int height = 200;
        int gap = GraphsHelper.getGap(period);
        int max = GraphsHelper.getMax(openEntries);
        int xpart = width / GraphsHelper.getLast(period);
        int ypart = max > 0 ? (height - 50) / max : 0;
        int init = -xpart;
        render(openEntries, resolvedEntries, width, height, xpart, ypart, gap, init);
    }
}
