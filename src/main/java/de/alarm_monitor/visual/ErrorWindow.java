package de.alarm_monitor.visual;

import javax.swing.*;
import java.awt.*;


public class ErrorWindow extends JFrame {


    private static Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 20);


    public ErrorWindow(String msg) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds((int) dim.getWidth() / 4, (int) dim.getHeight() / 4, (int) dim.getWidth() / 2, (int) dim.getHeight() / 2);
        this.setAlwaysOnTop(true);
        this.setLayout(new BorderLayout());
        JTextArea textField = new JTextArea(msg);
        textField.setFont(FONT);
        textField.setBackground(Color.red);
        this.setBackground(Color.red);
        this.add(textField, BorderLayout.CENTER);
        this.setVisible(true);
    }


}
