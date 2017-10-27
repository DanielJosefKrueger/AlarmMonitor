package de.alarm_monitor.main;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Reloadable;



@Config.HotReload
@Config.Sources("file:${mainconfig}")
public interface MainConfiguration extends Reloadable, Config {

    @Key("pdf_ordner")
    String path_folder();

    @Key("tesseract_verzeichnis")
    String path_tesseract();

    @DefaultValue("false")
    @Key("einsatzmittel_filtern")
    Boolean should_filter_einsatzmittel();

    @DefaultValue("")
    @Key("filter_signal_wort")
    String filter_einsatzmittel();

    @Key("email_versenden_aktiv")
    boolean isEmailActive();

    @Key("drucken_aktiv")
    boolean isPrintingActive();

    @Key("anzahl_kopien")
    int numerOfCopies();

    @DefaultValue("1")
    @Key("monitor")
    int monitor();

    @Key("ocr_packet")
    @DefaultValue("eng")
    String getOcrPacket();

}
