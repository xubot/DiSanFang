package com.example.administrator.qqlog.Util;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by Administrator on 2017/3/13.
 */

public class App extends Application {
    {
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }
    public void onCreate() {
        super.onCreate();
        UMShareAPI.get(this);
        ImageLoaderConfiguration build = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(build);
    }
   /* {
        PlatformConfig.setQQZone("1106036236", "mjFCi0oxXZKZEWJs");
    }*/
}
