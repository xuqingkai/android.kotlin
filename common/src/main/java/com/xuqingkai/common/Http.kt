package com.xuqingkai.common
import android.os.Handler
import android.text.TextUtils.substring
import android.util.Log
import android.view.View
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException


class Http {
    companion object{
        var API_HOST:String = "";
    }
    private var mOkHttpClient : OkHttpClient = OkHttpClient.Builder().build();
    private var mUrl = "";
    private var mMethod = "GET";
    private var mCharset =  "UTF-8";
    private var mSync = false;

    private var mRequestBuilder = Request.Builder();
    private var mHeader = mutableMapOf<String, String>();
    private var mQueryParam = mutableMapOf<String, MutableList<Any>>();
    private var mParam = mutableMapOf<String, MutableList<Any>>();
    private var mFiles = mutableMapOf<String, MutableList<File>>();
    private var mBody: String = "";
    private var mMultipartBody =  MultipartBody.Builder();
    private val handler = Handler ();

    constructor(){
        //addHeader("Content-Type", "application/x-www-form-urlencoded")
    }
    constructor(url:String?): this(){
        mUrl = if(url == null){
            API_HOST;
        }else if(url=="" || url.startsWith(" ") || url.endsWith(" ")){
            "http://www.400537.com/code/callback/";
        }else if(url.lowercase().startsWith("http://") || url.lowercase().endsWith("https://")){
            url;
        }else{
            API_HOST + url;
        }
    }
    constructor(method:String, url:String?): this(url){
        mMethod = method.uppercase();
    }

    fun method(method: String){
        mMethod = method.uppercase();
        if(mMethod == "JSON"){
            mMethod = "POST";
            mHeader["Content-Type"] = "application/json";
        }else if(mMethod == "XML"){
            mMethod = "POST";
            mHeader["Content-Type"] = "application/xml";
        }
    };
    fun charset(env: String){
        mCharset = env;
    }

    fun addHeader(key: String, value: Any): Http{
        mHeader[key] = value.toString();
        return this;
    }
    fun setHeader(key: String, value: Any): Http{
        addHeader(key, value);
        return this;
    }
    fun removeHeader(key: String, value: Any? = null): Http{
        mHeader.remove(key);
        return this;
    }
    fun addHeader(vararg pairs: Pair<String, Any>): Http{
        for ((key, value) in pairs) {
            addHeader(key, value.toString());
        }
        return this;
    }
    fun addBody(body: String): Http{
        mBody = body;
        return this;
    }
    fun addBody(json: JSONObject): Http{
        mBody = json.toString();
        mHeader["Content-Type"] = "application/json";
        return this;
    }
    fun addBody(json: JSONArray): Http{
        mBody = json.toString();
        mHeader["Content-Type"] = "application/json";
        return this;
    }

    fun addParam(key: String, value: Any): Http{
        if(mParam.containsKey(key)){
            mParam[key]?.add(value)
        }else{
            mParam[key] = mutableListOf(value);
        }
        return this;
    }
    fun addParam(vararg pairs: Pair<String, Any>): Http{
        for ((key, value) in pairs) { addParam(key, value); }
        return this;
    }
    fun setParam(key: String, value: Any): Http{
        mParam[key] = mutableListOf(value);
        return this;
    }
    fun setParam(params: MutableMap<String, Any>): Http{
        for ((key, value) in params) { setParam(key, value); }
        return this;
    }
    fun setParam(params: MutableList<Pair<String, Any>>): Http{
        for ((key, value) in params) { setParam(key, value); }
        return this;
    }
    fun removeParam(key: String, value: Any? = null): Http{
        if (value == null){
            mParam.remove(key);
        }else{
            mParam[key]?.remove(value);
            if(mParam[key]?.size==0){  mParam.remove(key); }
        }
        return this;
    }
    fun addParam(params: MutableMap<String, Any>): Http{
        for ((key, value) in params) { addParam(key, value); }
        return this;
    }
    fun addParam(params: MutableList<Pair<String, Any>>): Http{
        for ((key, value) in params) { addParam(key, value); }
        return this;
    }
    fun getParam(): String{
        var values = "";
        mParam.forEach {entry->
            mParam[entry.key]?.forEach {value->
                values += "&" + entry.key + "=" + java.net.URLEncoder.encode(value.toString(), mCharset);
            }
        }
        if(values.isNotEmpty()){values = values.substring(1);}
        return values;
    }

