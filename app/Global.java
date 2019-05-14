import chsmpp.CHSMPPServer;
import play.Application;
import play.GlobalSettings;
import play.Logger;

public class Global extends GlobalSettings {
//    private final chsmpp.CHSMPPServer CHSMPPServer = new CHSMPPServer();

    @Override
    public void onStart(Application app) {
        Logger.info("...............Application has started.........................");
        try {
//            CHSMPPServer.execute();
        } catch (Exception e) {
            Logger.error("......exception error......"+e.getLocalizedMessage());
        }
    }

    @Override
    public void onStop(Application app) {
        Logger.info("...............Application shutdown.............................");
//        CHSMPPServer.stopExecution();
    }

}