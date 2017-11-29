package de.alarm_monitor.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {


    private static final Logger logger = LogManager.getLogger(FileUtil.class);

    public static String getLastLinesOfFile(int number, File file) {

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {

            List<String> list = new ArrayList<>();
            in.lines().forEach(list::add);


            int begin = list.size() - number > 0 ? list.size() - number : 0;
            List<String> sublist = list.subList(begin, list.size());
            StringBuilder sb = new StringBuilder();
            for (String s : sublist) {
                sb.append(s).append("\n");
            }


            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Fehler beim Erstellen des Inhaltes" + e.getMessage();

        }

    }
}
