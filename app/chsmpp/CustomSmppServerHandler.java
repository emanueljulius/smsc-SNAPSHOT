package chsmpp;

import com.cloudhopper.smpp.SmppServerHandler;
import com.cloudhopper.smpp.SmppServerSession;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.pdu.BaseBind;
import com.cloudhopper.smpp.pdu.BaseBindResp;
import com.cloudhopper.smpp.type.SmppProcessingException;
import play.Logger;

public class CustomSmppServerHandler implements SmppServerHandler {

    @Override
    public void sessionBindRequested(Long sessionId, SmppSessionConfiguration sessionConfiguration, final BaseBind bindRequest) throws SmppProcessingException {
        // test name change of sessions
        // this name actually shows up as thread context....
        String clientUsername = sessionConfiguration.getSystemId();
        String clientPassword = sessionConfiguration.getPassword();
        String hostIp = sessionConfiguration.getHost();
            sessionConfiguration.setName("Application.SMPP." + sessionConfiguration.getSystemId());
//            throw new SmppProcessingException(SmppConstants.STATUS_BINDFAIL, "Invalid username or password!");
    }

    @Override
    public void sessionCreated(Long sessionId, SmppServerSession session, BaseBindResp preparedBindResponse) throws SmppProcessingException {
        Logger.info("Session created: {}", session);
        // need to do something it now (flag we're ready)
        SmppSessionConfiguration sessionConfiguration = session.getConfiguration();
        String clientUsername = sessionConfiguration.getSystemId();
        String clientPassword = sessionConfiguration.getPassword();
        String hostIp = sessionConfiguration.getHost();
        Logger.info(".......................CONNECTING HOST IP: "+hostIp);
        if(true) {
            session.serverReady(new CustomSmppSessionHandler(session));
        } else {
            session.unbind(5000);
            Logger.info("..........Invalid username or password or not allowed ip!");
        }
    }

    @Override
    public void sessionDestroyed(Long sessionId, SmppServerSession session) {
        Logger.info("Session destroyed: {}", session);
        // print out final stats
        if (session.hasCounters()) {
            Logger.info(" final session rx-submitSM: {}", session.getCounters().getRxSubmitSM());
        }

        // make sure it's really shutdown
           session.destroy();
    }

}

