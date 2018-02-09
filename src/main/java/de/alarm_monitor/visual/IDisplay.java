package de.alarm_monitor.visual;

import de.alarm_monitor.main.AlarmFax;

public interface IDisplay {


    void changeReporter(String name);

    void changeOperationNumber(String operationNumber);

    void changeAlarmTime(String alarmTime);

    void changeKeyWord(String keyWord);

    void changeComment(String comment);

    void changeAddress(String adresse);

    void changeOperationRessources(String operationRessources);

    void resetAlarm();


    void activateAlarm();

    void changeAlarmFax(AlarmFax alarmFax);
}
