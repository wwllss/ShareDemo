package com.mockuai.lib.share.wechat;

import android.content.Context;

import com.mockuai.lib.share.ILogin;
import com.mockuai.lib.share.IShare;
import com.mockuai.lib.share.constant.Platform;
import com.mockuai.lib.share.factory.PlatformFactory;

/**
 * Created by zhangyuan on 16/3/21.
 */
public class WeChatFactory extends PlatformFactory {

    public WeChatFactory(Context context) {
        super(context);
    }

    @Override
    public IShare createShare(Platform platform) {
        return new WeChatShare(getContext(), platform);
    }

    @Override
    public ILogin createLogin() {
        return new WeChatLogin();
    }
}
