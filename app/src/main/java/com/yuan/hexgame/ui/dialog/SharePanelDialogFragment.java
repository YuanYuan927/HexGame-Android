package com.yuan.hexgame.ui.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;
import com.yuan.hexgame.R;
import com.yuan.hexgame.util.LogUtil;

/**
 * Created by Yuan Sun on 2015/11/8.
 */
public class SharePanelDialogFragment extends DialogFragment {

    private static final String TAG = "SharePanelDialogFragment";

    public static final int SHARE_GAME = 0;
    public static final int SHARE_GAME_RESULT = 1;

    private static final String KEY_SHARE_TYPE = "share_type";
    private static final String KEY_RESULT_IMAGE = "result_image";

    private static final String APP_ID = "wxe8ca98a1f041b2be"; //wxe8ca98a1f041b2be wxd930ea5d5a258f4f
    //    AppID：wxe8ca98a1f041b2be
    //    AppSecret：d4624c36b6795d1d99dcf0547af5443d

    private static final String APP_DOWNLOAD_URL = "http://www.wandoujia.com/apps/com.yuan.hexgame";

    private ImageView mIvWeChat;
    private ImageView mIvMoments;

    private int mShareType;
    private Bitmap mResultImage;

    private IWXAPI mWXAPI;


    public static SharePanelDialogFragment newInstance(int shareType) {
        return newInstance(SHARE_GAME, null);
    }

    public static SharePanelDialogFragment newInstance(int shareType, Bitmap resultImage) {
        SharePanelDialogFragment frag = new SharePanelDialogFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_SHARE_TYPE, shareType);
        args.putParcelable(KEY_RESULT_IMAGE, resultImage);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LogUtil.i(TAG, "onCreateDialog");
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.i(TAG, "onCreateView");
        View sharePanelLayout = inflater.inflate(R.layout.dialog_share_panel, container);
        mIvWeChat = (ImageView) sharePanelLayout.findViewById(R.id.iv_wechat);
        mIvMoments = (ImageView) sharePanelLayout.findViewById(R.id.iv_moments);
        mIvWeChat.setOnClickListener(mShareOnClickListener);
        mIvMoments.setOnClickListener(mShareOnClickListener);

        mShareType = getArguments().getInt(KEY_SHARE_TYPE);
        mResultImage = getArguments().getParcelable(KEY_RESULT_IMAGE);
        registerAppToWeChat();
        return sharePanelLayout;
    }

    private View.OnClickListener mShareOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_wechat:
                    shareToWeChat(false);
                    break;
                case R.id.iv_moments:
                    shareToWeChat(true);
                    break;
            }
            dismiss();
        }
    };

    private void registerAppToWeChat() {
        mWXAPI = WXAPIFactory.createWXAPI(getActivity(), APP_ID);
        mWXAPI.registerApp(APP_ID);
    }

    public void shareToWeChat(boolean isMoments) {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        if (mShareType == SHARE_GAME) {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = APP_DOWNLOAD_URL;
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = getString(R.string.app_name);
            msg.description = getString(R.string.social_wechat_description);
            Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_square);
            msg.thumbData = Util.bmpToByteArray(thumb, true);
            req.transaction = buildTransaction("webpage");
            req.message = msg;
        } else if (mShareType == SHARE_GAME_RESULT) {
            WXImageObject imgObj = new WXImageObject(mResultImage);
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = imgObj;
            Bitmap thumbBmp = Bitmap.createScaledBitmap(mResultImage, 240, 135, true);
            msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
            req.transaction = buildTransaction("img");
            req.message = msg;
        }
        req.scene = isMoments ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        mWXAPI.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
