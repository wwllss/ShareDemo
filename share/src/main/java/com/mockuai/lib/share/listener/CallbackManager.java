package com.mockuai.lib.share.listener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyuan on 16/3/21.
 */
public class CallbackManager {

    private static CallbackManager instance;

    private final Map<String, OnShareListener> shareMap;

    private final Map<String, OnLoginListener> LoginMap;

    private CallbackManager() {
        shareMap = new HashMap<>();
        LoginMap = new HashMap<>();
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
}
