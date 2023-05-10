package com.xuqingkai.demo

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.donkingliang.imageselector.utils.ImageSelector
import com.xuqingkai.common.Config

class Photo {
    private lateinit var mContext : Context;
    private lateinit var mActivity : Activity;
    private lateinit var mFragment: Fragment;
    constructor(context: Context) {
        mContext = context;
    }
    fun camera(activity: Activity, maxSelectCount:Int = 0, RequestCode: Int = Config.REQUESTC_CODE_CAMERA){
        ImageSelector.builder()
            .useCamera(true) // 设置是否使用拍照
            .setSingle(true)  //设置是否单选
            .setMaxSelectCount(maxSelectCount) // 图片的最大选择数量，小于等于0时，不限数量。
            //.setSelected(true) // 把已选的图片传入默认选中
            .canPreview(true) //是否可以预览图片，默认为true
            .start(activity, RequestCode); // 打开相册
    }

}