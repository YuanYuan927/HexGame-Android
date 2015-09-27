package com.yuan.hexgame.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.yuan.hexgame.game.Player;

/**
 * Created by Yuan Sun on 2015/9/23.
 */
public class GameResultDialogFragment extends DialogFragment {

    private static final String KEY_DIALOG_MSG = "dialog_msg";

    public static GameResultDialogFragment newInstance(Player winner) {
        GameResultDialogFragment frag = new GameResultDialogFragment();
        String dialogMsg = getResultMessage(winner);
        Bundle args = new Bundle();
        args.putString(KEY_DIALOG_MSG, dialogMsg);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String dialogMsg = getArguments().getString(KEY_DIALOG_MSG);
        Activity parentActivity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        builder.setTitle("Result")
                .setMessage(dialogMsg)
                .setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setNegativeButton("Share", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder.create();
    }

    private static String getResultMessage(Player winner) {
        switch (winner) {
            case A:
                return "A win!";
            case B:
                return "B win!";
        }
        return "ERROR";
    }
}
