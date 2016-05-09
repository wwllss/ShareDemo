package com.mockuai.lib.share.listener;

import com.tencent.tauth.IUiListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyuan on 16/3/21.
 */
public class CallbackManager {

    private static CallbackManager instance;

    private final Map<String, OnShareListener> shareMap;

    private final Map<String, OnLoginListener> LoginMap;

    private final Map<String, IUiListener> qqShareMap;

    private CallbackManager() {
        shareMap = Collections.synchronizedMap(new HashMap<String, OnShareListener>());
        LoginMap = Collections.synchronizedMap(new HashMap<String, OnLoginListener>());
        qqShareMap = Collections.synchronizedMap(new HashMap<String, IUiListener>());
    }

    public static CallbackManager getInstance() {
        if (instance == null) {
            synchronized (CallbackManager.class) {
                if (instance == null) {
                    instance = new CallbackManager();
                }
            }
        }
        return instance;
    }

    public OnShareListener getOnShareListener(String transaction) {
        return shareMap.get(transaction);
    }

    public void removeOnShareListener(String transaction) {
        if (shareMap.containsKey(transaction))
            shareMap.remove(transaction);
    }

    public void addOnShareListener(String transaction, OnShareListener onShareListener) {
        shareMap.put(transaction, onShareListener);
    }

    public OnLoginListener getOnLoginListener(String transaction) {
        return LoginMap.get(transaction);
    }

    public void removeOnLoginListener(String transaction) {
        if (LoginMap.containsKey(transaction))
            LoginMap.remove(transaction);
    }

    public void addOnLoginListener(String transaction, OnLoginListener onLoginListener) {
        LoginMap.put(transaction, onLoginListener);
    }

    public void addOnQQShareListener(String transaction, IUiListener iUiListener) {
        qqShareMap.put(transaction, iUiListener);
    }

    public IUiListener getOnQQShareListener(String transaction) {
        return qqShareMap.get(transaction);
    }

    public void removeOnQQShareListener(String transaction) {
        if (qqShareMap.containsKey(transaction))
            qqShareMap.remove(transaction);
    }
}
