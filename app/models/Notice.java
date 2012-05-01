/**
 * Notice
 * 04.04.2012
 * @author Philipp Haussleiter
 *
 */
package models;

import com.sun.xml.internal.bind.CycleRecoverable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import play.Logger;
import play.data.binding.As;
import play.data.validation.Check;
import play.data.validation.CheckWith;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
@XmlRootElement(name = "notice")
@XmlAccessorType(XmlAccessType.FIELD)
public class Notice extends Model implements CycleRecoverable {

    public final static boolean RESOLVED = true;
    public final static boolean UNRESOLVED = false;

    private final static SimpleDateFormat FORMATTER = new SimpleDateFormat("dd MMM yyyy HH:mm:ss Z");
    @Required
    @XmlAttribute
    public String version;
    @As(lang = {"*"}, value = {"yyyy-MM-dd hh:mm:ss"})
    @XmlElement
    public Date created_at;
    @As(lang = {"*"}, value = {"yyyy-MM-dd hh:mm:ss"})
    @XmlElement
    public Date updated_at;
    @XmlElement(name = "api-key")
    @Required
    @CheckWith(ApiKeyCheck.class)
    public String apiKey;
    @OneToOne(cascade = CascadeType.REMOVE)
    @XmlElement
    public Request request;
    @ManyToOne
    @XmlElement
    public Notifier notifier;
    @OneToOne(cascade = CascadeType.REMOVE)
    @XmlElement
    public Error error;
    @ManyToOne
    @XmlElement(name = "server-environment")
    public ServerEnvironment environment;
    @ManyToOne
    @XmlTransient
    public Project project;
    @XmlTransient
    public boolean resolved;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.created_at = new Date();
            this.updated_at = new Date();
            this.project = Project.findByApiKey(this.apiKey);
            this.notifier = Notifier.getOrCreate(notifier.name, notifier.version, notifier.url);
            this.environment = ServerEnvironment.getOrCreate(environment.projectRoot, environment.environmentName, environment.appVersion, environment.hostname);
            if (this.request != null) {
                this.request.save();
            }
            this.error.save();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updated_at = new Date();
    }

    public String getPublished() {
        return FORMATTER.format(created_at);
    }

    public String getUpdated() {
        return FORMATTER.format(updated_at);
    }

    @Override
    public String toString() {
        return "notice for " + project + ":" + id + ":" + created_at;
    }

    public Object onCycleDetected(Context cntxt) {
        Notice notice = Notice.findById(this.id);
        return notice;
    }

    public static class ApiKeyCheck extends Check {
        public boolean isSatisfied(Object notice, Object apiKey) {
            if (ApiKey.findByApiKey(apiKey.toString()) != null) {
                Logger.debug(apiKey + "is valid");
                return true;
            }
            Logger.debug(apiKey + "is invalid");
            return false;
        }
    }
}
