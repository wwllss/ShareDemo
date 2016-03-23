package com.mockuai.lib.share;

import android.util.Log;

/**
 * Created by zhangyuan on 16/3/21.
 */
public class PlatformConfig {

    private static final String TAG = "PlatformConfig";

    private static PlatformConfig instance;

    private WeChatConfig weChatConfig;

    private SinaConfig sinaConfig;

    public static PlatformConfig getInstance() {
        if (instance == null) {
            synchronized (PlatformConfig.class) {
                if (instance == null) {
                    instance = new PlatformConfig();
                }
            }
        }
        return instance;
    }

    private PlatformConfig() {
    }

    public void config(WeChatConfig weChatConfig, SinaConfig sinaConfig) {
        this.weChatConfig = weChatConfig;
        if (sinaConfig == null) {
            Log.e(TAG, "sina config can not be null");
        }
        this.sinaConfig = sinaConfig;
    }

    public WeChatConfig getWeChatConfig() {
        return weChatConfig;
    }

    public SinaConfig getSinaConfig() {
        return sinaConfig;
    }

    public static class WeChatConfig {
        private String appKey;

        private String appSecret;

        public WeChatConfig(String appKey, String appSecret) {
            this.appKey = appKey;
            this.appSecret = appSecret;
        }

        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public String getAppSecret() {
            return appSecret;
        }

        public void setAppSecret(String appSecret) {
            this.appSecret = appSecret;
        }
    }

    public static class SinaConfig {
        private String appKey;

        private String redirectUrl;

        private static final String DEFAULT_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

        private static final String DEFAULT_SCOPE = "email,direct_messages_read,direct_messages_write,"
                + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                + "follow_app_official_microblog," + "invitation_write";

        private String scope;

        public SinaConfig(String appKey) {
            this(appKey, DEFAULT_REDIRECT_URL, DEFAULT_SCOPE);
        }

        SinaConfig(String appKey, String redirectUrl) {
            this(appKey, redirectUrl, DEFAULT_SCOPE);
        }

        SinaConfig(String appKey, String redirectUrl, String scope) {
            this.appKey = appKey;
            this.redirectUrl = redirectUrl;
            this.scope = scope;
        }

        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public String getRedirectUrl() {
            return redirectUrl;
        }

        public void setRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }
    }

}
