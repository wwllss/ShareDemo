package com.mockuai.lib.share.sina;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.mockuai.lib.share.IShare;
import com.mockuai.lib.share.PlatformConfig;
import com.mockuai.lib.share.constant.Platform;
import com.mockuai.lib.share.listener.OnShareListener;
import com.mockuai.lib.share.model.ShareContent;
import com.mockuai.lib.share.utils.BitmapUtil;
import com.mockuai.lib.share.utils.ShareUtil;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;

import java.lang.ref.WeakReference;

/**
 * Created by zhangyuan on 16/3/22.
 */
public class SinaShare implements IShare {

    private static final int BITMAP_SIZE = 150;

    private static final String TAG = "SinaShare";

    private final Context context;

    private IWeiboShareAPI api;

    private OnShareListener listener;

    private final WeakReference<Handler> handler;

    private PlatformConfig.SinaConfig config;

    private Bitmap bitmap;

    SinaShare(Context context) {
        this.context = context;
        config = PlatformConfig.getInstance().getSinaConfig();
        String sinaId = config.getAppKey();
        if (TextUtils.isEmpty(sinaId)) {
            Log.e(TAG, "sina appid 为空");
        }
        api = WeiboShareSDK.createWeiboAPI(context, sinaId);
        api.registerApp();
        handler = new WeakReference<Handler>(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                buildMessage((ShareContent) msg.obj);
            }
        });
    }

    @Override
    public void share(final ShareContent content, OnShareListener onShareListener) {
        this.listener = onShareListener;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                bitmap = BitmapUtil.scaleCenterCrop(BitmapUtil.getBitmap(content.getImageUrl()), BITMAP_SIZE, BITMAP_SIZE);
                Handler handler = SinaShare.this.handler.get();
                if (handler != null) {
                    Message msg = handler.obtainMessage();
                    msg.obj = content;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    private void buildMessage(ShareContent content) {
        WeiboMultiMessage msg = new WeiboMultiMessage();
        msg.textObject = getTextObj(content.getTitle());
        msg.imageObject = getImageObj();
        switch (content.getType()) {
            case ShareContent.WEB_PAGE:
                msg.mediaObject = getWebpageObj(content);
                break;
            case ShareContent.MUSIC:
                msg.mediaObject = getMusicObj(content);
                break;
            case ShareContent.VIDEO:
                msg.mediaObject = getVideoObj(content);
                break;
        }

        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = ShareUtil.buildTransaction(Platform.SINA, content.getType());
        request.multiMessage = msg;

        sendrequest(request);

    }

    private void sendrequest(SendMultiMessageToWeiboRequest request) {
        AuthInfo authInfo = new AuthInfo(
                context,
                config.getAppKey(),
                config.getRedirectUrl(),
                config.getScope());
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(context);
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }
        api.sendRequest((Activity) context, request, authInfo, token, new WeiboAuthListener() {

            @Override
            public void onWeiboException(WeiboException arg0) {
                if (listener != null)
                    listener.onFailed();
            }

            @Override
            public void onComplete(Bundle bundle) {
                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                AccessTokenKeeper.writeAccessToken(context, newToken);
                if (listener != null)
                    listener.onSuccess();
            }

            @Override
            public void onCancel() {
                if (listener != null)
                    listener.onCancel();
            }
        });
    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj(String text) {
        TextObject textObject = new TextObject();
        textObject.text = text;
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj(ShareContent content) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = content.getTitle();
        mediaObject.description = content.getText();

        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = content.getUrl();
        mediaObject.defaultText = content.getTitle();
        return mediaObject;
    }

    /**
     * 创建多媒体（音乐）消息对象。
     *
     * @return 多媒体（音乐）消息对象。
     */
    private MusicObject getMusicObj(ShareContent content) {
        // 创建媒体消息
        MusicObject musicObject = new MusicObject();
        musicObject.identify = Utility.generateGUID();
        musicObject.title = content.getTitle();
        musicObject.description = content.getText();

        // 设置 Bitmap 类型的图片到视频对象里        设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        musicObject.setThumbImage(bitmap);
        musicObject.actionUrl = content.getUrl();
        musicObject.dataUrl = content.getUrl();
        musicObject.dataHdUrl = content.getUrl();
        musicObject.duration = 10;
        musicObject.defaultText = content.getTitle();
        return musicObject;
    }

    /**
     * 创建多媒体（视频）消息对象。
     *
     * @return 多媒体（视频）消息对象。
     */
    private VideoObject getVideoObj(ShareContent content) {
        // 创建媒体消息
        VideoObject videoObject = new VideoObject();
        videoObject.identify = Utility.generateGUID();
        videoObject.title = content.getTitle();
        videoObject.description = content.getText();

        videoObject.setThumbImage(bitmap);
        videoObject.actionUrl = content.getUrl();
        videoObject.dataUrl = content.getUrl();
        videoObject.dataHdUrl = content.getUrl();
        videoObject.duration = 10;
        videoObject.defaultText = content.getTitle();
        return videoObject;
    }

}
