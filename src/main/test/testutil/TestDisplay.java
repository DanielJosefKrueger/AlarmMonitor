package testutil;

import de.alarm_monitor.main.AlarmFax;
import de.alarm_monitor.visual.IDisplay;


public class TestDisplay implements IDisplay {

    private String reporter;
    private String operationNumber;
    private String alarmTime;
    private String keyWord;
    private String comment;
    private String address;
    private String operationResources;
    private AlarmFax alarmfax;


    @Override
    public void changeReporter(String name) {
        this.reporter = reporter;
    }

    @Override
    public void changeOperationNumber(String operationNumber) {
        this.operationNumber = operationNumber;
    }

    @Override
    public void changeAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    @Override
    public void changeKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    @Override
    public void changeComment(String comment) {
        this.comment = comment;
    }

    @Override
    public void changeAddress(String adresse) {
        this.address = adresse;
    }

    @Override
    public void changeOperationRessources(String operationRessources) {
        this.operationResources = operationResources;
    }

    @Override
    public void resetAlarm() {

    }

    @Override
    public void activateAlarm() {

    }

    @Override
    public void changeAlarmFax(AlarmFax alarmFax) {
        this.alarmfax = alarmFax;
    }

    public AlarmFax getAlarmfax() {
        return alarmfax;
    }
}
