package de.alarm_monitor.main;

import de.alarm_monitor.callback.NewPdfCallback;

import java.io.File;

public class ObserverCallbackHandler extends Thread{

    private NewPdfCallback callback;
    private File file;

    public ObserverCallbackHandler(NewPdfCallback callback, File file){
        this.callback = callback;
        this.file = file;
    }

    @Override
    public void run(){
        callback.onNewPdfFile(file);
    }
}
