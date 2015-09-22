package com.yuan.hexgame.ui.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yuan.hexgame.R;

/**
 * Created by Yuan Sun on 2015/9/23.
 */
public class GameResultDialogFragment extends DialogFragment {

    private TextView mTvTitle;
    private TextView mTvActionPlayAgain;
    private TextView mTvActionShare;

    public GameResultDialogFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Activity parentActivity = getActivity();
//        LayoutInflater li = parentActivity.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_game_result, container);
        mTvTitle = (TextView) dialogLayout.findViewById(R.id.tv_dialog_title);
        mTvActionPlayAgain = (TextView) dialogLayout.findViewById(R.id.tv_action_play_again);
        mTvActionShare = (TextView) dialogLayout.findViewById(R.id.tv_action_share);

        mTvActionShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mTvActionPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return dialogLayout;
    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Activity parentActivity = getActivity();
//        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
//        LayoutInflater li = parentActivity.getLayoutInflater();
//        View dialogLayout = li.inflate(R.layout.dialog_game_result, null);
//        builder.setView(dialogLayout)
//        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dismiss();
//            }
//        });
//        mTvTitle = (TextView) dialogLayout.findViewById(R.id.tv_dialog_title);
//        mTvActionPlayAgain = (TextView) dialogLayout.findViewById(R.id.tv_action_play_again);
//        mTvActionShare = (TextView) dialogLayout.findViewById(R.id.tv_action_share);
//
//        mTvActionShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//        mTvActionPlayAgain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//        return builder.create();
//    }

}
