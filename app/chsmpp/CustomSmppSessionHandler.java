package chsmpp;

import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.impl.DefaultSmppSessionHandler;
import com.cloudhopper.smpp.pdu.*;
import com.cloudhopper.smpp.type.*;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import models.MT;
import play.Logger;
import utils.beanUtil;

import java.lang.ref.WeakReference;
import java.util.Date;

public class CustomSmppSessionHandler extends DefaultSmppSessionHandler {

    private static final JsonNodeFactory NODE_FACTORY = JsonNodeFactory.instance;
    private final WeakReference<SmppSession> sessionRef;

    public CustomSmppSessionHandler(SmppSession session) {
        this.sessionRef = new WeakReference<>(session);
    }

    @Override
    public PduResponse firePduRequestReceived(PduRequest pduRequest) {
        SmppSession session = sessionRef.get();

        // mimic how long processing could take on a slower smsc
        try {
            //Thread.sleep(50);
        } catch (Exception e) {
        }

        PduResponse response = pduRequest.createResponse();
        Logger.info("..........PDU Data Received: {}", pduRequest);
        if (pduRequest.getCommandId() == SmppConstants.CMD_ID_SUBMIT_SM) {
            SubmitSm submitSm = (SubmitSm) pduRequest;
            int length = submitSm.getShortMessageLength();
            Address source_address = submitSm.getSourceAddress();
            String source = source_address.getAddress();
            Address dest_address = submitSm.getDestAddress();
            String destination = dest_address.getAddress();
            byte[] shortMessage = submitSm.getShortMessage();
            String message = new String(shortMessage);
            Logger.info("...................received smpp sms............................");
            Logger.info("[source: "+source + " destination: " + destination + " message: " + message+"]");
            Logger.info("....................received smpp sms...........................");
            
            MT mt = new MT();
            mt.msisdn = destination;
            mt.text = message;
            mt.sentDate = new Date();
            beanUtil.save(mt);

            SmppSessionConfiguration sessionConfiguration = session.getConfiguration();
            String hostIp = sessionConfiguration.getHost();
            String clientUsername = sessionConfiguration.getSystemId();
            String clientPassword = sessionConfiguration.getPassword();

            /*ObjectNode sendStatus = BulkSMSAPIService.sendSMSFromSMPPUsingClient(source, destination, message, smppBulkClient);
            Logger.info("............smpp sms sent response....." + sendStatus.toString());*/
        }
        return response;
    }
}

