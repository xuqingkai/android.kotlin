package com.xuqingkai.common

import android.content.Context
import android.graphics.*
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.squareup.picasso.Picasso
import com.stfalcon.imageviewer.StfalconImageViewer
import jp.wasabeef.glide.transformations.BitmapTransformation as GlideBitmapTransformation
import java.security.MessageDigest
import com.squareup.picasso.Transformation as PicassoTransformation

class Image {

    private var mContext: Context;
    private var mRadius : Any = 0;
    private var mResID : Int;
    private var mWidth : Int = 0;
    private var mHeight : Int = 0;

    constructor(context: Context) {
        mContext = context;
        mResID = Config.IMAGE_PLACE_HOLDER
    }
    private var Url : String? = null;
    fun url(imgUrl: String?): Image{
        Url = imgUrl
        return this;
    }
    fun resize(width: Int,height: Int): Image{
        mWidth = width
        mHeight = height
        return this;
    }
    fun placeHolder(resID: Int): Image{
        mResID = resID
        return this;
    }
    fun circle(radius: Any="50%"):Image{
        mRadius = radius
        return this;
    }
    fun into(imageView: ImageView){
        intoGlide(imageView);
    }




    fun intoPicasso(imageView: ImageView){
        var picasso = Picasso.get().load(Url).placeholder(mResID);
        if(mWidth>0 && mHeight>0){ picasso.resize(mWidth,mHeight); }
        if(mRadius != null){
            picasso.transform(PicassoCircleTransform(mRadius, mWidth, mHeight))
        }
        picasso.into(imageView);

    }
    fun intoGlide(imageView: ImageView){
        var glide = Glide.with(mContext).load(Url).placeholder(mResID);
        if(mWidth>0 && mHeight>0){ glide.override(mWidth, mHeight).centerCrop(); }
        if(mRadius != null){
            glide.transform(MultiTransformation(
                GlideCircleTransform(mRadius, mWidth, mHeight)
            ));
        }
        glide.into(imageView);
    }



    private class PicassoCircleTransform : PicassoTransformation {
        private var mWidth : Int = 0;
        private var mHeight : Int = 0;
        private var mRadius: Any = 0
        constructor() {

        }
        constructor(radius:Any,width: Int,height: Int):this(){
            mWidth = width
            mHeight = height
            mRadius = radius;
        }
        override fun transform(source: Bitmap): Bitmap? {
            var width = source.width;
            var height = source.height;
            if(mWidth>0 && mHeight>0){ width = mWidth; height = mHeight; }
            val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)

            var radius = if (mRadius.toString().endsWith("%")) {
                val percent: Float = mRadius.toString().replace("%", "").toFloat() / 100
                width.coerceAtMost(height) * percent;
            } else {
                mRadius.toString().toFloat()
            }

            val paint = Paint()
            paint.isAntiAlias = true
            paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            canvas.drawRoundRect(RectF(0F, 0F, width.toFloat(), height.toFloat()), radius, radius, paint)
            if (source != output) {
                source.recycle()
            }
            return output
        }

        override fun key(): String? {
            return "circle"
        }
    }


    private class GlideCircleTransform : GlideBitmapTransformation {
        private val ID = "jp.wasabeef.glide.transformations.GlideCircleTransform.1"
        private val ID_BYTES = ID.toByteArray(CHARSET)
        private var mWidth : Int = 0;
        private var mHeight : Int = 0;
        private var mRadius: Any = 0

        constructor() { }
        constructor(radius:Any,width: Int,height: Int):this(){
            mWidth = width
            mHeight = height
            mRadius = radius;
        }

        override fun transform(context: Context, bitmapPool: BitmapPool, source: Bitmap, outWidth: Int, outHeight: Int): Bitmap {

            var width = source.width;
            var height = source.height;
            if(mWidth>0 && mHeight>0){ width = mWidth; height = mHeight; }
            val output = bitmapPool[width, height, Bitmap.Config.ARGB_8888];
            output.setHasAlpha(true);

            val canvas = Canvas(output);
            var radius = if (mRadius.toString().endsWith("%")) {
                val percent: Float = mRadius.toString().replace("%", "").toFloat() / 100;
                width.coerceAtMost(height) * percent;
            } else {
                mRadius.toString().toFloat()
            }
            val paint = Paint()
            paint.isAntiAlias = true
            paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            canvas.drawRoundRect(RectF(0F, 0F, width.toFloat(), height.toFloat()), radius, radius, paint)
            return output
        }

        override fun equals(other: Any?): Boolean {
            return other is GlideCircleTransform
        }

        override fun hashCode(): Int {
            return ID.hashCode()
        }

        override fun updateDiskCacheKey(messageDigest: MessageDigest) {
            messageDigest.update(ID_BYTES)
        }


    }
}