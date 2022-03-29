package com.github.deskid.xdebug;

import android.os.Process;


import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by deskidz on 12/5/16.
 */

public class XDebug implements IXposedHookLoadPackage, IXposedHookZygoteInit {
    private static final int DEBUG_ENABLE_DEBUGGER = 0x1;
    private XC_MethodHook debugAppsHook = new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(MethodHookParam param)
                throws Throwable {
            XposedBridge.log("-- beforeHookedMethod :" + param.args[1]);
            int id = 5;
            int flags = (Integer) param.args[id];
            if ((flags & DEBUG_ENABLE_DEBUGGER) == 0) {
                flags |= DEBUG_ENABLE_DEBUGGER;
            }
            param.args[id] = flags;
            if (BuildConfig.DEBUG) {
                XposedBridge.log("-- app debugable flags to 1 :" + param.args[1]);
            }
        }
    };

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
    }

    @Override
    public void initZygote(final IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
        XposedBridge.hookAllMethods(Process.class, "start", debugAppsHook);
    }
}
