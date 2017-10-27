package de.alarm_monitor.util;

import de.alarm_monitor.main.FaxProzessorImpl;
import de.alarm_monitor.main.Start;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

@Singleton
public class AlarmResetter {

    private static final Logger logger = LogManager.getLogger(AlarmResetter.class);

    private long resetTime = Long.MAX_VALUE;


    @Inject
    public AlarmResetter(){

    }


    @PostConstruct
    public void  init(){
        startController();
    }



    public void resetAlarm(long delay){
        logger.info("Setting delay");
        resetTime =System.currentTimeMillis() + delay;
    }


    private void  startController(){

        Runnable controller = new Runnable() {
            @Override
            public void run() {
                while(true){


                    if(resetTime < System.currentTimeMillis()){
                           Start.getDisplay().resetAlarm();
                           logger.info("resetting Display");
                           resetTime = Long.MAX_VALUE;
                    }


                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread controllerThread = new Thread(controller);
        controllerThread.start();

    }






}
