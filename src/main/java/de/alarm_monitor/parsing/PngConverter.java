package de.alarm_monitor.parsing;

import de.alarm_monitor.configuration.MainConfiguration;
import de.alarm_monitor.main.SystemInformation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PngConverter {
    private static Logger logger = LogManager.getLogger(PngConverter.class);
    private final SystemInformation systemInformation;
    private final MainConfiguration mainConfiguration;

    @Inject
    public PngConverter(SystemInformation systemInformation, final Provider<MainConfiguration> provider) {
        this.systemInformation = systemInformation;
        this.mainConfiguration = provider.get();
    }

    public String convertToPng(File file) throws IOException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY.HH.mm.ss");
        String time = sdf.format(cal.getTime());

        String name = systemInformation.getWorkingFolder().getPath() + File.separatorChar + time.toString() + ".png";
        BufferedImage bim = convertToBufferedImage(file);
        ImageIOUtil.writeImage(bim, name, mainConfiguration.getDpiPng());
        return name;
    }


    @NotNull
    private BufferedImage concatTwoImages(BufferedImage bufferedImage1, BufferedImage bufferedImage2) {
        int heightTotal = bufferedImage1.getHeight() + bufferedImage2.getHeight();

        BufferedImage bim = new BufferedImage(bufferedImage1.getWidth() > bufferedImage2.getWidth() ? bufferedImage1.getWidth() : bufferedImage2.getWidth(),
                heightTotal, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bim.getGraphics();
        graphics.drawImage(bufferedImage1, 0, 0, null);
        graphics.drawImage(bufferedImage2, 0, bufferedImage1.getHeight(), null);
        return bim;
    }

    public BufferedImage convertToBufferedImage(File pdf) throws IOException {
        PDDocument document = PDDocument.load(pdf);
        BufferedImage bim;
        try {
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            if (document.getNumberOfPages() == 1) {
                bim = pdfRenderer.renderImageWithDPI(0, mainConfiguration.getDpiPng(), ImageType.RGB);
            } else {
                // we only handle 2 pages. This should be really enough
                BufferedImage bufferedImage1 = pdfRenderer.renderImageWithDPI(0, mainConfiguration.getDpiPng(), ImageType.RGB);
                BufferedImage bufferedImage2 = pdfRenderer.renderImageWithDPI(1, mainConfiguration.getDpiPng(), ImageType.RGB);
                bim = concatTwoImages(bufferedImage1, bufferedImage2);
            }
        } finally {
            document.close();
        }
        return bim;

    }

}
