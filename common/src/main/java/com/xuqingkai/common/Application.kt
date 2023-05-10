package com.xuqingkai.common

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.kongzue.dialogx.DialogX

open class Application: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate();
        DialogX.init(this);
        com.blankj.utilcode.util.Utils.init(this);
    }
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }
}