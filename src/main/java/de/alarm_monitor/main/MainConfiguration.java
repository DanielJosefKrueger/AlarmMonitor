package de.alarm_monitor.main;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Reloadable;

import java.io.File;

@Config.HotReload
@Config.Sources("file:${mainconfig}")
public interface MainConfiguration extends Reloadable, Config {

    @Key("folder_with_pdfs")
    String path_folder();

    @Key("tesseract_directory")
    String path_tesseract();

    @Key("path_ocr")
    String path_ocr();

    @DefaultValue("false")
    @Key("filter_einsatzmittel")
    Boolean should_filter_einsatzmittel();

    @DefaultValue("")
    @Key("filter")
    String filter_einsatzmittel();

    @Key("email_active")
    boolean isEmailActive();

    @Key("printing_active")
    boolean isPrintingActive();

    @Key("number_copies")
    int numerOfCopies();

    @Key("ocr_packet")
    @DefaultValue("eng")
    String getOcrPacket();

}
