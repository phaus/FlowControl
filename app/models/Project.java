/**
 * Project
 * 08.04.2012
 * @author Philipp Haussleiter
 *
 */
package models;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.Query;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import play.cache.Cache;
import play.data.binding.As;
import play.data.validation.URL;
import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
public class Project extends Model {

    @XmlElement
    @Column(unique = true)
    public String name;
    @URL
    @XmlElement
    public String url;
    @As(lang = {"*"}, value = {"yyyy-MM-dd hh:mm:ss"})
    @XmlElement
    public Date created_at;
    @OneToOne
    @XmlElement
    public ApiKey apiKey;
    @XmlTransient
    @OneToMany(mappedBy = "project")
    public List<Notice> notices;

    public static Project findByApiKey(String apiKey) {
        String key = "project_" + apiKey;
        Project project = Cache.get(key, Project.class);
        if (project == null) {
            Query query = JPA.em().createQuery("SELECT p "
                    + "FROM Project p, ApiKey ak "
                    + "WHERE p.apiKey = ak "
                    + "AND ak.apiKey = :ak").setParameter("ak", apiKey);
            if (query.getResultList().size() > 0) {
                project = (Project) query.getResultList().get(0);
                Cache.set(key, project, "30d");
            }
        }
        return project;
    }

    public List<Account> getAccounts() {
        Query query = JPA.em().createQuery("SELECT a "
                + "FROM Account a "
                + "INNER JOIN a.projects p "
                + "WHERE p = :project").setParameter("project", this);
        return query.getResultList();
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
                + "INNER JOIN n.project p "
                + "WHERE n.project = :project ";
        switch (resolved) {
            case Notice.RESOLVED:
                queryString += "AND n.resolved = 1 ";
                break;
            case Notice.UNRESOLVED:
                queryString += "AND n.resolved = 0 ";
                break;
            default:
        }
        Query query = JPA.em().createQuery(queryString).setParameter("project", this);
        return query.getResultList();
    }

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            if (this.apiKey == null) {
                this.apiKey = new ApiKey();
                this.apiKey.save();
            }
            this.created_at = new Date();
        }
    }

    @PreRemove
    public void preRemove() {
        for (Account a : this.getAccounts()) {
            a.removeProject(this);
        }
        for (Notice n : this.notices) {
            n.delete();
        }
        this.apiKey.delete();

        String key = "project_" + this.apiKey;
        Cache.delete(key);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
