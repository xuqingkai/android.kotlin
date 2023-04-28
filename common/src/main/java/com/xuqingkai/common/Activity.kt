package com.xuqingkai.common

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Http.API_HOST = Config.API_HOST;
    }
    open fun getMetaData(key:String): String?{
        var info = this.packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
        return info.metaData.getString(key);
    }
}