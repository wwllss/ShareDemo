package com.mockuai.lib.share.qq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mockuai.lib.share.IShare;
import com.mockuai.lib.share.PlatformConfig;
import com.mockuai.lib.share.constant.Platform;
import com.mockuai.lib.share.factory.PlatformFactory;
import com.mockuai.lib.share.listener.CallbackManager;
import com.mockuai.lib.share.model.ShareContent;
import com.tencent.tauth.Tencent;

/**
 * Created by zhangyuan on 16/3/23.
 */
public class QQHandleActivity extends Activity {

    private static final String KEY_TRANSACTION = "TRANSACTION";

    private static final String KEY_SHARE_CONTENT = "SHARE_CONTENT";

    private static final String KEY_PLATFORM = "PLATFORM";

    private String transaction;

    static void newIntent(Context context, String transaction, ShareContent content, Platform platform) {
        Intent i = new Intent(context, QQHandleActivity.class);
        i.putExtra(KEY_TRANSACTION, transaction);
        i.putExtra(KEY_SHARE_CONTENT, content);
        i.putExtra(KEY_PLATFORM, platform);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Platform platform = (Platform) getIntent().getSerializableExtra(KEY_PLATFORM);
        transaction = getIntent().getStringExtra(KEY_TRANSACTION);
        IShare share = PlatformFactory.createShare(this, platform);
        QQShare qqShare = (QQShare) share;
        qqShare.realShare(this, (ShareContent) getIntent().getParcelableExtra(KEY_SHARE_CONTENT), transaction, platform);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
        Tencent.onActivityResultData(requestCode, resultCode, data, CallbackManager.getInstance().getOnQQShareListener(transaction));
        CallbackManager.getInstance().removeOnQQShareListener(transaction);
    }
}
