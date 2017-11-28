package de.alarm_monitor.main;

import com.google.inject.AbstractModule;
import de.alarm_monitor.TextCorrecting.TextCorrecter;
import de.alarm_monitor.TextCorrecting.TextCorrecterImpl;
import de.alarm_monitor.parsing.OCRProcessor;
import de.alarm_monitor.parsing.OCRProcessorImpl1;
import de.alarm_monitor.parsing.OCRProzessorImpl2;

public class AlarmMonitorModule extends AbstractModule {
    @Override
    protected void configure() {

        bind(OCRProcessor.class).to(OCRProzessorImpl2.class);
        bind(TextCorrecter.class).to(TextCorrecterImpl.class);

    }
}
