package org.drulabs.localdash.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dingu on 1/1/18.
 */

public class MediaPlayerCommandDTO implements Serializable{
    private final String PLAY = "mediaplayer.play";
    private final String PAUSE = "mediaplayer.pause";
    private final String STOP = "mediaplayer.stop";



    private String command;
    private Date timeToExec;



    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Date getTimeToExec() {
        return timeToExec;
    }

    public void setTimeToExec(Date timeToExec) {
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
