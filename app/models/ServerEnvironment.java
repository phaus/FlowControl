/**
 * ServerEnvironment
 * 04.04.2012
 * @author Philipp Haussleiter
 *
 */
package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import play.cache.Cache;
import play.db.jpa.Model;

@Entity
@XmlRootElement(name = "server-environment")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServerEnvironment extends Model {

    @XmlElement(name = "project-root")
    public String projectRoot;
    @XmlElement(name = "environment-name")
    public String environmentName;
    @XmlElement(name = "app-version")
    public String appVersion;
    @XmlElement
    public String hostname;
    @OneToMany(mappedBy = "environment")
    @XmlAnyElement
    public List<Notice> notices;

    private ServerEnvironment(String projectRoot, String environmentName, String appVersion, String hostname) {
        this.projectRoot = projectRoot.isEmpty() ? projectRoot : "";
        this.environmentName = environmentName.isEmpty() ? projectRoot : "";
        this.appVersion = appVersion.isEmpty() ? projectRoot : "";
        this.hostname = hostname.isEmpty() ? projectRoot : "";
    }

    public static ServerEnvironment getOrCreate(String projectRoot, String environmentName, String appVersion, String hostname) {
        projectRoot = projectRoot == null || projectRoot.isEmpty() ? projectRoot : "";
        environmentName = environmentName == null || environmentName.isEmpty() ? projectRoot : "";
        appVersion = appVersion == null || appVersion.isEmpty() ? projectRoot : "";
        hostname = hostname == null || hostname.isEmpty() ? projectRoot : "";
        String key = "ServerEnvironment_" + projectRoot + "_" + environmentName + "_" + appVersion + "_" + hostname;
        ServerEnvironment environment = Cache.get(key, ServerEnvironment.class);
        if (environment == null) {
            environment = ServerEnvironment.find("projectRoot = ? and environmentName = ? and appVersion = ? and hostname = ?", projectRoot, environmentName, appVersion, hostname).first();
            if (environment == null) {
                environment = new ServerEnvironment(projectRoot, environmentName, appVersion, hostname);
                environment.save();
            }
            Cache.set(key, environment, "1d");
        }
        return environment;
    }

    @Override
    public String toString() {
        if(this.environmentName.isEmpty()){
            return "";
        }
        return this.environmentName + "(" + this.appVersion + ") @" + this.hostname;
    }
}
