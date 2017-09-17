package de.alarm_monitor.ocr;


import de.alarm_monitor.main.MainConfigurationLoader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PNGParser {

    private static Logger log = LoggerFactory.getLogger(PNGParser.class);

    public static String parsePdfToPng(File file) throws IOException {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM YYYY HH mm ss");
        String time = sdf.format(cal.getTime());

        String name = MainConfigurationLoader.getConfig().path_working() + time.toString() + ".png";

        PDDocument document = PDDocument.load(file);
        PDFRenderer pdfRenderer = new PDFRenderer(document);

        for (int page = 0; page < document.getNumberOfPages(); ++page) {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
            ImageIOUtil.writeImage(bim, name, 300);
        }
        document.close();
        return name;
    }

}
