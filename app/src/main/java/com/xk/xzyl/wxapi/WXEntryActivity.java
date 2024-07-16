package com.xk.xzyl.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * author : fengqiao
 * date   : 2022/1/4 14:20
 * desc   :
 */
public class WXEntryActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG", getIntent().toString());
    }
}
