package com.xuqingkai.common

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.xuqingkai.common.Config.Companion.REQUEST_CODE_PERMISSION


class Permission {
    companion object{
        val CAMERA = android.Manifest.permission.CAMERA
        val READ_SMS = android.Manifest.permission.READ_SMS
        val ACCESS_COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION
        val ACCESS_FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION
        val READ_CONTACTS = android.Manifest.permission.READ_CONTACTS
        val RECORD_AUDIO = android.Manifest.permission.RECORD_AUDIO
        val MANAGE_EXTERNAL_STORAGE = android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
        val READ_EXTERNAL_STORAGE = android.Manifest.permission.READ_EXTERNAL_STORAGE
        val WRITE_EXTERNAL_STORAGE = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    }
    private var mContext: Context? = null;
    private var mActivity: AppCompatActivity? = null;
    private var mPermissions = mutableListOf<String>();
    constructor(){
    }
    constructor(context: Context):this(){
        mContext = context;
    }
    constructor(context: Context, activity: AppCompatActivity): this(context){
        mActivity = activity;
    }
    fun add(permission: String): Permission{
        mPermissions.add(permission);
        return this;
    }

    fun check(): Boolean?{
        return if(XXPermissions.isGranted(mContext, mPermissions)) {
            true;
        }else{
            if(XXPermissions.isPermanentDenied(mActivity, mPermissions)){
                false;
            }else{
                null;
            }
        }
    }

    fun request(allow:(all: Boolean)->Unit, refuse:(never: Boolean)->Unit){
        if(check() != true){
            XXPermissions.with(mContext).permission(mPermissions).request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, all: Boolean) { allow(all); }
                override fun onDenied(permissions: MutableList<String>, never: Boolean) { refuse(never); }
            })
            return;
        }
    }
}