package com.sharp.ambition.xposed;

import android.util.Log;

import de.robv.android.xposed.XposedBridge;

/**
 * date   : 2024/6/19 17:03
 * desc   :
 */
public interface ILogger {

    ILogger DEFAULT = new ILogger() {
        @Override
        public void i(String message) {
            XposedBridge.log("VoiceReadHook");
            Log.e("Logger", message);
        }
    };

    void i(String message);

}
