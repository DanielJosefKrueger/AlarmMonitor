package integrationtests;

public class TestConstants {


    public static final String opertionNumber = "Test 1234";
    public static final String LINK = "www.beispiellink.de";

    public static final String alarmTime = "18:30Uhr";
    public static final String operationResources = "Einsatzmittelname: 2.2.4 TEST FF TESTFEUERWEHR\n" +
            "gef. Geräte : Pressluftatmer\n" +
            "Einsatzmittelname: 2.2.4 TEST FF TESTFEUERWEHR\n" +
            "gef. Geräte : testgerät\n" +
            "Einsatzmittelname: 2.2.4 TEST FF TESTFEUERWEHR\n" +
            "gef. Geräte : \n" +
            "Einsatzmittelname: 2.2.4 TEST FF TESTFEUERWEHR\n" +
            "gef. Geräte :";
    public static final String reporter = "";
    public static final String keyword = "";
    public static final String address = "Straße : Beispielstraße    Haus-Nr.: 12\n" +
            "Abschnitt: BeispielAbschnitt \n" +
            "Ort: 12345 Musterort";
    public static final String comment = "commentline1\ncommentline2";


    public static final String testForPdf = "Einsatznummer: " + opertionNumber + " Alarmzeit: " + alarmTime + "\n" +
            "Mitteiler" + reporter + "\n" +
            "EINSATZORT\n" +
            address + "\n" +
            "EINSATZGRUND \n" +
            "Schlagw.: " + keyword + "\n" +
            "EINSATZMITTEL\n" +
            operationResources + "\n" +
            "BEMERKUNG\n" +
            comment;

}
