package de.alarm_monitor.visual;

import de.alarm_monitor.main.AlarmFax;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AlarmMonitorGridBag extends JFrame implements IDisplay {


    private final static String OPERATIONNUMBER_DEFAULT = "";
    private final static String ALARMTIME_DEFAULT = "";
    private final static String REPORTER_DEFAULT = "Mitteiler:\n";
    private final static String KEYWORD_DEFAULT = "";
    private final static String COMMENT_DEFAULT = "Bemerkung:\n";
    private final static String ADRESSE_DEFAULT = "Adresse:\n";
    private final static String OPERATIONRESSOURCES_DEFAULT = "Einsatzmittel:\n";
    private static Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 20);
    private JLabel oprationNumber;
    private JLabel alarmTime;
    private JLabel reporter;
    private JLabel KEYWORD;
    private JTextArea comment;
    private JTextArea adresse;
    private JTextArea operationRessources;
    private JButton resetButton;


    public AlarmMonitorGridBag() {
        super("Alarmmonitor");


        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        this.setSize(dim);
        //this.setContentPane(new BackGroundPane(MainAction.PATH_BACKGROUND));
        this.getContentPane().setLayout(null);
        this.setAlwaysOnTop(false);
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 0.2;

        JPanel body = new JPanel();
        this.add(body);
        body.setBounds(20, 20, (int) dim.getWidth() - 40, (int) dim.getHeight() - 40);


        body.setLayout(new GridBagLayout());
        body.setBackground(Color.white);
        body.setOpaque(true);

        LineBorder lBorder = new LineBorder(new Color(100, 100, 100));
        oprationNumber = new JLabel(OPERATIONNUMBER_DEFAULT);
        //oprationNumber.setBackground(Color.black);
        oprationNumber.setFont(FONT);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        body.add(oprationNumber, c);


        alarmTime = new JLabel(ALARMTIME_DEFAULT);
        alarmTime.setBackground(Color.white);
        alarmTime.setOpaque(true);
        alarmTime.setFont(FONT);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        body.add(alarmTime, c);

        reporter = new JLabel(REPORTER_DEFAULT);
        reporter.setBackground(Color.white);
        reporter.setOpaque(true);
        reporter.setFont(FONT);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        body.add(reporter, c);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setOpaque(false);
        resetButton = new JButton("Alarm zurücksetzen");
        resetButton.setFont(FONT);
        c.gridx = 1;
        c.gridy = 1;
        resetButton.setBounds(0, 0, (int) (dim.getWidth() * 0.2), (int) (dim.getHeight() * 0.1));
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                resetAlarm();
            }
        });

        buttonPanel.add(resetButton);
        body.add(buttonPanel, c);


        KEYWORD = new JLabel(KEYWORD_DEFAULT);
        KEYWORD.setBackground(Color.white);
        KEYWORD.setOpaque(true);
        KEYWORD.setFont(FONT);
        KEYWORD.setOpaque(true);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        body.add(KEYWORD, c);
        c.gridwidth = 1;


        JPanel bemerkungPane = new JPanel();
        comment = new JTextArea(COMMENT_DEFAULT);
        comment.setBackground(Color.white);
        comment.setOpaque(true);
        comment.setFont(FONT);
        c.weighty = 0.6;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        c.fill = GridBagConstraints.BOTH;
        bemerkungPane.setLayout(null);
        comment.setBounds(0, 0, (int) (dim.getWidth()), (int) (dim.getHeight() * 0.3));
        bemerkungPane.add(comment);
        body.add(bemerkungPane, c);
        c.gridwidth = 1;


        JPanel adressPane = new JPanel();
        adressPane.setLayout(null);
        adresse = new JTextArea(ADRESSE_DEFAULT);
        adresse.setBackground(Color.white);
        adresse.setOpaque(true);
        adresse.setFont(FONT);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 4;
        adresse.setBounds(0, 0, (int) (dim.getWidth() * 0.5), (int) (dim.getHeight() * 0.3));
        adressPane.add(adresse);
        adressPane.setBackground(Color.white);
        adressPane.setOpaque(true);
        body.add(adressPane, c);


        JPanel einsatzmittelPane = new JPanel();
        einsatzmittelPane.setLayout(null);
        operationRessources = new JTextArea(OPERATIONRESSOURCES_DEFAULT);
        operationRessources.setBackground(Color.white);
        operationRessources.setOpaque(true);
        operationRessources.setFont(FONT);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 4;
        operationRessources.setBounds(0, 0, (int) (dim.getWidth() * 0.5), (int) (dim.getHeight() * 0.3));
        einsatzmittelPane.add(operationRessources);
        body.add(einsatzmittelPane, c);


        this.resetAlarm();
        this.setUndecorated(true);
        getRootPane().setBackground(Color.white);
        getRootPane().setOpaque(true);

        getContentPane().setBackground(Color.white);

        // getRootPane().setBorder(BorderFactory.createMatteBorder(15, 15, 15, 15, Color.RED));
        this.setVisible(true);


    }


    public static void main(String... args) {
        new AlarmMonitorGridBag().activateAlarm();

    }


    private void refresh() {
        repaint();
    }

    @Override
    public void changeAlarmFax(AlarmFax alarmFax) {
        this.reporter.setText(REPORTER_DEFAULT + alarmFax.getReporter());
        this.oprationNumber.setText(OPERATIONNUMBER_DEFAULT + alarmFax.getOperationNumber());
        this.alarmTime.setText(ALARMTIME_DEFAULT + alarmFax.getAlarmTime());
        this.KEYWORD.setText(KEYWORD_DEFAULT + alarmFax.getKeyword());
        this.adresse.setText(ADRESSE_DEFAULT + alarmFax.getAddress());
        this.operationRessources.setText(OPERATIONRESSOURCES_DEFAULT + alarmFax.getOperationRessources());
        this.comment.setText(COMMENT_DEFAULT + alarmFax.getComment());
    }

    @Override
    public void changeReporter(String name) {
        this.reporter.setText(REPORTER_DEFAULT + name);

    }


    @Override
    public void changeOperationNumber(String operationNumber) {
        this.oprationNumber.setText(OPERATIONNUMBER_DEFAULT + operationNumber);
    }


    @Override
    public void changeAlarmTime(String alarmTime) {
        this.alarmTime.setText(ALARMTIME_DEFAULT + alarmTime);

    }


    @Override
    public void changeKeyWord(String keyWord) {

        this.KEYWORD.setText(KEYWORD_DEFAULT + keyWord);
    }


    @Override
    public void changeAddress(String adresse) {
        this.adresse.setText(ADRESSE_DEFAULT + adresse);
    }


    @Override
    public void changeOperationRessources(String operationRessources) {
        this.operationRessources.setText(OPERATIONRESSOURCES_DEFAULT + operationRessources);

    }

    @Override
    public void changeComment(String comment) {
        this.comment.setText(COMMENT_DEFAULT + comment);

    }


    @Override
    public void resetAlarm() {
        changeOperationNumber("Einsatznummer:");
        changeAlarmTime("Alarmzeit:");
        changeKeyWord("Schlagwort:");
        changeComment("");
        changeReporter("");
        changeAddress("");
        changeOperationRessources("");

    }

    @Override
    public void activateAlarm() {
        // getRootPane().setBorder(BorderFactory.createMatteBorder(15, 15, 15, 15, Color.RED));


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();

                while (System.currentTimeMillis() < start + 5 * 60 * 1000) {
                    //getRootPane().setBorder(BorderFactory.createMatteBorder(15, 15, 15, 15, Color.RED));
                    getContentPane().setBackground(Color.red);
                    refresh();
                    try {
                        Thread.sleep(500);


                        // getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE));
                        getContentPane().setBackground(Color.white);

                        refresh();
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


}
