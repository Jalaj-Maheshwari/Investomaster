package com.android.App_25.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.App_25.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddLowerWatchPointDialog extends DialogFragment {




    @BindView(R.id.dialog_stock) public EditText lowerWatchPoint;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View custom = inflater.inflate(R.layout.add_stock_dialog, null);

        ButterKnife.bind(this, custom);

        lowerWatchPoint.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                addWatchPoint();
                return true;
            }
        });
        builder.setView(custom);

        builder.setMessage(getString(R.string.dialog_Lower_watch));
        builder.setPositiveButton(getString(R.string.dialog_add),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        addWatchPoint();
                    }
                });
        builder.setNegativeButton(getString(R.string.dialog_cancel), null);

        lowerWatchPoint.setHint("");
        Dialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }

    private void addWatchPoint() {
        Activity parent = getActivity();
        if (parent instanceof MainActivity) {
            int lowerpoint = Integer.parseInt(lowerWatchPoint.getText().toString());
            int notificationID = 1;
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext());
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setContentTitle("Team_25 ");
            mBuilder.setContentText("Stocks Crossed Lower Watch point");

            Intent resultIntent = new Intent(getContext(), MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
            stackBuilder.addParentStack(MainActivity.class);

// Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

           // NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
            //mNotificationManager.notify(notificationID, mBuilder.build());
        }
        dismissAllowingStateLoss();
    }


}
