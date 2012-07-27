/**
 * Line
 * 04.04.2012
 * @author Philipp Haussleiter
 *
 */
package models;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.libs.Codec;

@Entity
public class Line extends Model {

    @Lob
    public String method = "";
    @Required
    @Lob
    public String file;
    @Required
    public String number;
    public String hash;
    @ManyToOne
    public Backtrace backtrace;

    public Line(String file, String method, String number) {
        this.method = method;
        this.file = file;
        this.number = number;
    }

    public Line(generated.Backtrace.Line line) {
        this.method = line.getMethod();
        this.number = line.getNumber();
        this.file = line.getFile();
    }

    @Override
    public String toString() {
        return method + " (" + file + ":" + number + ")";
    }

    @PrePersist
    private void hash() {
        this.hash = Codec.hexSHA1(file + "#" + method + "#" + number);
    }
}
