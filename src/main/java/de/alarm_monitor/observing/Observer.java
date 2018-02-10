package de.alarm_monitor.observing;

import com.google.inject.Provider;
import de.alarm_monitor.callback.NewPdfCallback;
import de.alarm_monitor.configuration.InternalConfiguration;
import de.alarm_monitor.configuration.MainConfiguration;
import de.alarm_monitor.main.SystemInformation;
import de.alarm_monitor.security.AlertAdminReporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
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


    private static final Logger logger = LogManager.getLogger(Observer.class);
    private final List<NewPdfCallback> callbacks = new ArrayList<>();
    private final List<String> foundedFiles;
    private final SystemInformation systemInformation;
    private final MainConfiguration mainConfiguration;
    private final AlertAdminReporter alertAdminReporter;
    private long lastErrorMsg = 0;
    private long lastErroreMAIL = 0;
    private Path pathPdfFolder;

    @Inject
    public Observer(final SystemInformation systemInformation,
                    final Provider<MainConfiguration> provider,
                    final AlertAdminReporter alertAdminReporter) {
        this.alertAdminReporter = alertAdminReporter;
        this.systemInformation = systemInformation;
        this.mainConfiguration = provider.get();
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
        boolean initiated = initiateFirstRun(foundedFiles);


        while (!initiated) {
            try {
                logger.error("Fehler beim initialisieren der bereits vorhand3enen PFDs, versuche es in 5 Sekunden erneut");
                Thread.sleep(5000);
                initiated = initiateFirstRun(foundedFiles);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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
                if (System.currentTimeMillis() - lastErroreMAIL > InternalConfiguration.INTERVALL_BETWEEN_FOLDER_ERROR_email * 1000 * 60) {
                    alertAdminReporter.sendAlertToAdmin("Fehler beim Durchsuchen des Pdf Ordners", x);
                    lastErroreMAIL = System.currentTimeMillis();
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
        TimeUnit.MILLISECONDS.sleep(mainConfiguration.getDelayPdf());

        for (NewPdfCallback callback : callbacks) {
            ObserverCallbackHandler handler = new ObserverCallbackHandler(callback, file.toFile());
            handler.start();
        }
    }


    public void addCallback(NewPdfCallback callback) {
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

