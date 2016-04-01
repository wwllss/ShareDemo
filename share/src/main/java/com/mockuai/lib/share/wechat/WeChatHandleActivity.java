package com.mockuai.lib.share.wechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.mockuai.lib.share.PlatformConfig;
import com.mockuai.lib.share.listener.CallbackManager;
import com.mockuai.lib.share.listener.OnLoginListener;
import com.mockuai.lib.share.listener.OnShareListener;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by zhangyuan on 16/3/22.
 */
public class WeChatHandleActivity extends FragmentActivity implements IWXAPIEventHandler {

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
        getRespHandle(baseResp.getType(), baseResp).onHandleResp();
    }

    private RespHandle getRespHandle(int type, BaseResp resp) {
        RespHandle handle;
        if (isShare(type)) {
            handle = new ShareHandle(resp);
        } else if (isLogin(type)) {
            handle = new LoginHandle(resp);
        } else {
            handle = new RespHandle(resp);
        }
        return handle;
    }

    private boolean isShare(int type) {
        return type == 2;
    }

    private boolean isLogin(int type) {
        return type == 1;
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    class RespHandle<T extends BaseResp> {

        private T resp;

        private String transaction;

        @SuppressWarnings("unchecked")
        public RespHandle(BaseResp resp) {
            this.resp = (T) resp;
            this.transaction = resp.transaction;
        }

        public T getResp() {
            return resp;
        }

        public String getTransaction() {
            return transaction;
        }

        public void onHandleResp() {
            toast(getResp().errStr);
        }
    }

    class ShareHandle extends RespHandle<SendMessageToWX.Resp> {

        public ShareHandle(BaseResp resp) {
            super(resp);
        }

        @Override
        public void onHandleResp() {
            OnShareListener listener = CallbackManager.getInstance().getOnShareListener(getTransaction());
            switch (getResp().errCode) {
                case BaseResp.ErrCode.ERR_OK: {
                    if (listener != null) {
                        listener.onSuccess();
                    } else {
                        toast("分享成功");
                    }
                }
                break;
                case BaseResp.ErrCode.ERR_USER_CANCEL: {
                    if (listener != null) {
                        listener.onCancel();
                    } else {
                        toast("分享取消");
                    }
                }
                break;
                default: {
                    if (listener != null) {
                        listener.onFailed();
                    } else {
                        toast("分享失败");
                    }
                }
                break;
            }
            CallbackManager.getInstance().removeOnShareListener(getTransaction());
        }
    }

    class LoginHandle extends RespHandle<SendAuth.Resp> {

        public LoginHandle(BaseResp resp) {
            super(resp);
        }

        @Override
        public void onHandleResp() {
            OnLoginListener listener = CallbackManager.getInstance().getOnLoginListener(getTransaction());
            switch (getResp().errCode) {
                case BaseResp.ErrCode.ERR_OK: {
                    if (listener != null) {
                        listener.onSuccess(getResp().code);
                    }
                }
                break;
                case BaseResp.ErrCode.ERR_USER_CANCEL: {
                    if (listener != null) {
                        listener.onCancel();
                    }
                }
                break;
                default: {
                    if (listener != null) {
                        listener.onFailed();
                    }
                }
                break;
            }
            CallbackManager.getInstance().removeOnLoginListener(getTransaction());
        }
    }
}
