
package de.alarm_monitor.visual;

        import javax.swing.*;
        import javax.swing.border.LineBorder;
        import java.awt.*;
        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;


public class AlarmMonitorGridBag extends JFrame implements IDisplay {


    private final static String EINSATZNUMMER_DEFAULT = "";
    private final static String ALARMZEIT_DEFAULT = "";
    private final static String MITTEILER_DEFAULT = "Mitteiler:\n";
    private final static String SCHLAGWORT_DEFAULT = "";
    private final static String BEMERKUNG_DEFAULT = "Bemerkung:\n";
    private final static String ADRESSE_DEFAULT = "Adresse:\n";
    private final static String EINSATZMITTEL_DEFAULT = "Einsatzmittel:\n";
    private static Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 20);
    private JLabel einsatznummer;
    private JLabel alarmzeit;
    private JLabel name;
    private JLabel schlagWort;
    private JTextArea bemerkung;
    private JTextArea adresse;
    private JTextArea einsatzMittel;
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
        einsatznummer = new JLabel(EINSATZNUMMER_DEFAULT);
        //einsatznummer.setBackground(Color.black);
        einsatznummer.setFont(FONT);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        body.add(einsatznummer, c);


        alarmzeit = new JLabel(ALARMZEIT_DEFAULT);
        alarmzeit.setBackground(Color.white);
        alarmzeit.setOpaque(true);
        alarmzeit.setFont(FONT);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        body.add(alarmzeit, c);


        name = new JLabel(MITTEILER_DEFAULT);
        name.setBackground(Color.white);
        name.setOpaque(true);
        name.setFont(FONT);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        body.add(name, c);


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


        schlagWort = new JLabel(SCHLAGWORT_DEFAULT);
        schlagWort.setBackground(Color.white);
        schlagWort.setOpaque(true);
        schlagWort.setFont(FONT);
        schlagWort.setOpaque(true);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        body.add(schlagWort, c);
        c.gridwidth = 1;


        JPanel bemerkungPane = new JPanel();
        bemerkung = new JTextArea(BEMERKUNG_DEFAULT);
        bemerkung.setBackground(Color.white);
        bemerkung.setOpaque(true);
        bemerkung.setFont(FONT);
        c.weighty = 0.6;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        c.fill = GridBagConstraints.BOTH;
        bemerkungPane.setLayout(null);
        bemerkung.setBounds(0, 0, (int) (dim.getWidth()), (int) (dim.getHeight() * 0.3));
        bemerkungPane.add(bemerkung);
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
        einsatzMittel = new JTextArea(EINSATZMITTEL_DEFAULT);
        einsatzMittel.setBackground(Color.white);
        einsatzMittel.setOpaque(true);
        einsatzMittel.setFont(FONT);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 4;
        einsatzMittel.setBounds(0, 0, (int) (dim.getWidth() * 0.5), (int) (dim.getHeight() * 0.3));
        einsatzmittelPane.add(einsatzMittel);
        body.add(einsatzmittelPane, c);


        this.resetAlarm();
        this.setVisible(true);


    }


	/*
	public static void main(String[] args){
		new DisplayNew();

	}*/


    @Override
    public void changeName(String name) {
        this.name.setText(MITTEILER_DEFAULT + name);

    }


    @Override
    public void ChangeEinsatznummer(String einsatznummer) {
        this.einsatznummer.setText(EINSATZNUMMER_DEFAULT + einsatznummer);
    }


    @Override
    public void changeAlarmzeit(String alarmzeit) {
        this.alarmzeit.setText(ALARMZEIT_DEFAULT + alarmzeit);

    }


    @Override
    public void changeSchlagwort(String schlagwort) {
        this.schlagWort.setText(SCHLAGWORT_DEFAULT + schlagwort);
    }


    @Override
    public void changeAdresse(String adresse) {
        this.adresse.setText(ADRESSE_DEFAULT + adresse);
    }


    @Override
    public void changeEinsatzmittel(String einsatzmittel) {
        this.einsatzMittel.setText(EINSATZMITTEL_DEFAULT + einsatzmittel);

    }

    @Override
    public void changeBemerkung(String bemerkung) {
        this.bemerkung.setText(BEMERKUNG_DEFAULT + bemerkung);

    }


    @Override
    public void resetAlarm() {
        ChangeEinsatznummer("Einsatznummer:");
        changeAlarmzeit("Alarmzeit:");
        changeSchlagwort("Schlagwort:");
        changeBemerkung("");
        changeName("");
        changeAdresse("");
        changeEinsatzmittel("");
    }


}
