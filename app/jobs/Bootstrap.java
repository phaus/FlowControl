package jobs;


import models.Project;
import play.Logger;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

/**
 * Bootstrap
 * 09.04.2012
 * @author Philipp Haussleiter
 *
 */
@OnApplicationStart
public class Bootstrap extends Job {

    @Override
    public void doJob() {
        Logger.info("Bootstrap Job");
        if (play.Play.configuration.get("application.mode").equals("dev") && Project.count() == 0) {
            Logger.info("Loading fixtures");
            Fixtures.loadModels("data.yml");
        }
    }
}
