package com.xuqingkai.common

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import com.blankj.utilcode.util.Utils

open class Config {
    companion object{
        open val DEBUG = getMetaData("DEBUG");
        open val API_HOST: String = getAPIHost();
        open val API_HOST_TEST = "http://www.400537.com/code/callback/";
        val WEIXIN_LOGIN_APPID = getMetaData("WEIXIN_LOGIN_APPID") + "";
        val WEIXIN_PAY_APPID = getMetaData("WEIXIN_PAY_APPID") + "";
        val WEIXIN_MINAPP_APPID = getMetaData("WEIXIN_MINAPP_APPID") + "";
        val WEIXIN_MINAPP_ORIGINID = getMetaData("WEIXIN_MINAPP_ORIGINID") + "";
        val TENCENT_MAP_APPKEY = getMetaData("TENCENT_MAP_APPKEY");
        val TENCENT_MAP_APPSECRET = getMetaData("TENCENT_MAP_APPSECRET");
        val TENCENT_VOICE_APPID = ("0" + getMetaData("TENCENT_VOICE_APPID")).toInt();
        val TENCENT_VOICE_SECRETID = getMetaData("TENCENT_VOICE_SECRETID");
        val TENCENT_VOICE_SECRETKEY = getMetaData("TENCENT_VOICE_SECRETKEY");
        val TENCENT_VOICE_SECRETKEY1 = getMetaData("TENCENT_VOICE_SECRETKEY1");

        open val REQUESTC_CODE_CAMERA = 2022110701;
        open val REQUEST_CODE_PERMISSION = 2022110702;

        open var PACKAGENAME_WEIXIN = "com.tencent.mm";
        open var PACKAGENAME_QQ = "com.tencent.mobileqq";
        open var PACKAGENAME_ALIPAY = "com.eg.android.AlipayGphone";
        open val IMAGE_PLACE_HOLDER = R.mipmap.image_placeholder;

        fun getAPIHost(): String {
            var host: String = getMetaData("API_HOST") + ""
            host = host.trim(' ').replace("\\", "/")
            if (host.endsWith("/")) {
                host = host.substring(0, host.length - 1)
            }
            return host
        }

        open fun getMetaData(key : String):String?{
            var application = Utils.getApp();
            return application.packageManager.getApplicationInfo(application.packageName, PackageManager.GET_META_DATA).metaData.getString(key);
        }

    }
}