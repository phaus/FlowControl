/**
 * Backtrace
 * 04.04.2012
 * @author Philipp Haussleiter
 *
 */
package models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import play.db.jpa.Model;

@Entity
public class Backtrace extends Model {

    @OneToMany(mappedBy = "backtrace", cascade = CascadeType.REMOVE)
    List<Line> lines;

    public static void saveLines(Backtrace backtrace) {
        for (Line line : backtrace.lines) {
            line.backtrace = backtrace;
            line.save();
        }
    }

    public Backtrace(generated.Backtrace backtrace) {
        lines = new ArrayList<Line>();
        for (generated.Backtrace.Line line : backtrace.getLine()) {
            lines.add(new Line(line));
        }
    }

    public String summarize() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.size() && i < 10; i++) {
            sb.append(lines.get(i)).append("<br>\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            sb.append(lines.get(i)).append("\n");
        }
        return sb.toString().trim();
    }
}
