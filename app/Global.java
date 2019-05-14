import chsmpp.CHSMPPClient;
import play.Application;
import play.GlobalSettings;
import play.Logger;

public class Global extends GlobalSettings {
    private final chsmpp.CHSMPPClient chsmppClient = new CHSMPPClient();

    @Override
    public void onStart(Application app) {
        Logger.info("...............Application has started.........................");
        try {
            chsmppClient.initialize();
        } catch (Exception e) {
            Logger.error("......exception error......"+e.getLocalizedMessage());
        }
    }

    @Override
    public void onStop(Application app) {
        Logger.info("...............Application shutdown.............................");
        chsmppClient.unbindAndClose();
    }

}