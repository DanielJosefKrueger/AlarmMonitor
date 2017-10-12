package de.alarm_monitor.main;

import de.alarm_monitor.callback.NewPdfCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Observer extends Thread {


    private static Logger logger = LogManager.getLogger(Observer.class);
    private List<NewPdfCallback> callbacks = new ArrayList<>();
    private  long lastErrorMsg=0;

    private void initiateFirstRun(MainConfiguration configuration, List<String> foundedFiles, Path p) {

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(p)) {
            for (Path file : stream) {
                String s = file.getFileName().toString();

                if (!foundedFiles.contains(s)) {
                    foundedFiles.add(file.getFileName().toString());
                    logger.trace("Registered " + s + " at first Run ");
                }
            }
        } catch (IOException | DirectoryIteratorException x) {
            if (System.currentTimeMillis() - lastErrorMsg > 5000) {
                logger.error("", x);
                p = Paths.get(configuration.path_folder());
                lastErrorMsg = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void run() {
        MainConfiguration configuration = MainConfigurationLoader.getConfig();
        ArrayList<String> foundedFiles = new ArrayList<>();

        Path p = null;
        try {
            p = Paths.get(configuration.path_folder());
        } catch (Exception e) {
            logger.error("", e);
            System.exit(-1);
        }
        //first Run
        initiateFirstRun(configuration, foundedFiles,p);

        while (true) {

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(p)) {
                for (Path file : stream) {

                    String s = file.getFileName().toString();

                    if (!foundedFiles.contains(s)) {
                        foundedFiles.add(file.getFileName().toString());
                        logger.info("Alarm: new File " + s);
                        Thread.sleep(1000);
                        //wait for file to be completely stored
                        int counter = 0;
                        while (counter < 10 && !file.toFile().canRead()) {
                            Thread.sleep(100);
                            counter++;
                        }


                        //callbacks
                        for (NewPdfCallback callback : callbacks) {
                            ObserverCallbackHandler handler = new ObserverCallbackHandler(callback, file.toFile());
                            handler.start();
                        }
                    }

                }
                Thread.sleep(2000);

            } catch (IOException | DirectoryIteratorException | InterruptedException x) {
                if(System.currentTimeMillis() -lastErrorMsg > 5000 ){
                    logger.error("", x);
                    lastErrorMsg = System.currentTimeMillis();
                    if(!p.equals(configuration.path_folder())){
                        p = Paths.get(configuration.path_folder());
                        initiateFirstRun(configuration, foundedFiles,p);
                    }

                }
            }

        }
    }


    public void addCallback(NewPdfCallback callback) {
        this.callbacks.add(callback);
    }


}

