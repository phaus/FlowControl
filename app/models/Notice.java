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
import play.Logger;
import play.data.binding.As;
import play.data.validation.Check;
import play.data.validation.CheckWith;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Notice extends Model implements CycleRecoverable {

    public final static int ALL = 0;
    public final static int RESOLVED = 1;
    public final static int UNRESOLVED = 2;
    private final static SimpleDateFormat FORMATTER = new SimpleDateFormat("dd MMM yyyy HH:mm:ss Z");
    @Required
    public String version;
    @As(lang = {"*"}, value = {"yyyy-MM-dd hh:mm:ss"})
    public Date created_at;
    @As(lang = {"*"}, value = {"yyyy-MM-dd hh:mm:ss"})
    public Date updated_at;
    @Required
    @CheckWith(ApiKeyCheck.class)
    public String apiKey;
    @OneToOne(cascade = CascadeType.REMOVE)
    public Request request;
    @ManyToOne
    public Notifier notifier;
    @OneToOne(cascade = CascadeType.REMOVE)
    @ManyToOne
    public Error error;
    @ManyToOne
    public ServerEnvironment environment;
    @ManyToOne
    public Project project;
    public boolean resolved;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.created_at = new Date();
            this.updated_at = new Date();
            this.project = Project.findByApiKey(this.apiKey);
            if (this.request != null) {
                this.request.save();
            }
            if(this.error != null) {
                this.error = this.error.getErrorReference();
            }
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updated_at = new Date();
    }

    public Notice(generated.Notice notice) {
        if(notice != null){
            this.version = notice.getVersion();
            this.apiKey = notice.getApiKey();
            if (notice.getNotifier() != null) {
                this.notifier = Notifier.getOrCreate(notice.getNotifier());
            }
            if (notice.getError() != null) {
                this.error = new Error(notice.getError());
            }
            if(notice.getRequest() != null){
                this.request = new Request(notice.getRequest());
            }
            if(notice.getServerEnvironment() != null){
                this.environment = ServerEnvironment.getOrCreate(notice.getServerEnvironment());
                Logger.info("found: "+this.environment);
            }
        } else {
            Logger.error("generated.Notice notice is null!");
        }

    }

    public String getPublished() {
        return FORMATTER.format(created_at);
    }

    public String getUpdated() {
        return FORMATTER.format(updated_at);
    }

    @Override
    public String toString() {
        return "["+error.toString()+"]";
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
