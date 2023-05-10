package com.xuqingkai.demo

import android.content.Context
import android.widget.ImageView
import com.stfalcon.imageviewer.StfalconImageViewer

class ImageZoom {
    private var mContext:Context
    private var mImages = mutableListOf<String>();
    private var mStfalconImageViewer: StfalconImageViewer<String>? = null;

    constructor(context:Context){
        mContext = context;
    }
    fun addImage(url:String){
        mImages.add(url);
    }
    fun intoImageView(imageView: ImageView){
        imageView.setOnClickListener(){
            mStfalconImageViewer = StfalconImageViewer.Builder<String>(mContext, mImages) { view, imageUrl ->
                Image(mContext).url(imageUrl).into(view);
                view.setOnClickListener(){mStfalconImageViewer?.close();}
            }.withStartPosition(mImages.size-1).show();
        }
    }
}