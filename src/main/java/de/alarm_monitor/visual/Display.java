package de.alarm_monitor.visual;

import de.alarm_monitor.configuration.MainConfiguration;

import javax.swing.*;
import java.awt.*;


public class Display extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = -7874383036137263007L;


    private final JLabel alarmGrund;
    private final JLabel infos;
    private final MainConfiguration configuration;


    public Display(MainConfiguration mainConfiguration) {
        configuration = mainConfiguration;


        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(dim);
        //this.setContentPane(new BackGroundPane(configuration.path_background()));
        this.getContentPane().setLayout(null);
        //this.setLayout(null);
        this.setAlwaysOnTop(false);

        //this.getContentPane().setBackground(Color.blue);
        //this.setForeground(Color.blue);

        this.setUndecorated(true);


        alarmGrund = new JLabel("Grund:");
        alarmGrund.setBounds((int) (dim.getWidth() * 0.1), 0, (int) (dim.getWidth() * 0.5), (int) (dim.getHeight() * 0.25));
        this.add(alarmGrund);
        alarmGrund.setFont(new Font("serif", Font.BOLD, 30));
        alarmGrund.setForeground(Color.yellow);

        infos = new JLabel("Weitere Infos:");
        infos.setBounds((int) (dim.getWidth() * 0.1), (int) (dim.getHeight() * 0.25), (int) (dim.getWidth() * 0.5), (int) (dim.getHeight() * 0.25));
        this.add(infos);
        infos.setFont(new Font("serif", Font.BOLD, 30));
        infos.setForeground(Color.yellow);

        this.setVisible(true);
    }


    public void changeGrund(String grund) {

        this.alarmGrund.setText("Grund: " + grund);
        this.revalidate();
        //reSize();

    }

    public void changeInfo(String info) {
        this.infos.setText("Informationen: " + info);
        this.revalidate();
        //reSize();
    }


    private void reSize() {


        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(0, 0, (int) dim.getWidth() - 5, (int) dim.getHeight() - 5);
        this.setBounds(0, 0, (int) dim.getWidth(), (int) dim.getHeight());

    }

}

