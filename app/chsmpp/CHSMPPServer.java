package chsmpp;

import com.cloudhopper.smpp.SmppServerConfiguration;
import com.cloudhopper.smpp.impl.DefaultSmppServer;
import play.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class CHSMPPServer {

    private DefaultSmppServer smppServer;

    public CHSMPPServer(){}

    public void execute() throws Exception {
        // setup 3 things required for a server
        // for monitoring thread use, it's preferable to create your own instance
        // of an executor and cast it to a ThreadPoolExecutor from Executors.newCachedThreadPool()
        // this permits exposing things like executor.getActiveCount() via JMX possible
        // no point renaming the threads in a factory since underlying Netty
        // framework does not easily allow you to customize your thread names
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        // to enable automatic expiration of requests, a second scheduled executor
        // is required which is what a monitor task will be executed with - this
        // is probably a thread pool that can be shared with between all client bootstraps
        ScheduledThreadPoolExecutor monitorExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1, new ThreadFactory() {
            private final AtomicInteger sequence = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("SmppServerSessionWindowMonitorPool-" + sequence.getAndIncrement());
                return t;
            }
        });

        int SERVER_PORT = 9010;
        int SERVER_MAX_CONNECTION_SIZE = 10;
        int SERVER_REQUEST_EXPIRY_TIMEOUT = 30000;
        int SERVER_WINDOW_MONITOR_INTERVAL = 15000;
        int SERVER_WINDOW_SIZE = 5;

        // create a server configuration
        SmppServerConfiguration configuration = new SmppServerConfiguration();
        configuration.setPort(SERVER_PORT);
        configuration.setMaxConnectionSize(SERVER_MAX_CONNECTION_SIZE);
        configuration.setNonBlockingSocketsEnabled(true);
        configuration.setDefaultRequestExpiryTimeout(SERVER_REQUEST_EXPIRY_TIMEOUT);
        configuration.setDefaultWindowMonitorInterval(SERVER_WINDOW_MONITOR_INTERVAL);
        configuration.setDefaultWindowSize(SERVER_WINDOW_SIZE);
        configuration.setDefaultWindowWaitTimeout(configuration.getDefaultRequestExpiryTimeout());
        configuration.setDefaultSessionCountersEnabled(true);
        configuration.setJmxEnabled(true);

        // create a server, start it up
        if(smppServer == null) {
            smppServer = new DefaultSmppServer(configuration, new CustomSmppServerHandler(), executor, monitorExecutor);
        }
        Logger.info("......Starting SMPP server...");
        smppServer.start();
        Logger.info("......SMPP server started at port: "+SERVER_PORT);
    }

    public void stopExecution(){
        Logger.info("......Stopping SMPP server...");
        smppServer.stop();
        smppServer.destroy();
        Logger.info("......SMPP server stopped");
        Logger.info("Server counters: {}", smppServer.getCounters());
    }

}
