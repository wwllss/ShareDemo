package com.mockuai.lib.sharedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mockuai.lib.share.IShare;
import com.mockuai.lib.share.constant.Platform;
import com.mockuai.lib.share.factory.PlatformFactory;
import com.mockuai.lib.share.listener.OnShareListener;
import com.mockuai.lib.share.model.ShareContent;

import java.io.Serializable;

public class MainActivity extends Activity {

    private ShareContent shareContent;

    private OnShareListener listener = new OnShareListener() {
        @Override
        public void onSuccess() {
            Toast.makeText(MainActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(MainActivity.this, "分享取消", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            Toast.makeText(MainActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shareContent = new ShareContent
                .Builder()
                .type(ShareContent.WEB_PAGE)
                .title("分享自Wwllss Library")
                .text("分享自Wwllss Library")
                .url("http://www.baidu.com")
                .imageUrl("http://img3.imgtn.bdimg.com/it/u=4062504192,3408107527&fm=21&gp=0.jpg")
                .build();
    }

    public void shareByWeChat(View view) {
        IShare share = PlatformFactory.createShare(this, Platform.WE_CHAT);
        share.share(shareContent, listener);
    }

    public void shareByWeChatTimeLine(View view) {
        IShare share = PlatformFactory.createShare(this, Platform.WE_CHAT_TIME_LINE);
        share.share(shareContent, listener);
    }

    private static final String TAG = "MainActivity";

    public void shareBySina(View view) {
//        PlatformFactory factory = new SinaFactory(this);
//        IShare share = factory.createShare(Platform.SINA);
//        share.share(shareContent, listener);
        Intent intent = new Intent(this, SecondActivity.class);
        Test test = new Test();
        Log.d(TAG, "test--------------->   " + test.toString());
        Log.d(TAG, "shareContent------->   " + shareContent.toString());
        intent.putExtra("test", test);
        intent.putExtra("shareContent", shareContent);
        startActivity(intent);
    }

    public static class Test implements Serializable {

    }
}
