/**
 * Request
 * 04.04.2012
 * @author Philipp Haussleiter
 *
 */
package models;

import com.sun.xml.internal.bind.CycleRecoverable;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import play.data.validation.URL;
import play.db.jpa.Model;

@Entity
public class Request extends Model implements CycleRecoverable {

    @URL
    public String url;
    public String component;
    public String action;
    @OneToOne
    public VarList params;
    @OneToOne
    public VarList session;
    @OneToOne
    public VarList cgiData;

    public Request(generated.Request request) {
        this.url = request.getUrl();
        this.action = request.getAction();
        this.component = request.getComponent();
        if (request.getParams() != null) {
            this.params = new VarList(request.getParams());
        }
        if (request.getSession() != null) {
            this.session = new VarList(request.getSession());
        }
        if (request.getCgiData() != null) {
            this.cgiData = new VarList(request.getCgiData());
        }
    }

    public Object onCycleDetected(Context cntxt) {
        Request r = Request.findById(this.id);
        return r;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("url:").append(url).append("\n");
        sb.append("action:").append(action).append("\n");
        sb.append("component:").append(component).append("\n");
        sb.append("params:").append(params).append("\n");
        sb.append("session:").append(session).append("\n");
        sb.append("cgiData:").append(cgiData).append("\n");
        return sb.toString();
    }

    public String summarize() {
        StringBuilder sb = new StringBuilder();
        sb.append("url:").append(url).append("<br />");
        sb.append("action:").append(action).append("<br />");
        sb.append("component:").append(component).append("<br />");
        if (params != null) {
            sb.append("params:").append(params.summarize()).append("<br />");
        }
        if (session != null) {
            sb.append("session:").append(session.summarize()).append("<br />");
        }
        if (cgiData != null) {
            sb.append("cgiData:").append(cgiData.summarize()).append("<br />");
        }
        return sb.toString();
    }

    @PrePersist
    public void prePersist() {
        if (params != null) {
            params.save();
            params.saveVars();
        }
        if (session != null) {
            session.save();
            session.saveVars();
        }
        if (cgiData != null) {
            cgiData.save();
            cgiData.saveVars();
        }
    }

    @PreRemove
    public void preRemove() {
        if (params != null) {
            params.delete();
        }
        if (session != null) {
            session.delete();
        }
        if (cgiData != null) {
            cgiData.delete();
        }
    }
}
