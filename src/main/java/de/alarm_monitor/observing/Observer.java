package de.alarm_monitor.observing;

import de.alarm_monitor.callback.NewPdfCallback;
import de.alarm_monitor.configuration.InternalConfiguration;
import de.alarm_monitor.configuration.MainConfiguration;
import de.alarm_monitor.configuration.MainConfigurationLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Observer extends Thread {


    private static Logger logger = LogManager.getLogger(Observer.class);
    private List<NewPdfCallback> callbacks = new ArrayList<>();
    private long lastErrorMsg = 0;
    private MainConfiguration mainConfiguration;
    private Path pathPdfFolder;
    private List<String> foundedFiles;

    public Observer() {
        mainConfiguration = MainConfigurationLoader.getConfig();
        pathPdfFolder = new File(mainConfiguration.path_folder()).toPath();
        foundedFiles = new ArrayList<>();
    }


    private boolean initiateFirstRun(List<String> foundedFiles) {

        try {
            for (Path file : getListOfFiles()) {
                String s = file.getFileName().toString();
                if (!foundedFiles.contains(s)) {
                    foundedFiles.add(file.getFileName().toString());
                    logger.trace("Registered {} at first Run ", s);
                }
            }
        } catch (IOException | DirectoryIteratorException x) {
            if (System.currentTimeMillis() - lastErrorMsg > InternalConfiguration.INTERVALL_BETWEEN_FOLDER_ERROR_MESSAGES) {
                logger.error("", x);
                lastErrorMsg = System.currentTimeMillis();
            }
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        ArrayList<String> foundedFiles = new ArrayList<>();
        logger.trace("PDF Folder is set to: {}", mainConfiguration.path_folder());
        //first Run
        initiateFirstRun(foundedFiles);

        while (true) {
            try {

                for (Path file : getListOfFiles()) {
                    if (!foundedFiles.contains(file.getFileName().toString())) {
                        foundedFiles.add(file.getFileName().toString());
                        triggerCallbacks(file);
                    }
                }
                Thread.sleep(InternalConfiguration.INTERVAL_PDF_FOLDER_INVESTIGATION);

            } catch (IOException | DirectoryIteratorException | InterruptedException x) {
                if (System.currentTimeMillis() - lastErrorMsg > InternalConfiguration.INTERVALL_BETWEEN_FOLDER_ERROR_MESSAGES) {
                    logger.error("", x);
                    lastErrorMsg = System.currentTimeMillis();
                    testNewFolderConfigured();
                }
            }
        }
    }

    private void testNewFolderConfigured() {
        if (!pathPdfFolder.equals(new File(mainConfiguration.path_folder()).toPath())) {
            logger.trace("pdf folder Configurationo: changed from {} to  {}", pathPdfFolder.getFileName(), mainConfiguration.path_folder());
            pathPdfFolder = new File(mainConfiguration.path_folder()).toPath();

            initiateFirstRun(foundedFiles);
        }
    }

    private void triggerCallbacks(Path file) throws InterruptedException {

        logger.info("Neue Datei wurde gefunden " + file.getFileName());
        TimeUnit.MILLISECONDS.sleep(MainConfigurationLoader.getConfig().getDelayPdf());

        for (NewPdfCallback callback : callbacks) {
            ObserverCallbackHandler handler = new ObserverCallbackHandler(callback, file.toFile());
            handler.start();
        }
    }


    public void  addCallback(NewPdfCallback callback) {
        this.callbacks.add(callback);
    }


    private List<Path> getListOfFiles() throws IOException {
        List<Path> list = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(pathPdfFolder)) {
            for (Path path : stream) {
                list.add(path);
            }
        }
        return list;
    }
}

