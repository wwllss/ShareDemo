package com.mockuai.lib.share.qq;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.mockuai.lib.share.IShare;
import com.mockuai.lib.share.PlatformConfig;
import com.mockuai.lib.share.R;
import com.mockuai.lib.share.constant.Platform;
import com.mockuai.lib.share.listener.CallbackManager;
import com.mockuai.lib.share.listener.OnShareListener;
import com.mockuai.lib.share.model.ShareContent;
import com.mockuai.lib.share.utils.ShareUtil;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * Created by zhangyuan on 16/5/9.
 */
public class QQShare implements IShare {

    private Context context;

    private Platform platform;

    private String transaction;

    public QQShare(Context context, Platform platform) {
        this.context = context;
        this.platform = platform;
    }

    @Override
    public void share(ShareContent content, final OnShareListener onShareListener) {
        transaction = ShareUtil.buildTransaction(platform, content.getType());
        CallbackManager.getInstance().addOnShareListener(transaction, onShareListener);
        IUiListener listener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                if (onShareListener != null) {
                    onShareListener.onSuccess();
                }
            }

            @Override
            public void onError(UiError uiError) {
                if (onShareListener != null) {
                    onShareListener.onFailed();
                }
            }

            @Override
            public void onCancel() {
                if (onShareListener != null) {
                    onShareListener.onCancel();
                }
            }
        };
        CallbackManager.getInstance().addOnQQShareListener(transaction, listener);
        QQHandleActivity.newIntent(context, transaction, content, platform);
    }

    void realShare(Activity activity, ShareContent content, String transaction, Platform platform) {
        this.context = activity;
        this.transaction = transaction;
        this.platform = platform;

        Tencent api = Tencent.createInstance(PlatformConfig.getInstance().getQQConfig().getTencentId(), context);
        Bundle bundle = buildShareBundle(content, platform);
        if (platform == Platform.QQ) {
            api.shareToQQ((Activity) context, bundle, CallbackManager.getInstance().getOnQQShareListener(transaction));
        } else if (platform == Platform.QZONE) {
            api.shareToQzone((Activity) context, bundle, CallbackManager.getInstance().getOnQQShareListener(transaction));
        }
    }

    private Bundle buildShareBundle(ShareContent content, Platform platform) {
        if (platform == Platform.QQ) {
            return buildShareBundleQQ(content);
        } else if (platform == Platform.QZONE) {
            return buildShareBundleQZone(content);
        }
        throw new IllegalArgumentException("unknown share platform,this class supported Platform.QQ or Platform.QZONE,not " + platform);
    }

    private Bundle buildShareBundleQZone(ShareContent content) {
        final Bundle bundle = new Bundle();
        bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, content.getTitle());
        bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, content.getText());
        bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, content.getUrl());
        ArrayList<String> list = new ArrayList<>();
        list.add(content.getImageUrl());
        bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, list);
        return bundle;
    }

    private Bundle buildShareBundleQQ(ShareContent content) {
        final Bundle params = new Bundle();
        params.putInt(com.tencent.connect.share.QQShare.SHARE_TO_QQ_KEY_TYPE, com.tencent.connect.share.QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_TITLE, content.getTitle());
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_SUMMARY, content.getText());
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_TARGET_URL, content.getUrl());
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_IMAGE_URL, content.getImageUrl());
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_APP_NAME, context.getString(R.string.app_name));
        return params;
    }
}
