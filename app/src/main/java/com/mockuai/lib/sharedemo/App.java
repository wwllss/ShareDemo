package com.mockuai.lib.sharedemo;

import android.app.Application;

import com.mockuai.lib.share.PlatformConfig;

/**
 * Created by zhangyuan on 16/3/23.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PlatformConfig.getInstance().config(
                new PlatformConfig.WeChatConfig("wx4aaa888cb11aa851", ""),
                new PlatformConfig.SinaConfig("1918832594")
        );
    }
}
