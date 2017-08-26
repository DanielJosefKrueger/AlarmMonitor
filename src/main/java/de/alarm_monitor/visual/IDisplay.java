package de.alarm_monitor.visual;

public interface IDisplay {


    public void changeName(String name);

    public void ChangeEinsatznummer(String einsatznummer);

    public void changeAlarmzeit(String alarmzeit);

    public void changeSchlagwort(String schlagwort);

    public void changeBemerkung(String bemerkung);

    public void changeAdresse(String adresse);

    public void changeEinsatzmittel(String einsatzmittel);

    public void resetAlarm();


}
