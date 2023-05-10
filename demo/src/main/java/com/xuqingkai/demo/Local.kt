package com.xuqingkai.demo

import android.content.Context
import com.tencent.mmkv.MMKV
import org.json.JSONArray
import org.json.JSONObject

open class Local {
    lateinit var mContext: Context;
    constructor(){
    }
    constructor(context:Context){
        mContext = context;
        MMKV.initialize(mContext);
    }
    companion object{
        private var mMMKV:MMKV = MMKV.defaultMMKV();
        open fun remove(key: String){
            mMMKV.remove(key);
        }
        fun setBool(key: String, value: Boolean){
            mMMKV.putBoolean(key, value);
        }
        fun getBool(key: String): Boolean{
            return mMMKV.getBoolean(key, false);
        }
        fun setInt(key: String, value: Int){
            mMMKV.putInt(key, value);
        }
        fun getInt(key: String): Int{
            return mMMKV.getInt(key, 0);
        }
        fun setFloat(key: String, value: Float){
            mMMKV.putFloat(key, value);
        }
        fun getFloat(key: String): Float{
            return mMMKV.getFloat(key, 0F);
        }
        fun setString(key: String, value: String){
            mMMKV.putString(key, value);
        }
        fun getString(key: String): String?{
            return mMMKV.getString(key, null);
        }
        fun addJSONArray(key: String, value: String){
            mMMKV.putString(key, value.toString());
        }
        fun setJSONArray(key: String, value: JSONArray){
            mMMKV.putString(key, value.toString());
        }
        fun getJSONArray(key: String): JSONArray {
            var jsonArray = mMMKV.getString(key, null) ?: "[]";
            return JSONArray(jsonArray);
        }
        fun addJSONObject(key: String, value: JSONObject){
            mMMKV.putString(key, value.toString());
        }
        fun setJSONObject(key: String, value: JSONObject){
            mMMKV.putString(key, value.toString());
        }
        fun getJSONObject(key: String): JSONObject {
            var jsonObject = mMMKV.getString(key, null) ?: "{}";
            return JSONObject(jsonObject);
        }
    }
    fun remove(key: String){
        mMMKV.remove(key);
    }
    fun setInt(key: String, value: Int): Local {
        mMMKV.putInt(key, value);
        return this;
    }
    fun getInt(key: String): Int{
        return mMMKV.getInt(key, 0);
    }
    fun setFloat(key: String, value: Float): Local {
        mMMKV.putFloat(key, value);
        return this;
    }
    fun getFloat(key: String): Float{
        return mMMKV.getFloat(key, 0F);
    }
    fun setString(key: String, value: String): Local {
        mMMKV.putString(key, value);
        return this;
    }
    fun getString(key: String): String?{
        return mMMKV.getString(key, null);
    }
    fun addJSONArray(key: String, value: String): Local {
        mMMKV.putString(key, value.toString());
        return this;
    }
    fun setJSONArray(key: String, value: JSONArray): Local {
        mMMKV.putString(key, value.toString());
        return this;
    }
    fun getJSONArray(key: String): JSONArray {
        var jsonArray = mMMKV.getString(key, null) ?: "[]";
        return JSONArray(jsonArray);
    }
    fun addJSONObject(key: String, value: JSONObject): Local {
        mMMKV.putString(key, value.toString());
        return this;
    }
    fun setJSONObject(key: String, value: JSONObject): Local {
        mMMKV.putString(key, value.toString());
        return this;
    }
    fun getJSONObject(key: String): JSONObject {
        var jsonObject = mMMKV.getString(key, null) ?: "{}";
        return JSONObject(jsonObject);
    }
}