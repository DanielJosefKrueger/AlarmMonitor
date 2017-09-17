package de.alarm_monitor.main;

import de.alarm_monitor.callback.NewPdfCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Observer extends Thread {


    private static Logger log = LoggerFactory.getLogger(Observer.class);
    private List<NewPdfCallback> callbacks = new ArrayList<>();


    @Override
    public void run() {
        MainConfiguration configuration = MainConfigurationLoader.getConfig();
        ArrayList<String> foundedFiles = new ArrayList<>();

        Path p = null;
        try {
            p = Paths.get(configuration.path_folder());
        } catch (Exception e) {
            log.error("", e);
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
            log.error("", x);
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
                            callback.onNewPdfFile(file.toFile());
                        }
                    }

                }
                Thread.sleep(10);

            } catch (IOException | DirectoryIteratorException | InterruptedException x) {
                // IOException can never be thrown by the iteration.
                // In this snippet, it can only be thrown by newDirectoryStream.

                log.error("", x);
            }

        }
    }


    public void addCallback(NewPdfCallback callback) {
        this.callbacks.add(callback);
    }


}

