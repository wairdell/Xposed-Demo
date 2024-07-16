package com.sharp.ambition.xposed;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import com.google.gson.Gson;

/**
 * author : fengqiao
 * date   : 2021/12/10 12:02
 * desc   :
 */
public class VoiceReadHook implements IXposedHookLoadPackage {


    private final Gson gson;
    private ILogger logger = ILogger.DEFAULT;

    public VoiceReadHook() {
        XposedBridge.log("VoiceReadHook");
        gson = new Gson();
    }


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        logger.i("Xposed handleLoadPackage " + lpparam.packageName);
        List<String> packageList = Arrays.asList("com.zhuoyixia.speech.voicereadsdk",
                "com.xk.xzyl",
                "com.duoku.yuetu",
                "com.example.vscode_import",
                "com.xzzq.xiaozhuo",
                "com.speech.voicereadsdk.demo");
        if (packageList.contains(lpparam.packageName)) {
            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);

                    Context context = (Context) param.args[0];
                    Toast.makeText(context, "afterHookedMethod", Toast.LENGTH_SHORT).show();
                    logger.i("attach Xposed afterHookedMethod");

                    ClassLoader classLoader = ((Context) param.args[0]).getClassLoader();
                    File externalCacheDir = Environment.getExternalStorageDirectory();
                    File file = new File(externalCacheDir, "log.txt");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
                    logger = new ILogger() {
                        @Override
                        public void i(String message) {
                            ILogger.DEFAULT.i(message);
                            try {
                                bufferedWriter.append(message);
                                bufferedWriter.flush();
                                bufferedWriter.newLine();
                            } catch (Exception e) {
                            }
                        }
                    };

                    logger.i("attach Xposed afterHookedMethod => " + lpparam.packageName);

                    Class<?> dialogClazz = classLoader.loadClass("android.app.Dialog");
                    //XposedBridge.hookMethod(dialogClazz.getDeclaredMethod("show"))
                    /*XposedHelpers.findAndHookMethod(dialogClazz, "show", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            XposedBridge.log("dialog show beforeHookedMethod");
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            Method dismissMethod = dialogClazz.getDeclaredMethod("dismiss");
                            dismissMethod.invoke(param.thisObject);
                            XposedBridge.log("dialog show afterHookedMethod");
                        }
                    });*/
//                    hookEmulator(classLoader);
                    try {
                        String className = "com.xlx.speech.voicereadsdk.entrance.SpeechVoiceManager";
                        Class<?> clazz = classLoader.loadClass(className);

                        hookInit(clazz, classLoader);
                        hookLoadVoiceAd(clazz, classLoader);
                    } catch (Throwable e) {

                    }

                    try {
                        hookStubApp(classLoader);
                    } catch (Throwable e) {
//                        bufferedWriter.append(e.toString());
//                        bufferedWriter.flush();
                    }
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);

                }
            });
        }
    }

    private void hookEmulator(ClassLoader classLoader) {
        XposedHelpers.findAndHookMethod("com.blankj.utilcode.util.DeviceUtils", classLoader, "isEmulator", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("DeviceUtils isEmulator beforeHookedMethod");
                super.beforeHookedMethod(param);
                param.setResult(false);
            }
        });
        XposedHelpers.findAndHookMethod("com.snail.antifake.jni.EmulatorDetectUtil", classLoader, "a", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("EmulatorDetectUtil a beforeHookedMethod");
                super.beforeHookedMethod(param);
                param.setResult(false);
            }
        });

        XposedHelpers.findAndHookMethod("com.duoku.yuetu.utils.v", classLoader, "b", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("utils.v.b beforeHookedMethod");
                super.beforeHookedMethod(param);
                param.setResult(false);
            }
        });

        XposedHelpers.findAndHookMethod("com.duoku.yuetu.utils.extension.d", classLoader, "e", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("e beforeHookedMethod");
                super.beforeHookedMethod(param);
                param.setResult(false);
            }
        });

        XposedHelpers.findAndHookMethod("com.duoku.yuetu.utils.extension.d", classLoader, "a", Context.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("a beforeHookedMethod");
                super.beforeHookedMethod(param);
                param.setResult("");
            }
        });

        XposedHelpers.findAndHookMethod("com.duoku.yuetu.utils.extension.d", classLoader, "b", Context.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("b beforeHookedMethod");
                super.beforeHookedMethod(param);
                param.setResult(false);
            }
        });

//        Class<?> emulatorDetectUtil classLoader.loadClass("com.snail.antifake.jni.EmulatorDetectUtil");
    }


    private void hookInit(Class clazz, ClassLoader classLoader) throws ClassNotFoundException {
        Class<?> voiceConfigClazz = classLoader.loadClass("com.xlx.speech.voicereadsdk.bean.VoiceConfig");
        XposedHelpers.findAndHookMethod(clazz, "init", Context.class, voiceConfigClazz, new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Context context = (Context) param.args[0];
                StringBuilder builder = new StringBuilder();
                try {
                    for (int i = 1; i < param.args.length; i++) {
                        builder.append("arg[").append(i).append("] = " ).append(gson.toJson(param.args[i])).append(",");
                    }
                } catch (Throwable throwable) {
                    logger.i(throwable.toString());
                }

                logger.i("init => " + builder);
                Toast.makeText(context, "afterHookedMethod", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hookLoadVoiceAd(Class clazz, ClassLoader classLoader) throws ClassNotFoundException {
        Class<?> adSlotClazz = classLoader.loadClass("com.xlx.speech.voicereadsdk.bean.AdSlot");
        Class<?> listenerClazz = classLoader.loadClass("com.xlx.speech.voicereadsdk.entrance.VoiceAdLoadListener");
        XposedHelpers.findAndHookMethod(clazz, "loadVoiceAd", Context.class, adSlotClazz, listenerClazz, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                XposedBridge.log("loadVoiceAd Xposed beforeHookedMethod");
                StringBuilder builder = new StringBuilder();
                try {
                    for (int i = 1; i < param.args.length - 1; i++) {
                        builder.append("arg[").append(i).append("] = " ).append(gson.toJson(param.args[i])).append(",");
                    }
                } catch (Throwable e) {
                    logger.i(e.toString());
                }
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                for (StackTraceElement traceElement : stackTrace) {
                    logger.i(traceElement.toString());
                }
                logger.i("loadVoiceAd => " + builder);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                logger.i("loadVoiceAd Xposed afterHookedMethod");
            }
        });
    }

    private void hookStubApp(ClassLoader classLoader) throws ClassNotFoundException, IOException {
        Class<?> stubClass = classLoader.loadClass("com.stub.StubApp");
        XposedHelpers.findAndHookMethod(stubClass, "attachBaseContext", Context.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                logger.i("attachBaseContext beforeHookedMethod");
            }
        });
        XposedHelpers.findAndHookMethod(stubClass, "a", String.class, Boolean.TYPE, Boolean.TYPE, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < param.args.length; i++) {
                    builder.append("arg[").append(i).append("] = " ).append(gson.toJson(param.args[i])).append(",");
                }
                param.setResult("libjiagu.so");

                logger.i("hookStubApp beforeHookedMethod => " + builder);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                XposedBridge.log("hookStubApp afterHookedMethod => ");
            }
        });
        logger.i("hookStubApp called");
    }

}
