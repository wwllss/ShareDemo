package com.mockuai.lib.share.listener;

/**
 * Created by zhangyuan on 16/3/21.
 */
public interface OnLoginListener {

    void onSuccess(String code);

    void onCancel();

    void onFailed();

}
