package de.alarm_monitor.visual;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class BackGroundPane extends JPanel {

    private final static Logger log = LoggerFactory.getLogger(BackGroundPane.class);
    /**
     *
     */
    private static final long serialVersionUID = 3360713107803149100L;
    Image img = null;


    public BackGroundPane(String pathBackground) {

        if (pathBackground != null) {
            MediaTracker mt = new MediaTracker(this);
            img = Toolkit.getDefaultToolkit().getImage(pathBackground);
            mt.addImage(img, 0);
            try {
                mt.waitForAll();
            } catch (InterruptedException e) {
                log.error("", e);
                e.printStackTrace();
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}



