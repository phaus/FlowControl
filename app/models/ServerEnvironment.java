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
import play.Logger;
import play.cache.Cache;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.libs.Codec;

@Entity
public class ServerEnvironment extends Model {

    public String projectRoot;
    @Required
    public String environmentName;
    public String appVersion;
    public String hostname;
    @OneToMany(mappedBy = "environment")
    public List<Notice> notices;

    private ServerEnvironment(String projectRoot, String environmentName, String appVersion, String hostname) {
        this.projectRoot = projectRoot;
        this.environmentName = environmentName;
        this.appVersion = appVersion;
        this.hostname = hostname;
    }

    public static ServerEnvironment getOrCreate(String projectRoot, String environmentName, String appVersion, String hostname) {
        String key = "SE_" + Codec.hexSHA1(projectRoot + "_" + environmentName + "_" + appVersion + "_" + hostname);
        ServerEnvironment environment = Cache.get(key, ServerEnvironment.class);
        if (environment == null) {
            environment = ServerEnvironment.find("projectRoot = ? and environmentName = ? and appVersion = ? and hostname = ?", projectRoot, environmentName, appVersion, hostname).first();
            Logger.info("found from db " + environment);
            if (environment == null) {
                environment = new ServerEnvironment(projectRoot, environmentName, appVersion, hostname);
                environment.save();
                Cache.set(key, environment, "1d");
            }
        }
        return environment;
    }

    public static ServerEnvironment getOrCreate(generated.ServerEnvironment se) {
        return getOrCreate(se.getProjectRoot(),
                se.getEnvironmentName(),
                se.getAppVersion(),
                se.getHostname());
    }

    @Override
    public String toString() {
        if (this.environmentName.isEmpty()) {
            return "";
        }
        return this.environmentName + "(" + this.appVersion + ") @" + this.hostname + "/" + projectRoot;
    }
}
