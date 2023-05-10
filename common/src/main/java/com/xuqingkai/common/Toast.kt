package com.xuqingkai.common

import android.content.Context
import com.hjq.toast.Toaster

class Toast {
    private val mContext: Context? = null;
    constructor(){}
    fun show(text: Any?){
        Toaster.show(text);
    }
}