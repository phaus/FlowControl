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
import play.data.validation.Required;
import play.db.jpa.JPA;
import play.db.jpa.Model;
import play.libs.Codec;

@Entity
public class Error extends Model {

    @Required
    public String clazz;
    public String message;
    public String hash;
    @OneToOne(cascade = CascadeType.REMOVE)
    public Backtrace backtrace;
    @OneToMany(mappedBy = "error")
    public List<Notice> notices;

    public Error(generated.Error error) {
        this.clazz = error.getClazz();
        this.message = error.getMessage();
        if (error.getBacktrace() != null) {
            this.backtrace = new Backtrace(error.getBacktrace());
        }
    }

    public static List<Error> findErrorsByAccount(Account currentAccount) {
        Query query = JPA.em().createQuery("SELECT e "
                + "FROM Notice n "
                + "INNER JOIN n.error e "
                + "INNER JOIN n.project p "
                + "WHERE n.resolved = 0 "
                + "AND p IN ("
                + "SELECT ap FROM Account a INNER JOIN a.projects ap WHERE a = :account"
                + ") GROUP BY e.id "
                + "ORDER by count(n.id) DESC ").setParameter("account", currentAccount);
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

    public List<Notice> getResolvedNotices(Account currentAccount) {
        return getNoticesWithState(currentAccount, Notice.RESOLVED);
    }

    public List<Notice> getUnresolvedNotices(Account currentAccount) {
        return getNoticesWithState(currentAccount, Notice.UNRESOLVED);
    }

    public List<Notice> getNoticesWithState(Account currentAccount, int resolved) {
        String queryString = "SELECT n "
                + "FROM Notice n "
                + "INNER JOIN n.project p "
                + "WHERE  p IN ("
                + "SELECT ap FROM Account a INNER JOIN a.projects ap WHERE a = :account"
                + ")";
        switch (resolved) {
            case Notice.RESOLVED:
                queryString += "AND n.resolved = 1 ";
                break;
            case Notice.UNRESOLVED:
                queryString += "AND n.resolved = 0 ";
                break;
            default:
        }
        queryString += "AND n.error = :error ";
        queryString += "ORDER BY n.created_at DESC";
        Query query = JPA.em().createQuery(queryString).setParameter("account", currentAccount).setParameter("error", this);
        return query.getResultList();
    }

    public List<Notice> getNotices(Account currentAccount) {
        String queryString = "SELECT n "
                + "FROM Notice n "
                + "INNER JOIN n.project p "
                + "WHERE p IN ("
                + "SELECT ap FROM Account a INNER JOIN a.projects ap WHERE a = :account"
                + ") "
                + "AND n.error = :error ";
        queryString += "ORDER BY n.created_at DESC";
        Query query = JPA.em().createQuery(queryString).setParameter("account", currentAccount).setParameter("error", this);
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
