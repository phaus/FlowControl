/**
 * Line
 * 04.04.2012
 * @author Philipp Haussleiter
 *
 */
package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.libs.Codec;

@Entity
@XmlRootElement(name = "line")
@XmlAccessorType(XmlAccessType.FIELD)
public class Line extends Model {

    @XmlAttribute
    public String method = "";
    @Required
    @XmlAttribute
    public String file;
    @Required
    @XmlAttribute
    public int number;
    public String hash;
    @ManyToOne
    @XmlTransient
    public Backtrace backtrace;

    public Line(String file, String method, int number) {
        this.method = method;
        this.file = file;
        this.number = number;
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
