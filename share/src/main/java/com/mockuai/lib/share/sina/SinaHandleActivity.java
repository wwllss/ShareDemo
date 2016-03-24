package com.mockuai.lib.share.sina;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.mockuai.lib.share.IShare;
import com.mockuai.lib.share.PlatformConfig;
import com.mockuai.lib.share.R;
import com.mockuai.lib.share.constant.Platform;
import com.mockuai.lib.share.factory.PlatformFactory;
import com.mockuai.lib.share.listener.CallbackManager;
import com.mockuai.lib.share.listener.OnShareListener;
import com.mockuai.lib.share.model.ShareContent;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;

/**
 * Created by zhangyuan on 16/3/23.
 */
public class SinaHandleActivity extends FragmentActivity implements IWeiboHandler.Response {

    private static final String KEY_TRANSACTION = "TRANSACTION";

    private static final String KEY_SHARE_CONTENT = "SHARE_CONTENT";

    private IWeiboShareAPI api;

    static void newIntent(Context context, String transaction, ShareContent content) {
        Intent i = new Intent(context, SinaHandleActivity.class);
        i.putExtra(KEY_TRANSACTION, transaction);
        i.putExtra(KEY_SHARE_CONTENT, content);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = new View(this);
        view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        setContentView(view);
        api = WeiboShareSDK.createWeiboAPI(this, PlatformConfig.getInstance().getSinaConfig().getAppKey());
        api.registerApp();
        String transaction = getIntent().getStringExtra(KEY_TRANSACTION);
        if (TextUtils.isEmpty(transaction)) {
            if (savedInstanceState != null) {
                api.handleWeiboResponse(getIntent(), this);
                finish();
            }
        } else {
            IShare share = PlatformFactory.createShare(this, Platform.SINA);
            SinaShare sinaShare = (SinaShare) share;
            sinaShare.realShare((ShareContent) getIntent().getParcelableExtra(KEY_SHARE_CONTENT), transaction);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        api.handleWeiboResponse(intent, this);
        finish();
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        if (baseResponse != null) {
            OnShareListener listener = CallbackManager.getInstance().getOnShareListener(baseResponse.transaction);
            switch (baseResponse.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    if (listener != null) {
                        listener.onSuccess();
                    } else {
                        Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
                    }
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    if (listener != null) {
                        listener.onCancel();
                    } else {
                        Toast.makeText(this, "分享取消", Toast.LENGTH_LONG).show();
                    }
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    if (listener != null) {
                        listener.onFailed();
                    } else {
                        Toast.makeText(this, "分享失败", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
            CallbackManager.getInstance().removeOnShareListener(baseResponse.transaction);
        }

    }
}
