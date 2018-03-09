package org.drulabs.localdash.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import org.drulabs.localdash.ChatActivity;
import org.drulabs.localdash.MediaPlayerActivity;
import org.drulabs.localdash.R;
import org.drulabs.localdash.model.DeviceDTO;
import org.drulabs.localdash.notification.NotificationToast;
import org.drulabs.localdash.transfer.DataSender;

import java.io.File;
import java.util.ArrayList;

/**
 * Authored by KaushalD on 9/2/2016.
 */
public class DialogUtils {

    public static final int CODE_PICK_AUDIO= 21;

    public static AlertDialog getServiceSelectionDialog(final Activity activity, final DeviceDTO
            selectedDevice) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(selectedDevice.getDeviceName());
        String[] types = {"Share audio","Play Media"};
        alertDialog.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                switch (which) {
                    case 0:
                        Intent audioPicker = new Intent(Intent.ACTION_GET_CONTENT);
                        audioPicker.setType("audio/*");
                        activity.startActivityForResult(audioPicker, CODE_PICK_AUDIO);
                        break;
                    case 1:
                        DataSender.sendChatRequest(activity, selectedDevice.getIp
                                (), selectedDevice.getPort());
                        NotificationToast.showToast(activity, "Media Play request " +
                                "sent");

                }
            }

        });

        return (alertDialog.create());
    }

    public static AlertDialog getChatRequestDialog(final Activity activity, final DeviceDTO requesterDevice) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

        String chatRequestTitle = activity.getString(R.string.chat_request_title);
        chatRequestTitle = String.format(chatRequestTitle, requesterDevice.getPlayerName() + "(" +
                requesterDevice.getDeviceName() + ")");
        alertDialog.setTitle(chatRequestTitle);
        String[] types = {"Accept", "Reject"};
        alertDialog.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                switch (which) {
                    //Request accepted
                    case 0:
                        openChatActivity(activity, requesterDevice);
                        NotificationToast.showToast(activity, "Media play request " +
                                "accepted");
                        DataSender.sendChatResponse(activity, requesterDevice.getIp(),
                                requesterDevice.getPort(), true);
                        break;
                    // Request rejected
                    case 1:
                        DataSender.sendChatResponse(activity, requesterDevice.getIp(),
                                requesterDevice.getPort(), false);
                        NotificationToast.showToast(activity, "Media play request " +
                                "rejected");
                        break;
                }
            }

        });

        return (alertDialog.create());
    }

//    public static void openChatActivity(Activity activity, DeviceDTO device) {
//        Intent chatIntent = new Intent(activity, ChatActivity.class);
//        chatIntent.putExtra(ChatActivity.KEY_CHAT_IP, device.getIp());
//        chatIntent.putExtra(ChatActivity.KEY_CHAT_PORT, device.getPort());
//        chatIntent.putExtra(ChatActivity.KEY_CHATTING_WITH, device.getPlayerName());
//        activity.startActivity(chatIntent);
//    }

    public static void openChatActivity(Activity activity, DeviceDTO device) {
        Intent mediaPlayerIntent = new Intent(activity, MediaPlayerActivity.class);
        mediaPlayerIntent.putExtra(ChatActivity.KEY_CHAT_IP, device.getIp());
        mediaPlayerIntent.putExtra(ChatActivity.KEY_CHAT_PORT, device.getPort());
        mediaPlayerIntent.putExtra(ChatActivity.KEY_CHATTING_WITH, device.getPlayerName());
        activity.startActivity(mediaPlayerIntent);
    }

    public static AlertDialog chooseMediaFileDialog(final Activity activity, File dir) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

        alertDialog.setTitle("Select File to Play");
        final File[] listofFiles = dir.listFiles();
        String[] files = new String[listofFiles.length];
        int k =0;

        for(int i =0;i<listofFiles.length;i++)
        {
            files[k++] = listofFiles[i].getName();

        }


        alertDialog.setItems(files, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                if(listofFiles[which].isFile())
                     ((MediaPlayerActivity)activity).setFileToPlay(listofFiles[which]);
                else
                    NotificationToast.showToast(activity,"Selected Item is not a valid file");

            }

        });

        return (alertDialog.create());
    }

}
