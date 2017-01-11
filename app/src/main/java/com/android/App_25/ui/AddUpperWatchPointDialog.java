package com.android.App_25.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.App_25.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddUpperWatchPointDialog extends DialogFragment {

    @BindView(R.id.dialog_stock) public EditText upperWatchPoint;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View custom = inflater.inflate(R.layout.add_stock_dialog, null);

        ButterKnife.bind(this, custom);

        upperWatchPoint.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                addUpperWatchPoint();
                return true;
            }
        });
        builder.setView(custom);

        builder.setMessage(getString(R.string.dialog_Upper_watch));
        builder.setPositiveButton(getString(R.string.dialog_add),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        addUpperWatchPoint();
                    }
                });
        builder.setNegativeButton(getString(R.string.dialog_cancel), null);

        upperWatchPoint.setHint("");

        Dialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }

    private void addUpperWatchPoint() {
        Activity parent = getActivity();
        if (parent instanceof MainActivity) {
            ((MainActivity) parent).addStock(upperWatchPoint.getText().toString());
        }
        dismissAllowingStateLoss();
    }


}
