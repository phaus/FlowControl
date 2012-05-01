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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import play.data.validation.URL;
import play.db.jpa.Model;

@Entity
@XmlRootElement(name = "request")
@XmlAccessorType(XmlAccessType.FIELD)
public class Request extends Model implements CycleRecoverable {

    @URL
    @XmlElement
    public String url;
    @XmlElement
    public String component;
    @XmlElement
    public String action;
//    @OneToOne
//    @XmlElement
//    public VarList params;
//    @OneToOne
//    @XmlElement
//    public VarList session;
//    @OneToOne
//    @XmlElement(name = "cgi-data")
//    public VarList cgiData;

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
//        sb.append("params:").append(params).append("\n");
//        sb.append("session:").append(session).append("\n");
//        sb.append("cgiData:").append(cgiData).append("\n");
        return sb.toString();
    }

    public String summarize() {
        StringBuilder sb = new StringBuilder();
        sb.append("url:").append(url).append("<br />");
        sb.append("action:").append(action).append("<br />");
        sb.append("component:").append(component).append("<br />");
//        if (params != null) {
//            sb.append("params:").append(params.summarize()).append("<br />");
//        }
//        if (session != null) {
//            sb.append("session:").append(session.summarize()).append("<br />");
//        }
//        if (cgiData != null) {
//            sb.append("cgiData:").append(cgiData.summarize()).append("<br />");
//        }
        return sb.toString();
    }

//    @PrePersist
//    public void prePersist() {
//        if (params != null) {
//            params.save();
//        }
//        if (session != null) {
//            session.save();
//        }
//        if (cgiData != null) {
//            cgiData.save();
//        }
//    }
//
//    @PreRemove
//    public void preRemove() {
//        if (params != null) {
//            params.delete();
//        }
//        if (session != null) {
//            session.delete();
//        }
//        if (cgiData != null) {
//            cgiData.delete();
//        }
//    }
}
