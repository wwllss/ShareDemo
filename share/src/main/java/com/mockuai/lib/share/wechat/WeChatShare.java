package com.mockuai.lib.share.wechat;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.mockuai.lib.share.constant.Platform;
import com.mockuai.lib.share.listener.CallbackManager;
import com.mockuai.lib.share.IShare;
import com.mockuai.lib.share.PlatformConfig;
import com.mockuai.lib.share.listener.OnShareListener;
import com.mockuai.lib.share.model.ShareContent;
import com.mockuai.lib.share.utils.BitmapUtil;
import com.mockuai.lib.share.utils.ShareUtil;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXMusicObject;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by zhangyuan on 16/3/21.
 */
public class WeChatShare implements IShare {

    private static final String TAG = "WeChatShare";

    private static final int THUMB_SIZE = 100;

    private IWXAPI wxapi;

    private int scene;

    private String transaction;

    private Platform platform;

    WeChatShare(Context context, Platform platform) {
        this.platform = platform;
        String weChatId = PlatformConfig.getInstance().getWeChatConfig().getAppKey();
        if (TextUtils.isEmpty(weChatId)) {
            Log.e(TAG, "wechat appid 为空");
        }
        wxapi = WXAPIFactory.createWXAPI(context, weChatId);
        wxapi.registerApp(weChatId);
        scene = platform == Platform.WE_CHAT_TIME_LINE
                ? SendMessageToWX.Req.WXSceneTimeline
                : SendMessageToWX.Req.WXSceneSession;
    }

    @Override
    public void share(ShareContent content, OnShareListener onShareListener) {
        transaction = ShareUtil.buildTransaction(platform, content.getType());
        CallbackManager.getInstance().addOnShareListener(transaction, onShareListener);
        switch (content.getType()) {
            case ShareContent.TEXT:
                shareText(content);
                break;
            case ShareContent.IMAGE:
                sharePicture(content);
                break;
            case ShareContent.WEB_PAGE:
                shareWebPage(content);
                break;
            case ShareContent.MUSIC:
                shareMusic(content);
        }
    }

    private void shareText(ShareContent shareContent) {
        String text = shareContent.getText();
        if (TextUtils.isEmpty(text)) {
            text = shareContent.getTitle();
        }
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = transaction;
        req.message = msg;
        req.scene = scene;
        wxapi.sendReq(req);
    }


    private void sharePicture(ShareContent shareContent) {
        WXImageObject imgObj = new WXImageObject();
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = transaction;
        req.message = msg;
        req.scene = scene;
        share(shareContent.getImageUrl(), req);
    }

    private void shareWebPage(ShareContent shareContent) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareContent.getUrl();
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = shareContent.getTitle();
        msg.description = shareContent.getText();

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = transaction;
        req.message = msg;
        req.scene = scene;
        share(shareContent.getImageUrl(), req);
    }


    private void shareMusic(ShareContent shareContent) {
        WXMusicObject music = new WXMusicObject();
        music.musicUrl = shareContent.getUrl();
        WXMediaMessage msg = new WXMediaMessage(music);
        msg.title = shareContent.getTitle();
        msg.description = shareContent.getText();
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = transaction;
        req.message = msg;
        req.scene = scene;
        share(shareContent.getImageUrl(), req);
    }

    private void share(final String imageUrl, final SendMessageToWX.Req req) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap image = BitmapUtil.getBitmap(imageUrl);
                    if (image != null) {
                        if (req.message.mediaObject instanceof WXImageObject) {
                            req.message.mediaObject = new WXImageObject(image);
                        }
                        req.message.thumbData = BitmapUtil.bitmapToByteArray(BitmapUtil.scaleCenterCrop(image, THUMB_SIZE, THUMB_SIZE));
                    }
                    wxapi.sendReq(req);
                } catch (Throwable throwable) {
                    //ignored
                }
            }
        };
        AsyncTask.execute(runnable);
    }
}
