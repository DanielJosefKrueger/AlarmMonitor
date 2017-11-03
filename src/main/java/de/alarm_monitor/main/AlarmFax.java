package de.alarm_monitor.main;

public class AlarmFax {

    private String reporter ="Mitteiler nicht gesetzt";
    private String operatioNumber ="Einsatznummer nicht gesetzt";
    private String alarmTime ="Keine Alarmzeit gesetzt";
    private String keyword ="Keine Schlüsselwort gesetzt";
    private String comment ="Keine Bemerkung";
    private String address ="Keine Adresse gesetzt";
    private String operationRessources ="Keine Einsatzmittel gesetzt";
    private String link="Kein Link vorhanden";

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getOperatioNumber() {
        return operatioNumber;
    }

    public void setOperatioNumber(String operatioNumber) {
        this.operatioNumber = operatioNumber;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOperationRessources() {
        return operationRessources;
    }

    public void setOperationRessources(String operationRessources) {
        this.operationRessources = operationRessources;
    }
}
