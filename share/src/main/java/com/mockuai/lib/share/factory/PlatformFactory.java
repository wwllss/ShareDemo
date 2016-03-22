package com.mockuai.lib.share.factory;

import android.content.Context;

import com.mockuai.lib.share.ILogin;
import com.mockuai.lib.share.IShare;
import com.mockuai.lib.share.constant.Platform;
import com.mockuai.lib.share.sina.SinaFactory;
import com.mockuai.lib.share.wechat.WeChatFactory;

/**
 * Created by zhangyuan on 16/3/21.
 */
public abstract class PlatformFactory {

    private final Context context;

    public PlatformFactory(Context context) {
        this.context = context;
    }

    public abstract IShare createShare(Platform platform);

    public abstract ILogin createLogin();

    public Context getContext() {
        return context;
    }

    public static PlatformFactory get(Context context, Platform platform) {
        PlatformFactory factory = null;
        if (platform == Platform.WE_CHAT || platform == Platform.WE_CHAT_TIME_LINE) {
            factory = new WeChatFactory(context);
        } else if (platform == Platform.SINA) {
            factory = new SinaFactory(context);
        }
        return factory;
    }

    public static IShare createShare(Context context, Platform platform) {
        return get(context, platform).createShare(platform);
    }

    public static ILogin createLogin(Context context, Platform platform) {
        return get(context, platform).createLogin();
    }
}
