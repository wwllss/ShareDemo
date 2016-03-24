package com.mockuai.lib.sharedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mockuai.lib.share.IShare;
import com.mockuai.lib.share.constant.Platform;
import com.mockuai.lib.share.factory.PlatformFactory;
import com.mockuai.lib.share.listener.OnShareListener;
import com.mockuai.lib.share.model.ShareContent;
import com.mockuai.lib.share.sina.SinaFactory;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;


/**
 * Created by zhangyuan on 16/3/23.
 */
public class SecondActivity extends Activity implements IWeiboHandler.Response {

    private TextView textView;

    private OnShareListener listener = new OnShareListener() {
        @Override
        public void onSuccess() {
            Toast.makeText(SecondActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(SecondActivity.this, "分享取消", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            Toast.makeText(SecondActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
        }
    };

    private static final String TAG = "SecondActivity";

    private IWeiboShareAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        textView = new TextView(this);
        textView.setBackgroundColor(getResources().getColor(android.R.color.white));
        textView.setTextColor(getResources().getColor(android.R.color.black));
        textView.setText("Sina分享中……");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(55);
        setContentView(textView);


        MainActivity.Test test = (MainActivity.Test) getIntent().getSerializableExtra("test");
        ShareContent shareContent1 = getIntent().getParcelableExtra("shareContent");

        Log.d(TAG, "test--------------->   " + test.toString());
        Log.d(TAG, "shareContent------->   " + shareContent1.toString());

        final ShareContent shareContent = new ShareContent
                .Builder()
                .type(ShareContent.WEB_PAGE)
                .title("分享自Wwllss Library")
                .text("分享自Wwllss Library")
                .url("http://www.baidu.com")
                .imageUrl("http://img3.imgtn.bdimg.com/it/u=4062504192,3408107527&fm=21&gp=0.jpg")
                .build();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlatformFactory factory = new SinaFactory(SecondActivity.this);
                IShare share = factory.createShare(Platform.SINA);
                share.share(shareContent, listener);
            }
        });

        api = WeiboShareSDK.createWeiboAPI(this, "1918832594");
        api.registerApp();

        if (savedInstanceState != null) {
            api.handleWeiboResponse(getIntent(), this);
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        api.handleWeiboResponse(intent, this);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        Toast.makeText(this, baseResponse.errMsg, Toast.LENGTH_SHORT).show();
    }
}
