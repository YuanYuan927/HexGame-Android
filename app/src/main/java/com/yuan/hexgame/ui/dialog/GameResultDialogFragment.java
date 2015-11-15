package com.yuan.hexgame.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import com.yuan.hexgame.R;
import com.yuan.hexgame.game.Game;
import com.yuan.hexgame.game.GameSettings;
import com.yuan.hexgame.game.Player;
import com.yuan.hexgame.util.LogUtil;

/**
 * Created by Yuan Sun on 2015/9/23.
 */
public class GameResultDialogFragment extends DialogFragment {

    private static final String TAG = "GameResultDialogFragment";
    private static final String KEY_DIALOG_MSG = "dialog_msg";
    private static final String KEY_IS_DIALOG_SHOW_SHARE = "is_dialog_show_share";

    private GameResultDialogListener mGameResultDialogListener;

    public interface GameResultDialogListener {

        public void onRestartClick();

        public void onShareResultClick();
    }

    public static GameResultDialogFragment newInstance(Context context, Player winner) {
        GameResultDialogFragment frag = new GameResultDialogFragment();
        String dialogMsg = "";
        boolean isDialogShowShare = false;
        Resources res = context.getResources();
        switch (GameSettings.getInstance().getGameMode()) {
            case GameSettings.MODE_HUMAN_VS_HUMAN:
                dialogMsg = winner == Player.A ? res.getString(R.string.game_result_dialog_msg_a_win) : res.getString(R.string.game_result_dialog_msg_b_win);
                break;
            case GameSettings.MODE_HUMAN_VS_ROBOT:
                dialogMsg = winner == Player.A ? res.getString(R.string.game_result_dialog_msg_human_win) : res.getString(R.string.game_result_dialog_msg_robot_win);
                isDialogShowShare = winner == Player.A;
                break;
        }
        Bundle args = new Bundle();
        args.putString(KEY_DIALOG_MSG, dialogMsg);
        args.putBoolean(KEY_IS_DIALOG_SHOW_SHARE, isDialogShowShare);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mGameResultDialogListener = (GameResultDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mGameResultDialogListener = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String dialogMsg = getArguments().getString(KEY_DIALOG_MSG);
        boolean isDialogShowShare = getArguments().getBoolean(KEY_IS_DIALOG_SHOW_SHARE);
        Activity parentActivity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        builder.setTitle(getString(R.string.game_result_dialog_title))
               .setMessage(dialogMsg)
                .setPositiveButton(getString(R.string.game_result_dialog_button_play_again), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mGameResultDialogListener != null)
                            mGameResultDialogListener.onRestartClick();
                        dismiss();
                    }
                });
        if (isDialogShowShare) {
            builder.setNegativeButton(getString(R.string.game_result_dialog_button_share), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mGameResultDialogListener != null)
                        mGameResultDialogListener.onShareResultClick();
                    dismiss();
                }
            });
        }
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        LogUtil.i(TAG, "onCancel()");
        super.onCancel(dialog);
        if (mGameResultDialogListener != null)
            mGameResultDialogListener.onRestartClick();
    }

}