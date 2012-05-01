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
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Query;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import play.data.validation.Required;
import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.FIELD)
public class Error extends Model {

    @Required
    @XmlElement(name = "class")
    public String clazz;
    @XmlElement
    public String message;
    @OneToOne(cascade = CascadeType.REMOVE)
    @XmlElement(name = "backtrace")
    public Backtrace backtrace;

    public static List<Error> findErrorsByAccount(Account currentAccount) {
        Query query = JPA.em().createQuery("SELECT e FROM Notice n "
                + "INNER JOIN n.error e "
                + "INNER JOIN n.project p "
                + "WHERE p IN ("
                + "SELECT ap FROM Account a INNER JOIN a.projects ap WHERE a = :account"
                + ")").setParameter("account", currentAccount);
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
