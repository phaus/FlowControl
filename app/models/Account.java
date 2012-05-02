/**
 * Account
 * 04.04.2012
 * @author Philipp Haussleiter
 *
 */
package models;

import controllers.Application;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.Query;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import play.cache.Cache;
import play.data.binding.As;
import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.JPA;
import play.db.jpa.Model;
import play.libs.Codec;
import play.libs.Crypto;

@Entity
@XmlRootElement(name = "account")
@XmlAccessorType(XmlAccessType.FIELD)
public class Account extends Model {

    @Required
    @XmlElement
    public String uid;
    @Email
    @XmlElement
    public String email;
    @Required
    @XmlTransient
    public String password;
    @ManyToMany
    @XmlElement
    public List<Project> projects;
    @As(lang = {"*"}, value = {"yyyy-MM-dd hh:mm:ss"})
    @XmlElement
    public Date created_at;
    @XmlTransient
    @Transient
    public String clearPassword = null;

    public Account(String uid) {
        this.uid = uid;
        this.created_at = new Date();
    }

    public static Account findOrCreateByUid(String uid) {
        String key = "account_" + uid;
        Account account = Cache.get(key, Account.class);
        if (account == null) {
            account = Account.find("uid = ?", uid).first();
        }
        if (account == null) {
            account = new Account(uid);
            account.clearPassword = Codec.UUID().replace("-", "").substring(0, 8);
            account.setPassword(account.clearPassword);
            account.save();
        }
        return account;
    }

    public void setPassword(String password) {
        this.password = Crypto.passwordHash(password, Crypto.HashType.SHA1);
    }

    public boolean isAdmin() {
        for (String aid : Application.admins) {
            if (this.uid.equals(aid)) {
                return true;
            }
        }
        return false;
    }

    public List<Notice> getNotices(int resolved) {
        String queryString = "SELECT n "
                + "FROM Notice n "
                + "INNER JOIN n.project p "
                + "WHERE n.project = p ";
        switch (resolved) {
            case Notice.RESOLVED:
                queryString += "AND n.resolved = 1 ";
                break;
            case Notice.UNRESOLVED:
                queryString += "AND n.resolved = 0 ";
                break;
            default:
        }
        queryString += "AND p IN ("
                + "SELECT ap FROM Account a INNER JOIN a.projects ap WHERE a = :account"
                + ") "
                + "ORDER BY n.created_at DESC";
        Query query = JPA.em().createQuery(queryString).setParameter("account", this);
        return query.getResultList();
    }

    public List<Notice> getNoticesForPeriodAndProject(int period, Project project, int resolved) {
        String queryString = "SELECT n "
                + "FROM Notice n "
                + "INNER JOIN n.project p "
                + "WHERE n.project = p "
                + "AND n.created_at > :past ";
        switch (resolved) {
            case Notice.RESOLVED:
                queryString += "AND n.resolved = 1 ";
                break;
            case Notice.UNRESOLVED:
                queryString += "AND n.resolved = 0 ";
                break;
            default:
        }
        queryString += "AND p = :project "
                + "ORDER BY n.created_at DESC";
        Query query = JPA.em().createQuery(queryString).setParameter("past", getDateForPeriod(period)).setParameter("project", project);
        return query.getResultList();
    }

    public List<Notice> getNoticesForPeriod(int period, int resolved) {
        String queryString = "SELECT n "
                + "FROM Notice n "
                + "INNER JOIN n.project p "
                + "WHERE n.project = p "
                + "AND n.created_at > :past ";
        switch (resolved) {
            case Notice.RESOLVED:
                queryString += "AND n.resolved = 1 ";
                break;
            case Notice.UNRESOLVED:
                queryString += "AND n.resolved = 0 ";
                break;
            default:
        }
        queryString += "AND p IN ("
                + "SELECT ap FROM Account a INNER JOIN a.projects ap WHERE a = :account"
                + ") "
                + "ORDER BY n.created_at DESC";
        Query query = JPA.em().createQuery(queryString).setParameter("past", getDateForPeriod(period)).setParameter("account", this);
        return query.getResultList();
    }

    public List<Notice> getNoticesForProject(Project project, int resolved) {
        String queryString = "SELECT n "
                + "FROM Notice n "
                + "INNER JOIN n.project p "
                + "WHERE n.project = p ";
        switch (resolved) {
            case Notice.RESOLVED:
                queryString += "AND n.resolved = 1 ";
                break;
            case Notice.UNRESOLVED:
                queryString += "AND n.resolved = 0 ";
                break;
            default:
        }
        queryString += "AND p = :project "
                + "ORDER BY n.created_at DESC";
        Query query = JPA.em().createQuery(queryString).setParameter("project", project);
        return query.getResultList();
    }

    public List<Project> getProjects() {
        return this.projects;
    }

    public void addProject(Project project) {
        if (project != null && project.id != null) {
            this.projects.add(project);
            this.save();
        }
    }

    public void removeProject(Project project) {
        if (project != null && project.id != null) {
            this.projects.remove(project);
            this.save();
        }
    }

    @Override
    public String toString() {
        return this.uid;
    }

    @PrePersist
    public void prePersist() {
        this.created_at = new Date();
    }

    @PreRemove
    public void preRemove() {
        String key = "account_" + uid;
        Cache.delete(key);
    }

    private Date getDateForPeriod(int period) {
        GregorianCalendar now = new GregorianCalendar();
        switch (period) {
            case Calendar.YEAR:
                now.add(period, -4);
                return now.getTime();
            case Calendar.HOUR_OF_DAY:
                now.add(period, -24);
                return now.getTime();
            case Calendar.MONTH:
                now.add(period, -12);
                return now.getTime();
            case Calendar.DAY_OF_MONTH:
                now.add(period, -31);
                return now.getTime();
            default:
                return now.getTime();
        }
    }
}
