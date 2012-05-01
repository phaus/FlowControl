/**
 * Backtrace
 * 04.04.2012
 * @author Philipp Haussleiter
 *
 */
package models;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import play.db.jpa.Model;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Backtrace extends Model {

    @OneToMany(mappedBy = "backtrace", cascade = CascadeType.REMOVE)
    @XmlElement(name = "line")
    List<Line> lines;

    public static void saveLines(Backtrace backtrace) {
        for (Line line : backtrace.lines) {
            line.backtrace = backtrace;
            line.save();
        }
    }

    public String summarize(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.size() && i < 10; i++) {
            sb.append(lines.get(i)).append("<br>\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.size() && i < 10; i++) {
            sb.append(lines.get(i)).append("\n");
        }
        return sb.toString();
    }
}
