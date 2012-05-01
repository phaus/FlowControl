/**
 * Graphs
 * 29.04.2012
 * @author Philipp Haussleiter
 *
 */
package controllers;

import helpers.GraphsHelper;
import java.util.Map;
import models.Notice;

public class Graphs extends Application {
    public static void notices(){
        int period = GraphsHelper.HOUR;
        Map openEntries = GraphsHelper.createMapFromNotices(currentAccount.getNoticesForPeriod(period, Notice.UNRESOLVED), period);
        Map resolvedEntries = GraphsHelper.createMapFromNotices(currentAccount.getNoticesForPeriod(period, Notice.RESOLVED), period);
        int width = 480;
        int height = 200;
        int gap = GraphsHelper.getGap(period);
        int max = GraphsHelper.getMax(openEntries);
        int xpart = width / GraphsHelper.getLast(period);
        int ypart = max > 0 ? (height-50) / max : 0;
        int init = -xpart;
        render(openEntries, resolvedEntries, width, height, xpart, ypart, gap, init);
    }
}
