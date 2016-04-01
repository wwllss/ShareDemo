package com.mockuai.lib.share.wechat;

import android.content.Context;

import com.mockuai.lib.share.ILogin;
import com.mockuai.lib.share.PlatformConfig;
import com.mockuai.lib.share.constant.Platform;
import com.mockuai.lib.share.listener.CallbackManager;
import com.mockuai.lib.share.listener.OnLoginListener;
import com.mockuai.lib.share.listener.OnShareListener;
import com.mockuai.lib.share.utils.ShareUtil;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * TODO 目前只做到获取code。剩下的交于后台来做，可避免用户数据泄露风险。
 * Created by zhangyuan on 16/3/22.
 */
public class WeChatLogin implements ILogin {

    private IWXAPI api;

    private final String transaction;

    WeChatLogin(Context context) {
        String appKey = PlatformConfig.getInstance().getWeChatConfig().getAppKey();
        api = WXAPIFactory.createWXAPI(context, appKey);
        api.registerApp(appKey);
        this.transaction = ShareUtil.buildTransaction("WE_CHAT_LOGIN");
    }

    @Override
    public void login(OnLoginListener onLoginListener) {
        CallbackManager.getInstance().addOnLoginListener(transaction, onLoginListener);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.transaction = transaction;
        api.sendReq(req);
    }

}
