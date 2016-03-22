package com.mockuai.lib.share.utils;

import com.mockuai.lib.share.constant.Platform;
import com.mockuai.lib.share.model.ShareContent;

/**
 * Created by zhangyuan on 16/3/22.
 */
public class ShareUtil {

    public static String buildTransaction(Platform platform, final int type) {
        String prefix = getPlatformDesc(platform);
        switch (type) {
            case ShareContent.TEXT:
                prefix += "text";
                break;
            case ShareContent.IMAGE:
                prefix += "image";
                break;
            case ShareContent.WEB_PAGE:
                prefix += "webpage";
                break;
            case ShareContent.MUSIC:
                prefix += "music";
        }
        return prefix + System.currentTimeMillis();
    }

    public static String getPlatformDesc(Platform platform) {
        String desc = "";
        if (platform == Platform.WE_CHAT_TIME_LINE) {
            desc = "WE_CHAT_TIME_LINE";
        } else if (platform == Platform.WE_CHAT) {
            desc = "WE_CHAT";
        } else if (platform == Platform.SINA) {
            desc = "SINA";
        }
        return desc;
    }

}
