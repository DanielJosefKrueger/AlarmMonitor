package de.alarm_monitor.validation;

import com.google.inject.Inject;
import com.google.inject.Provider;
import de.alarm_monitor.configuration.MainConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Validator {

    private final MainConfiguration mainConfiguration;

    @Inject
    Validator(Provider<MainConfiguration> provider) {

        mainConfiguration = provider.get();
    }


    /**
     * @return true: all configuration is valid, false otherwise
     */
    public List<ValidationResult> validateMainConfig() {

        List<ValidationResult> results = new ArrayList<>();

        results.addAll(testPdfFolder(mainConfiguration));


        return results;
    }


    public List<ValidationResult> testPdfFolder(MainConfiguration mainConfiguration) {
        List<ValidationResult> results = new ArrayList<>();
        String path = mainConfiguration.path_folder();
        try {
            File file = new File(path);
            if (!file.isDirectory()) {
                results.add(new ValidationResult(ValidationCode.critical, "In KOnfigurationsdatei ist der PDF-Ordner falsch konfioguriert: Kein Ordner"));
            }
        } catch (Exception e) {
            results.add(new ValidationResult(ValidationCode.critical, "In KOnfigurationsdatei ist der PDF-Ordner falsch konfioguriert, Ordner konnte nicht gefunden werden"));
        }
        return results;
    }


}
