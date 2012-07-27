/**
 * ApiKey
 * 04.04.2012
 * @author Philipp Haussleiter
 *
 */
package models;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PreRemove;
import play.Logger;
import play.cache.Cache;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.libs.Codec;

@Entity
public class ApiKey extends Model {

    @Column(unique = true)
    @Required
    public String apiKey;

    public ApiKey() {
        this.apiKey = Codec.UUID();
    }

    @Override
    public String toString() {
        return this.apiKey;
    }

    public static ApiKey findByApiKey(String apikey) {
        String key = "ApiKey" + apikey;
        ApiKey apiKey = Cache.get(key, ApiKey.class);
        if (apiKey == null) {
            apiKey = ApiKey.find("apikey = ?", apikey).first();
            if (apiKey != null) {
                Logger.debug("found apikey with: " + apikey);
                Cache.set(key, apiKey, "30d");
            }
        }
        return apiKey;
    }

    @PreRemove
    public void preRemove() {
        List<Project> projects = Project.find("apiKey = ?", this).fetch();
        for (Project p : projects) {
            p.apiKey = null;
            p.save();
        }
        String key = "ApiKey_" + apiKey;
        Cache.delete(key);
    }
}
