package com.mockuai.lib.share.qq;

import android.content.Context;

import com.mockuai.lib.share.ILogin;
import com.mockuai.lib.share.IShare;
import com.mockuai.lib.share.constant.Platform;
import com.mockuai.lib.share.factory.PlatformFactory;
import com.mockuai.lib.share.wechat.WeChatLogin;
import com.mockuai.lib.share.wechat.WeChatShare;

/**
 * Created by zhangyuan on 16/3/21.
 */
public class QQFactory extends PlatformFactory {

    public QQFactory(Context context) {
        super(context);
    }

    @Override
    public IShare createShare(Platform platform) {
        return new QQShare(getContext(), platform);
    }

    @Override
    public ILogin createLogin() {
        return null;
    }
}
