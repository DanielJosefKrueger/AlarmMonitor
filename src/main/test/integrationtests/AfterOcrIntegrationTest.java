package integrationtests;

import com.google.inject.Provider;
import de.alarm_monitor.configuration.MainConfiguration;
import de.alarm_monitor.correcting.TextCorrecter;
import de.alarm_monitor.correcting.TextCorrecterImpl;
import de.alarm_monitor.email.EMailList;
import de.alarm_monitor.extracting.Extractor;
import de.alarm_monitor.extracting.ExtractorImpl;
import de.alarm_monitor.main.Start;
import de.alarm_monitor.main.SystemInformation;
import de.alarm_monitor.parsing.OCRProcessor;
import de.alarm_monitor.processing.FaxProzessorImpl;
import de.alarm_monitor.security.AlertAdminReporter;
import de.alarm_monitor.util.AddressFinder;
import de.alarm_monitor.util.AlarmResetter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import testutil.TestDisplay;

import java.io.File;

import static integrationtests.TestConstants.LINK;
import static integrationtests.TestConstants.testForPdf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


class AfterOcrIntegrationTest {

    private static final String address = "testadress";
    @Mock
    AlertAdminReporter alertAdminReporter;
    @Captor
    ArgumentCaptor<String> emailTextCaptor;
    @Captor
    ArgumentCaptor<String> addressCaptor;
    @Mock
    private AlarmResetter alarmResetter;
    @Mock
    private OCRProcessor ocrProcessor;
    @Mock
    private MainConfiguration mainConfiguration;
    @Mock
    private SystemInformation systemInformation;
    @Mock
    private EMailList eMailList;
    @Mock
    private AddressFinder addressFinder;
    private TestDisplay display;
    @Captor
    private ArgumentCaptor<File> pdfCaptor;
    private Provider<MainConfiguration> mainConfigurationProvider;
    private Extractor extractor;
    private TextCorrecter correcter;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        //main config
        when(mainConfiguration.should_filter_einsatzmittel()).thenReturn(false);
        when(ocrProcessor.pdfToString(Matchers.anyObject())).thenReturn(testForPdf);
        display = new TestDisplay();
        when(systemInformation.getConfigFolder()).thenReturn(new File("src/java/test/ressources"));
        doNothing().when(alarmResetter).resetAlarm(Matchers.anyLong());
        doNothing().when(eMailList).broadcast(emailTextCaptor.capture(), Matchers.anyBoolean());
        doNothing().when(alertAdminReporter).sendAlertToAdmin(Matchers.anyString());
        doNothing().when(alertAdminReporter).sendAlertToAdmin(Matchers.anyString(), Matchers.anyObject());
        when(addressFinder.createLink(Matchers.anyString())).thenReturn(LINK);
        Start.setDisplay(display);
        mainConfigurationProvider = () -> mainConfiguration;
        correcter = new TextCorrecterImpl(systemInformation);
        extractor = new ExtractorImpl(mainConfigurationProvider);
    }


    @Test
    void testProcessAfterOcr_FULL_EMAIL() throws Exception {
        when(ocrProcessor.pdfToString(Matchers.anyObject())).thenReturn(testForPdf);
        FaxProzessorImpl faxProzessor = new FaxProzessorImpl(alarmResetter, ocrProcessor, correcter, extractor, eMailList,
                mainConfigurationProvider, addressFinder, alertAdminReporter);
        faxProzessor.processAlarmFax(new File(""));

        assertEquals(TestConstants.opertionNumber, display.getAlarmfax().getOperationNumber());
        assertEquals(TestConstants.comment, display.getAlarmfax().getComment());
        assertEquals(TestConstants.operationResources, display.getAlarmfax().getOperationRessources());
        assertEquals(TestConstants.keyword, display.getAlarmfax().getKeyword());
        assertEquals(TestConstants.reporter, display.getAlarmfax().getReporter());
        assertEquals(TestConstants.address, display.getAlarmfax().getAddress());
        assertEquals(TestConstants.LINK, display.getAlarmfax().getLink());
        assertEquals(TestConstants.alarmTime, display.getAlarmfax().getAlarmTime());
    }

    @Test
    void testProcessAfterOcr_EMPY_PDF_WITHOUT_EXCEPTION() throws Exception {
        when(ocrProcessor.pdfToString(Matchers.anyObject())).thenReturn("");
        FaxProzessorImpl faxProzessor = new FaxProzessorImpl(alarmResetter, ocrProcessor, correcter, extractor, eMailList,
                mainConfigurationProvider, addressFinder, alertAdminReporter);
        faxProzessor.processAlarmFax(new File(""));


    }


}
