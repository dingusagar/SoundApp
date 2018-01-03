package org.drulabs.localdash.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dingu on 1/1/18.
 */

public class MediaPlayerCommandDTO implements Serializable{
    public final static String PLAY = "mediaplayer.play";
    public final static String PAUSE = "mediaplayer.pause";
    public final static String STOP = "mediaplayer.stop";


    public MediaPlayerCommandDTO(String command) {
        this.command = command;
    }

    private String command;
    private long timeToExec;
    private String fileName;



    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }



    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public long getTimeToExec() {
        return timeToExec;
    }

    public void setTimeToExec(long timeToExec) {
        this.timeToExec = timeToExec;
    }



    @Override
    public String toString() {
        String stringRep = (new Gson()).toJson(this);
        return stringRep;
    }

    public static MediaPlayerCommandDTO fromJSON(String jsonRep) {
        Gson gson = new Gson();
        MediaPlayerCommandDTO commandDTO= gson.fromJson(jsonRep, MediaPlayerCommandDTO.class);
        return commandDTO;
    }
}
