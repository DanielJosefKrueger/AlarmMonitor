package de.alarm_monitor.main;

import org.aeonbits.owner.Config;

public interface MainConfiguration extends Config {

    @Key("background")
    String path_background();

    @Key("folder_with_pdfs")
    String path_folder();

    @Key("tesseract_directory")
    String path_tesseract();

    @Key("working_directory")
    String path_working();

    @Key("path_ocr")
    String path_ocr();

    @Key("email_active")
    boolean isEmailActive();

    @Key("printing_active")
    boolean isPrintingActive();


    @Key("number_copies")
    int numerOfCopies();

}
