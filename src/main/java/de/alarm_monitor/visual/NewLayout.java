package de.alarm_monitor.visual;

import de.alarm_monitor.main.AlarmFax;
import de.alarm_monitor.util.LayoutCalculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewLayout extends JFrame implements IDisplay {

    private final static String OPERATIONNUMBER_DEFAULT = "EinsatzNummer: ";
    private final static String ALARMTIME_DEFAULT = "Alarm Zeit: ";
    private final static String REPORTER_DEFAULT = "Mitteiler: \n";
    private final static String KEYWORD_DEFAULT = "Schlagwort: ";
    private final static String COMMENT_DEFAULT = "Bemerkung: \n";
    private final static String ADRESSE_DEFAULT = "Adresse: \n";
    private final static String OPERATIONRESSOURCES_DEFAULT = "Einsatzmittel: \n";
    private static Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 20);


    private final JLabel sectionOperationNumber;
    private final JLabel sectionOperationTime;
    private final JLabel sectionKeyWord;
    private final JLabel sectionReporter;
    private final JTextArea sectionAddress;
    private final JTextArea sectionOperationRessources;
    private final JTextArea sectionComment;
    private final JButton resetButton;

    private boolean alarmActive;

    public NewLayout() {

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        this.getContentPane().setLayout(null);
        this.setLayout(null);
        this.setSize(dim);
        this.getContentPane().setBackground(Color.white);
        JPanel body = new JPanel();
        body.setBounds(20, 20, (int) dim.getWidth() - 40, (int) dim.getHeight() - 40);
        body.setLayout(null);
        body.setBackground(Color.white);
        body.setOpaque(true);
        this.add(body);

        LayoutCalculator calculator = new LayoutCalculator(body.getWidth(), body.getHeight(), 2, 10);


        sectionOperationNumber = new JLabel(OPERATIONNUMBER_DEFAULT);
        sectionOperationNumber.setBounds(calculator.getRectangleForPosition(0, 0, 1, 1));
        sectionOperationNumber.setFont(FONT);
        body.add(sectionOperationNumber);


        sectionOperationTime = new JLabel(ALARMTIME_DEFAULT);
        sectionOperationTime.setBounds(calculator.getRectangleForPosition(1, 0, 1, 1));
        sectionOperationTime.setFont(FONT);
        body.add(sectionOperationTime);


        sectionKeyWord = new JLabel(KEYWORD_DEFAULT);
        sectionKeyWord.setBounds(calculator.getRectangleForPosition(0, 1, 1, 1));
        sectionKeyWord.setFont(FONT);
        body.add(sectionKeyWord);


        sectionReporter = new JLabel(REPORTER_DEFAULT);
        sectionReporter.setBounds(calculator.getRectangleForPosition(1, 1, 1, 1));
        sectionReporter.setFont(FONT);
        body.add(sectionReporter);


        sectionAddress = new JTextArea(ADRESSE_DEFAULT);
        sectionAddress.setBounds(calculator.getRectangleForPosition(0, 2, 1, 2));
        sectionAddress.setFont(FONT);
        body.add(sectionAddress);


        sectionOperationRessources = new JTextArea(OPERATIONRESSOURCES_DEFAULT);
        sectionOperationRessources.setBounds(calculator.getRectangleForPosition(0, 4, 1, 3));
        sectionOperationRessources.setFont(FONT);
        body.add(sectionOperationRessources);


        resetButton = new JButton("Alarm zurücksetzen");
        resetButton.setFont(FONT);
        resetButton.setBounds(calculator.getRectangleForPosition(1, 9, 0.5, 0.5));
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                resetAlarm();
            }
        });
        body.add(resetButton);


        sectionComment = new JTextArea(COMMENT_DEFAULT);
        sectionComment.setBounds(calculator.getRectangleForPosition(0, 7, 2, 3));
        sectionComment.setFont(FONT);
        sectionComment.setLineWrap(true);
        body.add(sectionComment);


        this.setUndecorated(true);
        this.setAlwaysOnTop(false);
        this.setVisible(true);
    }


    public static void main(String... args) {

        new NewLayout().changeComment("hallo\nhallo\nhallo");
    }


    @Override
    public void changeReporter(String name) {
        sectionReporter.setText(REPORTER_DEFAULT + name);
    }

    @Override
    public void changeOperationNumber(String operationNumber) {
        sectionOperationNumber.setText(OPERATIONNUMBER_DEFAULT + operationNumber);
    }

    @Override
    public void changeAlarmTime(String alarmTime) {
        sectionOperationTime.setText(ALARMTIME_DEFAULT + " " + alarmTime);
    }

    @Override
    public void changeKeyWord(String keyWord) {
        sectionKeyWord.setText(KEYWORD_DEFAULT + " " + keyWord);
    }

    @Override
    public void changeComment(String comment) {
        sectionComment.setText(COMMENT_DEFAULT + "\n" + comment);
    }

    @Override
    public void changeAddress(String adresse) {
        sectionAddress.setText(ADRESSE_DEFAULT + "\n" + adresse);
    }

    @Override
    public void changeOperationRessources(String operationRessources) {
        sectionOperationRessources.setText(OPERATIONRESSOURCES_DEFAULT + "\n" + operationRessources);
    }

    @Override
    public void resetAlarm() {
        sectionReporter.setText(REPORTER_DEFAULT);
        sectionOperationNumber.setText(OPERATIONNUMBER_DEFAULT);
        sectionOperationTime.setText(ALARMTIME_DEFAULT);
        sectionKeyWord.setText(KEYWORD_DEFAULT);
        sectionComment.setText(COMMENT_DEFAULT);
        sectionAddress.setText(ADRESSE_DEFAULT);
        sectionOperationRessources.setText(OPERATIONRESSOURCES_DEFAULT);
        alarmActive = false;
        getContentPane().setBackground(Color.white);
    }

    @Override
    public void activateAlarm() {
        alarmActive = true;
        Runnable runnable = () -> {
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() < start + 5 * 60 * 1000) {
                if (!alarmActive) {
                    break;
                }
                getContentPane().setBackground(Color.red);
                refresh();
                try {
                    Thread.sleep(500);
                    getContentPane().setBackground(Color.white);
                    refresh();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void refresh() {
        repaint();
    }

    @Override
    public void changeAlarmFax(AlarmFax alarmFax) {
        changeReporter(alarmFax.getReporter());
        changeOperationNumber(alarmFax.getOperationNumber());
        changeAlarmTime(alarmFax.getAlarmTime());
        changeKeyWord(alarmFax.getKeyword());
        changeComment(alarmFax.getComment());
        changeAddress(alarmFax.getAddress());
        changeOperationRessources(alarmFax.getOperationRessources());
    }
}
