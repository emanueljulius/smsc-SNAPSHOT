package controllers;

import chsmpp.CHSMPPClient;
import com.cloudhopper.smpp.type.RecoverablePduException;
import com.cloudhopper.smpp.type.SmppChannelException;
import com.cloudhopper.smpp.type.SmppTimeoutException;
import com.cloudhopper.smpp.type.UnrecoverablePduException;
import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("SMSC"));
    }

    public static Result callBack(){
        CHSMPPClient chsmppClient = new CHSMPPClient();
        try {
            String msisdn = "255783423272";
            String message = "Agrimark sms test";
            chsmppClient.initialize();
            chsmppClient.sendSMS("SMS", msisdn, message);
            chsmppClient.unbindAndClose();
        } catch (RecoverablePduException e) {
            Logger.error("...........error........." + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SmppChannelException e) {
            e.printStackTrace();
        } catch (UnrecoverablePduException e) {
            e.printStackTrace();
        } catch (SmppTimeoutException e) {
            e.printStackTrace();
        }
        return ok("....smpp client test.........");
    }

}
