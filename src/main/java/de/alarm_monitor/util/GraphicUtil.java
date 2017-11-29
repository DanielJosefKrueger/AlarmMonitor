package de.alarm_monitor.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class GraphicUtil {

    private static Logger logger = LogManager.getLogger(GraphicUtil.class);

    //from   https://stackoverflow.com/questions/4627553/show-jframe-in-a-specific-screen-in-dual-monitor-configuration
    public static void showOnScreen2(int screen, JFrame frame) {
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();

        if (screen > -1 && screen < gs.length) {
            gs[screen].setFullScreenWindow(frame);
        } else if (gs.length > 0) {
            gs[0].setFullScreenWindow(frame);

        } else {
            throw new RuntimeException("No Screens Found");
        }
    }

    public static void showOnScreen(int screen, JFrame frame) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();
        if (screen > -1 && screen < gd.length) {
            frame.setLocation(gd[screen].getDefaultConfiguration().getBounds().x, frame.getY());
        } else if (gd.length > 0) {
            frame.setLocation(gd[0].getDefaultConfiguration().getBounds().x, frame.getY());
        } else {
            throw new RuntimeException("No Screens Found");
        }
    }


}
