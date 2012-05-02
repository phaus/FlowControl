/**
 * Notifier
 * 04.04.2012
 * @author Philipp Haussleiter
 *
 */
package models;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import play.cache.Cache;
import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.Model;

@Entity
@XmlRootElement(name = "notifier")
@XmlAccessorType(XmlAccessType.FIELD)
public class Notifier extends Model {

    @Required
    @XmlElement
    public String name;
    @Required
    @XmlElement
    public String version;
    @URL
    @XmlElement
    public String url;
    @XmlTransient
    @OneToMany(mappedBy = "notifier")
    public List<Notice> notices;

    private Notifier(String name, String version, String url) {
        this.name = name;
        this.version = version;
        this.url = url;
    }

    public static Notifier getOrCreate(String name, String version, String url) {
        String key = "Notifier_" + name + "_" + version;
        Notifier notifier = Cache.get(key, Notifier.class);
        if (notifier == null) {
            notifier = Notifier.find("name = ? and version = ?", name, version).first();
            if (notifier == null) {
                notifier = new Notifier(name, version, url);
                notifier.save();
            }
            Cache.set(key, notifier, "1d");
        }
        return notifier;
    }

    @Override
    public String toString() {
        return this.name + " " + this.version;
    }
}