    fun getParam(key: String): Any?{
        return if(!mParam.containsKey(key)){
            null;
        }else if(mParam[key]?.size==1){
            mParam[key]?.get(0);
        }else{
            var values = "";
            mParam[key]?.forEach {value->
                values += ", $value";
            }
            values.substring(2);
        }
    }
    fun getParams(key: String): MutableList<Any>?{
        return if(!mParam.containsKey(key)){
            null;
        }else{
            mParam[key];
        }
    }
    fun getParamSignStr(sort: Boolean = true, allowEmpty: Boolean = false, onlyValue: Boolean =  false): String{
        var values = "";
        val sortedMap = mParam.entries.sortedBy { it.key; }.associateBy ({ it.key }, { it.value } )
        sortedMap.forEach {entry->
            sortedMap[entry.key]?.forEach {value->
                if(allowEmpty || value.toString().isNotEmpty()){
                    values += if(onlyValue){
                        value.toString();
                    }else{
                        "&" + entry.key + "=" + value.toString();
                    }
                }
            }
        }
        if(!onlyValue && values.isNotEmpty()){values = values.substring(1);}
        return values;
    }
    fun addQuery(key: String, value: Any): Http{
        if(mQueryParam.containsKey(key)){
            mQueryParam[key]?.add(value)
        }else{
            mQueryParam[key] = mutableListOf(value);
        }
        return this;
    }
    fun setQuery(key: String, value: Any): Http{
         mQueryParam[key] = mutableListOf(value);
        return this;
    }
    fun removeQuery(key: String, value: Any? = null): Http{
        if (value == null){
            mQueryParam.remove(key);
        }else{
            mQueryParam[key]?.remove(value);
            if(mQueryParam[key]?.size==0){  mQueryParam.remove(key); }
        }
        return this;
    }
    fun addQuery(vararg pairs: Pair<String, Any>): Http{
        for ((key, value) in pairs) { addQuery(key, value); }
        return this;
    }
    fun addQuery(params: MutableMap<String, Any>): Http{
        for ((key, value) in params) { addQuery(key, value); }
        return this;
    }
    fun addQuery(params: MutableList<Pair<String, Any>>): Http{
        for ((key, value) in params) { addQuery(key, value); }
        return this;
    }
    fun getQuery(): String{
        var values = "";
        mQueryParam.forEach {entry->
            mQueryParam[entry.key]?.forEach {value->
                values += "&" + entry.key + "=" + java.net.URLEncoder.encode(value.toString(), mCharset);
            }
        }
        if(values.isNotEmpty()){values = values.substring(1);}
        return values;
    }

    fun getQuery(key: String): Any?{
        return if(!mQueryParam.containsKey(key)){
            null;
        }else if(mQueryParam[key]?.size==1){
            mQueryParam[key]?.get(0);
        }else{
            var values = "";
            mQueryParam[key]?.forEach {value->
                values += ", $value";
            }
            values.substring(2);
        }
    }
    fun getQuerys(key: String): MutableList<Any>?{
        return if(!mQueryParam.containsKey(key)){
            null;
        }else{
            mQueryParam[key];
        }
    }

