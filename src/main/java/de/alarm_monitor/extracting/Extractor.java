package de.alarm_monitor.extracting;

import de.alarm_monitor.main.AlarmFax;

public interface Extractor {


    AlarmFax extractInformation(String recognizedText);

}
