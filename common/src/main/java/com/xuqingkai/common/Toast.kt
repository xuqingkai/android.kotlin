package com.xuqingkai.common

import android.content.Context
import com.hjq.toast.ToastUtils

class Toast {
    private val mContext: Context? = null;
    constructor(){}
    constructor(context: Context)

    fun show(text: Any?){
        ToastUtils.show(text);
    }
}