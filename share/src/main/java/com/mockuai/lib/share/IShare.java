package com.mockuai.lib.share;

import android.content.Context;

import com.mockuai.lib.share.listener.OnShareListener;
import com.mockuai.lib.share.model.ShareContent;

/**
 * Created by zhangyuan on 16/3/21.
 */
public interface IShare {

    void share(ShareContent content, OnShareListener onShareListener);

}
