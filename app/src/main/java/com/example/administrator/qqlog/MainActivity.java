package com.example.administrator.qqlog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.shareboard.SnsPlatform;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public ArrayList<SnsPlatform> platforms = new ArrayList<SnsPlatform>();
    private SHARE_MEDIA[] list = {SHARE_MEDIA.QQ};
    private TextView log;
    private TextView unlog;
    private ImageView qqImg;
    private TextView qqName;
    private UMAuthListener authListener;
    private TextView shareAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVariable();
        initView();
    }
    //控件的方法
    private void initView() {
        qqImg = (ImageView) findViewById(R.id.qqImg);
        qqName = (TextView) findViewById(R.id.qqName);
        log = (TextView) findViewById(R.id.log);
        unlog = (TextView) findViewById(R.id.unlog);
        shareAction = (TextView) findViewById(R.id.ShareAction);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击授权的方法
                UMShareAPI.get(MainActivity.this).doOauthVerify(MainActivity.this, platforms.get(0).mPlatform, authListener);
            }
        });
        unlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMShareAPI.get(MainActivity.this).deleteOauth(MainActivity.this, platforms.get(0).mPlatform, authListener);
            }
        });
        shareAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMImage image = new UMImage(MainActivity.this, "http://p2.so.qhimgs1.com/t019f0a9e88ea82026e.jpg");
                //得到音乐
                UMusic music = new UMusic("http://bd.kuwo.cn/yinyue/6406479?from=dq360");
                music.setTitle("This is music title");//音乐的标题
                music.setDescription("my description");//音乐的描述
               new ShareAction(MainActivity.this).setPlatform(SHARE_MEDIA.QQ)
                        .withText("hello")
                        .withMedia(image)
                        .setCallback(umShareListener)
                        .share();
                /*new ShareAction(MainActivity.this).withText("hello")
                        .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN)
                        .setCallback(umShareListener)
                        .withMedia(image)
                        .open();*/
            }
        });
    }
    //分享
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat","platform"+platform);
            Toast.makeText(MainActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(MainActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
                Log.d("throw","throw:"+t.getMessage());
            }
        }
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(MainActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
    //授权和初始化值
    private void initVariable() {
        authListener = new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
            }
            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

                switch (action){
                    case ACTION_AUTHORIZE:
                        log.setVisibility(View.GONE);
                        qqName.setVisibility(View.VISIBLE);
                        qqImg.setVisibility(View.VISIBLE);
                        UMShareAPI.get(MainActivity.this).getPlatformInfo(MainActivity.this, platforms.get(0).mPlatform, authListener);
                        Toast.makeText(MainActivity.this, "授权成功了", Toast.LENGTH_LONG).show();
                        break;
                    case ACTION_DELETE:
                        log.setVisibility(View.VISIBLE);
                        qqName.setVisibility(View.GONE);
                        qqImg.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "注销授权成功了", Toast.LENGTH_LONG).show();
                        break;
                    case ACTION_GET_PROFILE:
                        String naem = data.get("name");
                        String profile_image_url = data.get("profile_image_url");
                        Log.d("zzz", naem + "++++++" + profile_image_url);
                        ImageLoader.getInstance().displayImage(profile_image_url, qqImg);
                        qqName.setText(naem);
                        Toast.makeText(MainActivity.this, "获取信息成功了", Toast.LENGTH_LONG).show();
                        break;
                }
            }
            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                Toast.makeText(MainActivity.this, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                Toast.makeText(MainActivity.this, "取消了", Toast.LENGTH_LONG).show();
            }
        };
        initPlatforms();
    }
    private void initPlatforms() {
        platforms.clear();
        for (SHARE_MEDIA e : list) {
            if (!e.toString().equals(SHARE_MEDIA.GENERIC.toString())) {
                platforms.add(e.toSnsPlatform());
            }
        }
    }
    //回调的方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
