package controllers;

import chsmpp.CHSMPPClient;
import com.cloudhopper.smpp.type.RecoverablePduException;
import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("SMSC"));
    }

    public static Result sendSMS(){
        CHSMPPClient chsmppClient = new CHSMPPClient();
        try {
            chsmppClient.sendSMS("SMS", "255783423272", "sms test");
        } catch (RecoverablePduException e) {
            Logger.error("...........error........." + e.getMessage());
        }
        return ok("....smpp client test.........");
    }

}
