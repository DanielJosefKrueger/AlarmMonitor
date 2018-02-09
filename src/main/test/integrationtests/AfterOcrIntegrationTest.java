package integrationtests;

import com.google.inject.Provider;
import de.alarm_monitor.configuration.MainConfiguration;
import de.alarm_monitor.correcting.TextCorrecter;
import de.alarm_monitor.correcting.TextCorrecterImpl;
import de.alarm_monitor.email.EMailList;
import de.alarm_monitor.extracting.Extractor;
import de.alarm_monitor.extracting.ExtractorImpl;
import de.alarm_monitor.main.SystemInformation;
import de.alarm_monitor.parsing.OCRProcessor;
import de.alarm_monitor.processing.FaxProzessorImpl;
import de.alarm_monitor.util.AddressFinder;
import de.alarm_monitor.util.AlarmResetter;
import de.alarm_monitor.visual.IDisplay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import testutil.TestDisplay;

import java.io.File;

import static integrationtests.TestConstants.testForPdf;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


class AfterOcrIntegrationTest {

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

    private IDisplay display;
    @Captor
    private ArgumentCaptor<File> pdfCaptor;
    @Captor
    ArgumentCaptor<String> emailTextCaptor;

    @Captor
    ArgumentCaptor<String> addressCaptor;


    private Provider<MainConfiguration> mainConfigurationProvider;
    private Extractor extractor;
    private TextCorrecter correcter;

    private static final String address = "testadress";

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.initMocks(this.getClass());
        //main config
        when(mainConfiguration.should_filter_einsatzmittel()).thenReturn(false);
        display = new TestDisplay();
        doNothing().when(display).changeAddress(addressCaptor.capture());
        doNothing().when(display).activateAlarm();
        when(systemInformation.getConfigFolder()).thenReturn(new File("../testressources"));
        doNothing().when(alarmResetter).resetAlarm(Matchers.anyLong());
        doNothing().when(eMailList).broadcast(emailTextCaptor.capture());
        when(addressFinder.createLink(Matchers.anyString())).thenReturn(address);
        mainConfigurationProvider = () -> mainConfiguration;
        correcter = new TextCorrecterImpl(systemInformation);
        extractor = new ExtractorImpl(mainConfigurationProvider);
    }


    @Test
    void testProcessAfterOcr() throws Exception {


        FaxProzessorImpl faxProzessor = new FaxProzessorImpl(alarmResetter, ocrProcessor, correcter, extractor, eMailList,
                mainConfigurationProvider, addressFinder);
        faxProzessor.processAlarmFax(new File(""));
        when(ocrProcessor.pdfToString(pdfCaptor.capture())).thenReturn(testForPdf);


    }


}
