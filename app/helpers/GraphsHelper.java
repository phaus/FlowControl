/**
 * GraphsHelper
 * 29.04.2012
 * @author Philipp Haussleiter
 *
 */
package helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import models.Notice;

public class GraphsHelper {

    public final static int YEAR = Calendar.YEAR;
    public final static int MONTH = Calendar.MONTH;
    public final static int DAY = Calendar.DAY_OF_MONTH;
    public final static int HOUR = Calendar.HOUR_OF_DAY;
    private static Calendar calendar = Calendar.getInstance();

    public static Map createMapFromNotices(List<Notice> notices, int period, int resolved) {
        SimpleDateFormat formatter = getFormatter(period);
        Map map = new LinkedHashMap<Integer, Integer>();
        int value;
        for (int i = getFirst(period); i <= getLast(period); i++) {
            map.put(i, 0);
        }
        if (resolved == Notice.RESOLVED) {
            for (Notice n : notices) {
                value = Integer.parseInt(formatter.format(n.updated_at));
                map = addValue(map, value);
            }
        } else {
            for (Notice n : notices) {
                value = Integer.parseInt(formatter.format(n.created_at));
                map = addValue(map, value);
            }
        }
        return map;
    }

    public static int getMax(Map map) {
        int max = 0;
        for (Object key : map.keySet()) {
            max = Math.max(max, Integer.parseInt(map.get(key).toString()));
        }
        return max;
    }

    private static Map addValue(Map map, Integer key) {
        if (map.get(key) != null) {
            int value = Integer.parseInt(map.get(key).toString());
            value += 1;
            map.put(key, value);
        }
        return map;
    }

    public static int getWidth(int period) {
        switch (period) {
            case YEAR:
                return 240;
            case MONTH:
                return 360;
            case DAY:
                return 640;
            case HOUR:
            default:
                return 480;
        }
    }

    public static int getFirst(int period) {
        switch (period) {
            case YEAR:
                return calendar.get(Calendar.YEAR) - 2;
            case MONTH:
            case DAY:
                return 0;
            case HOUR:
            default:
                return 0;
        }
    }

    public static int getLast(int period) {
        switch (period) {
            case YEAR:
                return calendar.get(Calendar.YEAR) + 2;
            case MONTH:
                return 12;
            case DAY:
                return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            case HOUR:
                return calendar.getActualMaximum(Calendar.HOUR_OF_DAY);
            default:
                return calendar.get(Calendar.YEAR) + 2;
        }
    }

    private static SimpleDateFormat getFormatter(int period) {
        switch (period) {
            case YEAR:
                return new SimpleDateFormat("yyyy");
            case MONTH:
                return new SimpleDateFormat("MM");
            case DAY:
                return new SimpleDateFormat("dd");
            case HOUR:
                return new SimpleDateFormat("HH");
            default:
                return new SimpleDateFormat("yyyy");
        }
    }
}
