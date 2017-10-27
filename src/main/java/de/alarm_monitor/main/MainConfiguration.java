package de.alarm_monitor.main;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Reloadable;



@Config.HotReload
@Config.Sources("file:${mainconfig}")
public interface MainConfiguration extends Reloadable, Config {

    @Key("pdf_ordners")
    String path_folder();

    @Key("tesseract_verzeichnis")
    String path_tesseract();

    @Key("path_ocr")
    String path_ocr();

    @DefaultValue("false")
    @Key("einsatzmittel_filtern")
    Boolean should_filter_einsatzmittel();

    @DefaultValue("")
    @Key("filter_signal_wort")
    String filter_einsatzmittel();

    @Key("email_versenden_aktiv")
    boolean isEmailActive();

    @Key("drucken_active")
    boolean isPrintingActive();

    @Key("anzahl_kopien")
    int numerOfCopies();

    @Key("ocr_packet")
    @DefaultValue("eng")
    String getOcrPacket();

}
