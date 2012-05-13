/**
 * Error
 * 04.04.2012
 * @author Philipp Haussleiter
 *
 */
package models;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Query;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import play.data.validation.Required;
import play.db.jpa.JPA;
import play.db.jpa.Model;
import play.libs.Codec;

@Entity
@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.FIELD)
public class Error extends Model {

    @Required
    @XmlElement(name = "class")
    public String clazz;
    @XmlElement
    public String message;
    @XmlTransient
    public String hash;
    @OneToOne(cascade = CascadeType.REMOVE)
    @XmlElement(name = "backtrace")
    public Backtrace backtrace;
    @XmlTransient
    @OneToMany(mappedBy = "error")
    public List<Notice> notices;

    public static List<Error> findErrorsByAccount(Account currentAccount) {
        Query query = JPA.em().createQuery("SELECT e "
                + "FROM Notice n "
                + "INNER JOIN n.error e "
                + "INNER JOIN n.project p "
                + "WHERE n.resolved = 0 "
                + "AND p IN ("
                + "SELECT ap FROM Account a INNER JOIN a.projects ap WHERE a = :account"
                + ") GROUP BY e.id "
                + "ORDER by count(n.id) ASC ").setParameter("account", currentAccount);
        return query.getResultList();
    }

    public static List<Error> findErrorsByAccountAndProject(Account currentAccount, Project project) {
        Query query = JPA.em().createQuery("SELECT e "
                + "FROM Notice n "
                + "INNER JOIN n.error e "
                + "INNER JOIN n.project p "
                + "WHERE n.resolved = 0 "
                + "and p = :project "
                + "AND p IN ("
                + "SELECT ap FROM Account a INNER JOIN a.projects ap WHERE a = :account"
                + ") GROUP BY e.id "
                + "ORDER by count(n.id) ASC ").setParameter("account", currentAccount).setParameter("project", project);
        return query.getResultList();
    }

    public Error getErrorReference() {
        this.hash = Codec.hexSHA1(clazz + "#" + message + "#" + backtrace);
        Error error = Error.find("hash = ?", this.hash).first();
        if (error == null) {
            return this.save();
        } else {
            return error;
        }
    }

    public List<Notice> getResolvedNotices() {
        return getNoticesWithState(Notice.RESOLVED);
    }

    public List<Notice> getUnresolvedNotices() {
        return getNoticesWithState(Notice.UNRESOLVED);
    }

    public List<Notice> getNoticesWithState(int resolved) {
        String queryString = "SELECT n "
                + "FROM Notice n "
                + "WHERE n.error = :error ";
        switch (resolved) {
            case Notice.RESOLVED:
                queryString += "AND n.resolved = 1 ";
                break;
            case Notice.UNRESOLVED:
                queryString += "AND n.resolved = 0 ";
                break;
            default:
        }
        queryString += "ORDER BY n.created_at DESC";
        Query query = JPA.em().createQuery(queryString).setParameter("error", this);
        return query.getResultList();
    }

    @PrePersist
    public void prePersist() {
        this.backtrace.save();
        Backtrace.saveLines(this.backtrace);
    }

    public String summarize() {
        StringBuilder sb = new StringBuilder(toString());
        sb.append("<br />").append(backtrace.summarize());
        return sb.toString();
    }

    @Override
    public String toString() {
        return this.message + " at " + this.clazz;
    }
}
