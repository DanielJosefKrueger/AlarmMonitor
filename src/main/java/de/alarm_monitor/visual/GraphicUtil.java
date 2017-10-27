package de.alarm_monitor.visual;

import de.alarm_monitor.main.Observer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class GraphicUtil {

    private static Logger logger = LogManager.getLogger(GraphicUtil.class);
    //from   https://stackoverflow.com/questions/4627553/show-jframe-in-a-specific-screen-in-dual-monitor-configuration
    public static void showOnScreen( int screen, JFrame frame )
    {
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();

        if( screen > -1 && screen < gs.length )
        {
            gs[screen].setFullScreenWindow( frame );
        }
        else if( gs.length > 0 )
        {
           gs[0].setFullScreenWindow( frame );

        }
        else
        {
            throw new RuntimeException( "No Screens Found" );
        }
    }


}
