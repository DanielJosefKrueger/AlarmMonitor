package de.alarm_monitor.main;

public class AlarmFax {

    private String reporter = "Mitteiler nicht gesetzt";
    private String operatioNumber = "Einsatznummer nicht gesetzt";
    private String alarmTime = "Keine Alarmzeit gesetzt";
    private String keyword = "Keine Schlüsselwort gesetzt";
    private String comment = "Keine Bemerkung";
    private String address = "Keine Adresse gesetzt";
    private String operationRessources = "Keine Einsatzmittel gesetzt";
    private String link = "Kein Link vorhanden";

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

    public String getOperationNumber() {
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


    public String toEmailHtml() {
        StringBuilder emailBuilder = new StringBuilder();
        emailBuilder.append("<h1>Informationen zum Alarm</h1>");
        emailBuilder.append("<h2>Allgemein</h2>");
        String operationNumber = this.getOperationNumber();
        if (operationNumber.length() > 1) {
            emailBuilder.append("Einsatznummer: ").append(operationNumber);
        } else {
            emailBuilder.append("Die Einsatznummer konnte nicht ermittelt werden");
        }
        emailBuilder.append("<br>");

        String alarmTime = this.getAlarmTime();
        if (alarmTime.length() > 1) {
            emailBuilder.append("Alarmzeit: ").append(alarmTime);
        } else {
            emailBuilder.append("Leider konnte die Alarmzeit nicht ermittelt werden");
        }
        emailBuilder.append("<br>");

        String reporter = this.getReporter();
        if (reporter.length() > 1) {
            emailBuilder.append("Mitteiler: ").append(reporter);
        } else {
            emailBuilder.append("Leider konnte der Melder nicht ermittelt werden");
        }
        emailBuilder.append("<br>");


        String keyword = this.getKeyword();
        if (keyword.length() > 1) {
            emailBuilder.append("Schlagwort: ").append(keyword);
        } else {
            emailBuilder.append("Leider konnte das Schlagwort nicht ermittelt werden");
        }
        emailBuilder.append("<br><br>");

        String address = this.getAddress();
        emailBuilder.append("<h2>Adresse: </h2>");
        if (address.length() > 1) {
            emailBuilder.append(address);
        } else {
            emailBuilder.append("Die Adresse konnte nicht ermittelt werden");
        }
        emailBuilder.append("<br><br>");


        String operationRessources = this.getOperationRessources();
        emailBuilder.append("<h2>Einsatzmittel:</h2>");
        if (operationRessources.length() > 1) {
            emailBuilder.append(operationRessources);
        } else {
            emailBuilder.append("Die Einsatzmittel konnten nicht ermittelt werden");
        }
        emailBuilder.append("<br><br>");

        String comment = this.getComment();
        emailBuilder.append("<h2>Bemerkung:</h2>");
        if (comment.length() > 1) {
            emailBuilder.append(comment);
        } else {
            emailBuilder.append("Keine Bemerkung gefunden");
        }
        emailBuilder.append("<br><br>");

        emailBuilder.append("<h2>Link zum Routenplaner von Google:</h2>");
        emailBuilder.append("<a href=\"").append(this.getLink()).append("\">Zu Google Maps</a>");

        String email = emailBuilder.toString();
        return email.replaceAll("\n", "<br>");
    }

    public String toEmailPlainText() {
        StringBuilder email = new StringBuilder();
        email.append("Informationen zum Alarm\n");

        String operationNumber = this.getOperationNumber();
        if (operationNumber.length() > 1) {
            email.append("Einsatznummer: ").append(operationNumber);
        } else {
            email.append("Die Einsatznummer konnte nicht ermittelt werden");
        }
        email.append("\n");

        String alarmTime = this.getAlarmTime();
        if (alarmTime.length() > 1) {
            email.append("Alarmzeit: ").append(alarmTime);
        } else {
            email.append("Leider konnte die Alarmzeit nicht ermittelt werden");
        }
        email.append("\n");

        String reporter = this.getReporter();
        if (reporter.length() > 1) {
            email.append("Mitteiler: ").append(reporter);
        } else {
            email.append("Leider konnte der Melder nicht ermittelt werden");
        }
        email.append("\n");


        String keyword = this.getKeyword();
        if (keyword.length() > 1) {
            email.append("Schlagwort: ").append(keyword);
        } else {
            email.append("Leider konnte das Schlagwort nicht ermittelt werden");
        }
        email.append("\n\n");

        String address = this.getAddress();
        if (address.length() > 1) {
            email.append("Adresse: \n").append(address);
        } else {
            email.append("Die Adresse konnte nicht ermittelt werden");
        }
        email.append("\n\n");


        String operationRessources = this.getOperationRessources();
        if (operationRessources.length() > 1) {
            email.append("Einsatzmittel: \n").append(operationRessources);
        } else {
            email.append("Die Einsatzmittel konnten nicht ermittelt werden");
        }
        email.append("\n\n");

        String comment = this.getComment();
        if (comment.length() > 1) {
            email.append("Bemerkung:\n").append(comment);
        } else {
            email.append("Keine Bemerkung gefunden");
        }
        email.append("\n\n");

        email.append("Link zum Routenplaner von Google:\n");
        email.append(this.getLink()).append("\n");

        return email.toString();
    }


}
