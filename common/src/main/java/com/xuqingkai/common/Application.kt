package com.xuqingkai.common

import android.content.Context
import android.content.pm.PackageManager
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.blankj.utilcode.util.ToastUtils
import com.kongzue.dialogx.DialogX
import com.xuexiang.xutil.XUtil

open class Application: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate();
        DialogX.init(this);
        com.hjq.toast.ToastUtils.init(this);
        XUtil.init(this);
        com.blankj.utilcode.util.Utils.init(this);
    }
    override fun attachBaseContext(base: Context?) {
        MultiDex.install(this)
        super.attachBaseContext(base)
    }
}