package com.xuqingkai.common

import android.content.Context
import com.xuqingkai.common.Config.Companion.WEIXIN_LOGIN_APPID
import com.xuqingkai.common.Config.Companion.WEIXIN_MINAPP_APPID
import com.xuqingkai.common.Config.Companion.WEIXIN_MINAPP_ORIGINID
import com.xuqingkai.common.Utils.Companion.dateTime
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.xuexiang.xutil.display.ImageUtils


class OAuth {
    lateinit var mContext: Context;
    private var mWeixinAppID = "";
    private var mWeixinMinAppAppID = "";
    private var mWeixinMinAppOriginID = "";

    constructor(context: Context) {
        mContext = context
        mWeixinAppID = WEIXIN_LOGIN_APPID
        mWeixinMinAppAppID = WEIXIN_MINAPP_APPID
        mWeixinMinAppOriginID = WEIXIN_MINAPP_ORIGINID
    }
    fun weixin(appID:String):OAuth{
        mWeixinAppID = appID;
        return this;
    }
    fun weixinMinApp(appID:String, originID:String):OAuth{
        mWeixinMinAppAppID = appID;
        mWeixinMinAppOriginID = originID;
        return this;
    }

    fun login(type:String, state: String = ""){
        var api = WXAPIFactory.createWXAPI(mContext, mWeixinAppID);
        var req = SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = state;
        api.sendReq(req);
    }
    fun share(type:String, title:String, description:String, url: String = ""){
        var api = WXAPIFactory.createWXAPI(mContext, mWeixinAppID);
        val webpage = WXWebpageObject()
        webpage.webpageUrl = url
        val msg = WXMediaMessage(webpage)
        msg.title = title
        msg.description = description

        val req = SendMessageToWX.Req()
        req.transaction = dateTime("yyyyMMddHHmmss");
        req.message = msg
        req.scene = SendMessageToWX.Req.WXSceneSession
        //req.userOpenId = getOpenId()
        api.sendReq(req);
    }
    fun toMinApp(path: String? = null, type: String = "weixin", originID:String? = null){
        var api = WXAPIFactory.createWXAPI(mContext, mWeixinAppID);
        val req = WXLaunchMiniProgram.Req()
        req.userName = originID ?: mWeixinMinAppOriginID // 填小程序原始id
        if(!path.isNullOrEmpty()){
            req.path = path ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
        }
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE // 可选打开 开发版，体验版和正式版
        api.sendReq(req)
    }
}