package de.alarm_monitor.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import de.alarm_monitor.configuration.MainConfiguration;
import de.alarm_monitor.processing.FaxProzessorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.TestOnly;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;


public class AddressFinder {


    private final static String api_key = "AIzaSyAx-W1Y0Mv3l0liFCNZhavr0-vPKwU0Scc";
    private static final Logger logger = LogManager.getLogger(FaxProzessorImpl.class);

    private final MainConfiguration mainConfiguration;

    @Inject
    AddressFinder(Provider<MainConfiguration> provider) {
        this.mainConfiguration = provider.get();
    }


    public static LatLng getCordsAdress(String address) throws InterruptedException, ApiException, IOException {

        StringBuilder adressbuilder = new StringBuilder();
        Arrays.stream(address.split("\n"))
                .filter(e -> e.startsWith("Str") || e.startsWith("Ort"))
                .forEach(e -> adressbuilder.append(e.replaceAll(".tra.e", "")
                        .replaceAll("Ort", "")
                        .replaceAll("Haus-Nr", "")
                        .replaceAll("\\.", "")
                        .replaceAll("=", "")
                        .replaceAll(":", "")
                ));

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(api_key)
                .build();

        logger.trace("Fetching the kords from Google");
        logger.trace("Fetch-String is:" + adressbuilder);
        GeocodingResult[] results = GeocodingApi.geocode(context, adressbuilder.toString()).awaitIgnoreError();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        logger.trace("Received following coordinates from Google" + gson.toJson(results[0].geometry.location));
        return results[0].geometry.location;

    }

    //48.39979700000001, 12.7468121
    //https://www.google.de/maps/dir/Freiwillige+Feuerwehr+Markt+Gangkofen,+Jahnstraße,+Gangkofen/48.45397,12.54189
    //https://www.google.de/maps/place/48.39979700000001, 12.7468121

    @TestOnly
    public static String getHtmlFromUrl(String url_string) {

        URL url = null;
        try {
            url = new URL(url_string);
        } catch (MalformedURLException e) {
            logger.warn("Error on url {} ", url_string, e);
        }

        int ptr;
        StringBuffer buffer = new StringBuffer();
        try (InputStream is = url.openStream();) {
            while ((ptr = is.read()) != -1) {
                buffer.append((char) ptr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    @TestOnly
    public static String test_html(String url) {
        getHtmlFromUrl(url);
        return "";
    }

    private String createLinkFromCoordinates(LatLng kords) {
        String begin = mainConfiguration.getRoutingLinkBegin();

        return begin + kords.lat + "," + kords.lng;
    }

    public String createLink(String address) {
        try {
            LatLng kords = getCordsAdress(address);
            return createLinkFromCoordinates(kords);


        } catch (Exception e) {
            if (address.lastIndexOf(" ") > -1) {
                logger.info("Couldnt get link from google, retry");
                return createLink(address.substring(0, address.lastIndexOf(" ")));
            } else {
                return "Leider konnte kein Link geparst werden";
            }


        }


    }


    /*public static void printPdfFromHtml(String buffer){
        try{
            ITextRenderer renderer = new ITextRenderer();

            // if you have html source in hand, use it to generate document object
            renderer.setDocumentFromString( buffer );
            renderer.layout();

            String fileNameWithPath = "PDF-FromHtmlString.pdf";
            FileOutputStream fos = new FileOutputStream( fileNameWithPath );
            renderer.createPDF( fos );
            fos.close();

            System.out.println( "File 2: '" + fileNameWithPath + "' created." );


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }*/


}
