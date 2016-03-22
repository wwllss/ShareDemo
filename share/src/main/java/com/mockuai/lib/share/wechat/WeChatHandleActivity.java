package com.mockuai.lib.share.wechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mockuai.lib.share.PlatformConfig;
import com.mockuai.lib.share.listener.CallbackManager;
import com.mockuai.lib.share.listener.OnShareListener;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by zhangyuan on 16/3/22.
 */
public class WeChatHandleActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, PlatformConfig.getInstance().getWeChatConfig().getAppKey());
        api.handleIntent(getIntent(), this);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        int type = baseResp.getType();
        String transaction = baseResp.transaction;
        CallbackManager callbackManager = CallbackManager.getInstance();
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK: {
                if (isShare(type)) {
                    OnShareListener onShareListener = callbackManager.getOnShareListener(transaction);
                    if (onShareListener != null) {
                        onShareListener.onSuccess();
                    }
                }
            }
            break;
            case BaseResp.ErrCode.ERR_USER_CANCEL: {
                if (isShare(type)) {
                    OnShareListener onShareListener = callbackManager.getOnShareListener(transaction);
                    if (onShareListener != null) {
                        onShareListener.onCancel();
                    }
                }
            }
            break;
            default: {
                if (isShare(type)) {
                    OnShareListener onShareListener = callbackManager.getOnShareListener(transaction);
                    if (onShareListener != null) {
                        onShareListener.onFailed();
                    }
                }
            }
            break;
        }
    }

    private boolean isShare(int type) {
        return type == 2;
    }

    private boolean isLogin(int type) {
        return type == 1;
    }
}
