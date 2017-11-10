package de.alarm_monitor.parsing;


import de.alarm_monitor.main.InternalConfiguration;
import de.alarm_monitor.main.MainConfigurationLoader;
import de.alarm_monitor.main.SystemInformationenImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PngConverter {

    private static Logger logger = LogManager.getLogger(PngConverter.class);

    public static String convertToPng(File file) throws IOException {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM YYYY HH mm ss");
        String time = sdf.format(cal.getTime());

        String name = SystemInformationenImpl.get().getWorkingFolder().getPath()+ File.separatorChar + time.toString() + ".png";
        PDDocument document = PDDocument.load(file);
        try {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, MainConfigurationLoader.getConfig().getDpiPng(), ImageType.RGB);
                ImageIOUtil.writeImage(bim, name, MainConfigurationLoader.getConfig().getDpiPng());
            }
            return name;
        } finally {
            document.close();
        }


    }
}
