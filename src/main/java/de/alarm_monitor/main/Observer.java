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
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(p)) {
            for (Path file : stream) {
                String s = file.getFileName().toString();

                if (!foundedFiles.contains(s)) {
                    foundedFiles.add(file.getFileName().toString());
                    System.out.println("Registered " + s + " at first Run ");
                }
            }
        } catch (IOException | DirectoryIteratorException x) {
            // IOException can never be thrown by the iteration.
            // In this snippet, it can only be thrown by newDirectoryStream.
            logger.error("", x);
        }


        //noinspection InfiniteLoopStatement
        while (true) {

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(p)) {
                for (Path file : stream) {

                    String s = file.getFileName().toString();

                    if (!foundedFiles.contains(s)) {
                        foundedFiles.add(file.getFileName().toString());
                        System.out.println("Alarm: new File " + s);
                        //wait for file to be completely stored
                        int counter = 0;
                        while (counter < 10 && !file.toFile().canRead()) {
                            Thread.sleep(100);
                            System.out.println(counter);
                            counter++;
                        }


                        //callbacks
                        for (NewPdfCallback callback : callbacks) {
                            ObserverCallbackHandler handler = new ObserverCallbackHandler(callback, file.toFile());
                            handler.start();
                        }
                    }

                }
                Thread.sleep(10);

            } catch (IOException | DirectoryIteratorException | InterruptedException x) {
                // IOException can never be thrown by the iteration.
                // In this snippet, it can only be thrown by newDirectoryStream.

                logger.error("", x);
            }

        }
    }


    public void addCallback(NewPdfCallback callback) {
        this.callbacks.add(callback);
    }


}

