package de.alarm_monitor.main;

import de.alarm_monitor.callback.NewPdfCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Observer extends Thread {


    private static Logger logger = LogManager.getLogger(Observer.class);
    private List<NewPdfCallback> callbacks = new ArrayList<>();
    private  long lastErrorMsg=0;
    private Path folder;




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
            if (System.currentTimeMillis() - lastErrorMsg > InternalConfiguration.INTERVALL_BETWEEN_FOLDER_ERROR_MESSAGES) {
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
        logger.trace("PDF Folder is set to: {}", configuration.path_folder());

        try {
            folder = new File(configuration.path_folder()).toPath();
        } catch (Exception e) {
            logger.error("", e);
            System.exit(-1);
        }
        //first Run
        initiateFirstRun(configuration, foundedFiles,folder);

        while (true) {

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder)) {
                for (Path file : stream) {

                    String s = file.getFileName().toString();

                    if (!foundedFiles.contains(s)) {
                        foundedFiles.add(file.getFileName().toString());
                        logger.info("Alarm: new File " + s);
                        Thread.sleep(InternalConfiguration.DELAY_AFTER_FOUND_PDF);


                        //callbacks
                        for (NewPdfCallback callback : callbacks) {
                            ObserverCallbackHandler handler = new ObserverCallbackHandler(callback, file.toFile());
                            handler.start();
                        }
                    }

                }
                Thread.sleep(InternalConfiguration.INTERVAL_PDF_FOLDER_INVESTIGATION);

            } catch (IOException | DirectoryIteratorException | InterruptedException x) {
                if(System.currentTimeMillis() -lastErrorMsg > InternalConfiguration.INTERVALL_BETWEEN_FOLDER_ERROR_MESSAGES ){
                    logger.error("", x);
                    lastErrorMsg = System.currentTimeMillis();
                    if(!folder.equals(new File(configuration.path_folder()).toPath())){
                        logger.trace("pdf folder Configurationo: changedfrom {} to  {}",folder.getFileName(), configuration.path_folder());
                        folder = new File(configuration.path_folder()).toPath();;
                        initiateFirstRun(configuration, foundedFiles, folder);
                    }

                }
            }

        }
    }


    public void addCallback(NewPdfCallback callback) {
        this.callbacks.add(callback);
    }


}

