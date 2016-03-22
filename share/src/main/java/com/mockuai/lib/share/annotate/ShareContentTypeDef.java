package com.mockuai.lib.share.annotate;

import android.support.annotation.IntDef;

import com.mockuai.lib.share.model.ShareContent;

/**
 * Created by zhangyuan on 16/3/21.
 */
@IntDef({
        ShareContent.TEXT,
        ShareContent.IMAGE,
        ShareContent.MUSIC,
        ShareContent.VIDEO,
        ShareContent.WEB_PAGE})
public @interface ShareContentTypeDef {
}
