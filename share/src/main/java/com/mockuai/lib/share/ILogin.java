package com.mockuai.lib.share;

import com.mockuai.lib.share.listener.OnLoginListener;
import com.mockuai.lib.share.listener.OnShareListener;
import com.mockuai.lib.share.model.ShareContent;

/**
 * Created by zhangyuan on 16/3/21.
 */
public interface ILogin {

    void login(OnLoginListener onLoginListener);

}
