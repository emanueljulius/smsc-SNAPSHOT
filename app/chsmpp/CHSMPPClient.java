package chsmpp;

import com.cloudhopper.commons.charset.CharsetUtil;
import com.cloudhopper.commons.util.windowing.WindowFuture;
import com.cloudhopper.smpp.SmppBindType;
import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.impl.DefaultSmppClient;
import com.cloudhopper.smpp.impl.DefaultSmppSessionHandler;
import com.cloudhopper.smpp.pdu.*;
import com.cloudhopper.smpp.type.*;
import models.MO;
import models.MT;
import play.Logger;
import utils.beanUtil;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class CHSMPPClient {

    private static SmppSession session0 = null;
    private static DefaultSmppClient clientBootstrap;
    private static ScheduledThreadPoolExecutor submitSmnitorExecutor;
    private static ThreadPoolExecutor executor;

    public CHSMPPClient(){}

    public static void initialize() throws RecoverablePduException {
        //
        // setup 3 things required for any session we plan on creating
        //

        // for submitSmnitoring thread use, it's preferable to create your own instance
        // of an executor with Executors.newCachedThreadPool() and cast it to ThreadPoolExecutor
        // this permits exposing thinks like executor.getActiveCount() via JMX possible
        // no point renaming the threads in a factory since underlying Netty 
        // framework does not easily allow you to customize your thread names
        executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        // to enable automatic expiration of requests, a second scheduled executor
        // is required which is what a submitSmnitor task will be executed with - this
        // is probably a thread pool that can be shared with between all client bootstraps
        submitSmnitorExecutor = (ScheduledThreadPoolExecutor)Executors.newScheduledThreadPool(1, new ThreadFactory() {
            private final AtomicInteger sequence = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("SmppClientSessionWindowMonitorPool-" + sequence.getAndIncrement());
                return t;
            }
        });

        // a single instance of a client bootstrap can technically be shared
        // between any sessions that are created (a session can go to any different
        // number of SMSCs) - each session created under
        // a client bootstrap will use the executor and submitSmnitorExecutor set
        // in its constructor - just be *very* careful with the "expectedSessions"
        // value to make sure it matches the actual number of total concurrent
        // open sessions you plan on handling - the underlying netty library
        // used for NIO sockets essentially uses this value as the max number of
        // threads it will ever use, despite the "max pool size", etc. set on
        // the executor passed in here
        clientBootstrap = new DefaultSmppClient(Executors.newCachedThreadPool(), 1, submitSmnitorExecutor);

        //
        // setup configuration for a client session
        //
        DefaultSmppSessionHandler sessionHandler = new ClientSmppSessionHandler();

        SmppSessionConfiguration config0 = new SmppSessionConfiguration();
        config0.setWindowSize(1);
        config0.setName("agrimak");
        config0.setType(SmppBindType.TRANSCEIVER);
        config0.setHost("10.0.146.20");
        config0.setPort(6001);
        config0.setConnectTimeout(10000);
        config0.setSystemId("agrimak");
        config0.setPassword("1234@agr");
        config0.getLoggingOptions().setLogBytes(true);
        // to enable submitSmnitoring (request expiration)
        config0.setRequestExpiryTimeout(30000);
        config0.setWindowMonitorInterval(15000);
        config0.setCountersEnabled(true);

        //
        // create session, enquire link, submit an sms, close session
        //
        try {
            // create session a session by having the bootstrap connect a
            // socket, send the bind request, and wait for a bind response
            session0 = clientBootstrap.bind(config0, sessionHandler);

            // desubmitSm of an "asynchronous" enquireLink call - send it, get a future,
            // and then optionally choose to pick when we wait for it
            WindowFuture<Integer,PduRequest,PduResponse> future0 = session0.sendRequestPdu(new EnquireLink(), 10000, true);
            if (!future0.await()) {
                Logger.error("Failed to receive enquire_link_resp within specified time");
            } else if (future0.isSuccess()) {
                EnquireLinkResp enquireLinkResp2 = (EnquireLinkResp)future0.getResponse();
                Logger.info("enquire_link_resp #2: commandStatus [" + enquireLinkResp2.getCommandStatus() + "=" + enquireLinkResp2.getResultMessage() + "]");
            } else {
                Logger.error("Failed to properly receive enquire_link_resp: " + future0.getCause());
            }

            Logger.info("sendWindow.size: {}", session0.getSendWindow().getSize());
        } catch (SmppTimeoutException | SmppChannelException | UnrecoverablePduException | InterruptedException | RecoverablePduException e) {
            Logger.error("", e);
        }
    }

    public static SmppSession getSmppSession(){
        return session0;
    }

    public static void sendSMS(String senderID, String msisdn, String text) throws RecoverablePduException, InterruptedException, SmppChannelException, UnrecoverablePduException, SmppTimeoutException {
            byte[] textBytes = CharsetUtil.encode(text, CharsetUtil.CHARSET_GSM);

            SubmitSm submit0 = new SubmitSm();
            submit0.setCommandStatus(SmppConstants.CMD_ID_SUBMIT_SM);
            // add delivery receipt
            //submit0.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);

            submit0.setSourceAddress(new Address((byte)0x03, (byte)0x00, senderID));
            submit0.setDestAddress(new Address((byte)0x01, (byte)0x01, msisdn));
            submit0.setShortMessage(textBytes);

            SubmitSmResp submitResp = session0.submit(submit0, 10000);

            Logger.info(".....submitResp......"+submitResp.getResultMessage());
            Logger.info(".....message id......"+submitResp.getMessageId());

            MT mt = new MT();
            mt.msisdn = msisdn;
            mt.text = text;
            mt.sentDate = new Date();
            beanUtil.save(mt);
    }

    public static void unbindAndClose(){
        session0.unbind(5000);
        if (session0 != null) {
            Logger.info("Cleaning up session... (final counters)");
            if (session0.hasCounters()) {
                Logger.info("tx-enquireLink: {}", session0.getCounters().getTxEnquireLink());
                Logger.info("tx-submitSM: {}", session0.getCounters().getTxSubmitSM());
                Logger.info("tx-deliverSM: {}", session0.getCounters().getTxDeliverSM());
                Logger.info("tx-dataSM: {}", session0.getCounters().getTxDataSM());
                Logger.info("rx-enquireLink: {}", session0.getCounters().getRxEnquireLink());
                Logger.info("rx-submitSM: {}", session0.getCounters().getRxSubmitSM());
                Logger.info("rx-deliverSM: {}", session0.getCounters().getRxDeliverSM());
                Logger.info("rx-dataSM: {}", session0.getCounters().getRxDataSM());
            }

            session0.destroy();
            // alternatively, could call close(), get outstanding requests from
            // the sendWindow (if we wanted to retry them later), then call shutdown()
        }

        // this is required to not causing server to hang from non-daesubmitSmn threads
        // this also makes sure all open Channels are closed to I *think*
        Logger.info("Shutting down client bootstrap and executors...");
        clientBootstrap.destroy();
        executor.shutdownNow();
        submitSmnitorExecutor.shutdownNow();
        Logger.info("Done. Exiting");
    }

    /**
     * Could either implement SmppSessionHandler or only override select methods
     * by extending a DefaultSmppSessionHandler.
     */
    public static class ClientSmppSessionHandler extends DefaultSmppSessionHandler {

        public ClientSmppSessionHandler() {
            super();
        }

        @Override
        public void firePduRequestExpired(PduRequest pduRequest) {
            Logger.warn("PDU request expired: {}", pduRequest);
        }

        @Override
        public PduResponse firePduRequestReceived(PduRequest pduRequest) {
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

                try {
                    MO mo = new MO();
                    mo.msisdn = destination;
                    mo.text = message;
                    mo.receivedDate = new Date();
                    beanUtil.save(mo);
                    beanUtil.newSubscriber(destination);
                    String reply = "Code is being authenticated";
                    sendSMS("SMS", destination, reply);
                } catch (RecoverablePduException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (SmppChannelException e) {
                    e.printStackTrace();
                } catch (UnrecoverablePduException e) {
                    e.printStackTrace();
                } catch (SmppTimeoutException e) {
                    e.printStackTrace();
                }
            }

            return response;
        }

    }

}
