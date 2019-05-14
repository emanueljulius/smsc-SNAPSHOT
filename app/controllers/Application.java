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

    public static Result callBack(String msisdn, String message){
        CHSMPPClient chsmppClient = new CHSMPPClient();
        try {
            chsmppClient.execute("SMS", msisdn, message);
        } catch (RecoverablePduException e) {
            Logger.error("...........error........." + e.getMessage());
        }
        return ok("....smpp client test.........");
    }

}