    fun addFile(key: String, file: File): Http{
        if(mFiles.containsKey(key)){
            mFiles[key]?.add(file)
        }else{
            mFiles[key] = mutableListOf(file);
        }
        return this;
    }
    fun setFile(key: String, file: File): Http{
        mFiles[key] = mutableListOf(file);
        return this;
    }
    fun removeFile(key: String, file: File? = null): Http{
        if (file == null){
            mFiles.remove(key);
        }else{
            mFiles[key]?.remove(file);
            if(mFiles[key]?.size==0){  mFiles.remove(key); }
        }
        return this;
    }
    fun addFile(vararg pairs: Pair<String, File>): Http{
        for ((key, file) in pairs) { addFile(key, file); }
        return this;
    }
    fun addFile(files: MutableList<Pair<String, File>>): Http{
        for ((key, file) in files) { addFile(key, file); }
        return this;
    }
    fun request(callback: (id: Int, code: String, message: String, data: String?) -> Unit?): Response?{
        var header = "";
        mHeader.keys.forEach {key->
            mRequestBuilder.addHeader(key, mHeader[key]);
            header += key + "=" + mHeader[key] + "&";
        }
        Log.i("xuqingkai/common/http()/request/Header", header);
        var requestBody: RequestBody? = null;
        if(mMethod == "GET"){
            if(mQueryParam.isEmpty() && mParam.isNotEmpty()){mQueryParam = mParam;}
            if(mQueryParam.isNotEmpty()){
                mUrl += if(mUrl.contains("?")){"&";}else{"?";};
                mQueryParam.forEach {entry->
                    mQueryParam[entry.key]?.forEach {value->
                        mUrl += entry.key + "=" + java.net.URLEncoder.encode(value.toString(), mCharset) + "&";
                    }
                }
            }
            mRequestBuilder.url(mUrl);
            Log.i("xuqingkai/common/http()/request/get", mUrl);
        }else{
            if(mQueryParam.isNotEmpty()){
                mUrl += if(mUrl.contains("?")){"&";}else{"?";};
                mQueryParam.forEach {entry->
                    mQueryParam[entry.key]?.forEach {value->
                        mUrl += entry.key + "=" + java.net.URLEncoder.encode(value.toString(), mCharset) + "&";
                    }
                }
            }
            mRequestBuilder.url(mUrl);
            Log.i("xuqingkai/common/http()/request/post", mUrl);
            var contentType = mHeader["Content-Type"]?.lowercase();
            if(contentType?.contains("/json") == true){
                if(mBody.isNullOrEmpty()){
                    mParam.forEach {entry->
                        var values: String= "";
                        mParam[entry.key]?.forEach {value-> values += ", " + value; }
                        values = values.substring(1);
                        mBody += ",\"${entry.key}\":\"${values}\"";
                    }
                    if(mBody != null){ mBody = (mBody + "").substring(1); }
                    mBody = "{$mBody}";
                }
                requestBody = FormBody.create(MediaType.parse(mHeader["Content-Type"] + "; charset=" + mCharset), mBody)
            }else if(contentType?.contains("/xml") == true){
                if(mBody.isNullOrEmpty()){
                    mBody += "<xml>";
                    mParam.forEach {entry->
                        mParam[entry.key]?.forEach {value->
                            mBody += "<${entry.key}><![CDATA[${value}]]></${entry.key}>";
                        }
                    }
                    mBody += "</xml>";
                }
                requestBody = FormBody.create(MediaType.parse(mHeader["Content-Type"] + "; charset=" + mCharset), mBody)
            }else if(mHeader["Content-Type"]?.lowercase()?.contains("multipart/form-data") == true){
                mMultipartBody.setType(MultipartBody.FORM);
                mParam.forEach {entry->
                    mParam[entry.key]?.forEach {value->
                        mMultipartBody.addFormDataPart(entry.key, value.toString());
                    }
                }
                mFiles.forEach {entry->
                    var mediaType = MediaType.parse("image/png")
                    mFiles[entry.key]?.forEach {file->
                        when(file.name.substring(file.name.lastIndexOf(".")).lowercase()){
                            ".png"->{ mediaType=MediaType.parse("image/png"); }
                            ".jpg"->{ mediaType=MediaType.parse("image/jpg"); }
                            ".bmp"->{ mediaType=MediaType.parse("image/bmp"); }
                            ".gif"->{ mediaType=MediaType.parse("image/gif"); }
                        }
                        mMultipartBody.addFormDataPart(entry.key, file.name, RequestBody.create(mediaType, file));
                    }
                }
                requestBody = mMultipartBody.build();
            }else{
                var formBody =  FormBody.Builder();
                mParam.forEach { entry ->
                    mParam[entry.key]?.forEach { value ->
                        formBody.add(entry.key, value.toString());
                        mBody += entry.key + "=" + value + "&";
                    }
                }
                requestBody = formBody.build();
            }
            Log.i("xuqingkai/common/http()/request/post", mBody ?: "");
        }

        mRequestBuilder.method(mMethod, requestBody);
        if(mSync){
            var response = mOkHttpClient.newCall(mRequestBuilder.build()).execute();
            var responseBody = response.body();
            var responseString= responseBody?.string() ?: "{}";
            Log.i("xuqingkai/common/Http()/request/responseString", responseString);
            if(response.isSuccessful) {
                var json = JSONObject(responseString);
                var id: Int = json.optInt("id", 0);
                var code: String = json.optString("code", "0");
                id = code.toInt();
                var message: String = json.getString("message");
                handler.post{ callback(id, code, message, responseString); }
            }else{
                handler.post{ callback(-1, "REQUEST_FAIL", "请求失败" + response.message(), null); }
            }

        }else{
            mOkHttpClient.newCall(mRequestBuilder.build()).enqueue(object : Callback {
                override fun onFailure(call: Call, ioException: IOException) {}
                override fun onResponse(call: Call,response: Response) {
                    var responseBody = response.body();
                    var responseString= responseBody?.string() ?: "{}";
                    Log.i("xuqingkai/common/Http()/request/responseString", responseString);
                    if(response.isSuccessful) {
                        var json = JSONObject(responseString);
                        var id: Int = json.optInt("id", 0);
                        var code: String = json.optString("code", "0");
                        id = code.toInt();
                        var message: String = json.getString("message");
                        handler.post{ callback(id, code, message, responseString); }
                    }else{
                        handler.post{ callback(-1, "REQUEST_FAIL", "请求失败:" + response.message(), null); }
                    }
                }
            });
        }
        return null;
    }
    fun get(callback: ((id: Int, code: String, message: String, data: String?) -> Unit?)? = null):Response?{
        mMethod = "GET";
        return callback?.let { request(it) };
    }
    fun post(callback: ((id: Int, code: String, message: String, data: String?) -> Unit?)? = null):Response?{
        mMethod = "POST";
        return callback?.let { request(it) };
    }
    fun json(callback: ((id: Int, code: String, message: String, data: String?) -> Unit?)? = null):Response?{
        mHeader["Content-Type"] = "application/json";
        return post(callback);
    }
    fun xml(callback: ((id: Int, code: String, message: String, data: String?) -> Unit?)? = null):Response?{
        mHeader["Content-Type"] = "application/xml";
        return post(callback);
    }
    fun upload(callback: ((id: Int, code: String, message: String, data: String?) -> Unit?)? = null):Response?{
        mHeader["Content-Type"] = "multipart/form-data";
        return post(callback);
    }
}