package com.p.fiveminutefriend

import android.graphics.Bitmap
import android.view.animation.Transformation

class BitmapScalingTool(private val size: Int,
                        private val isHeightScale: Boolean) : Transformation() {

    fun transform(source: Bitmap): Bitmap? {
        if(isHeightScale) {
            val scale : Float = size.toFloat()/source.height
            val newSize = Math.round(source.width*scale)
            val scaledBitmap = Bitmap.createScaledBitmap(source, newSize, size, true)
            if(scaledBitmap!=source) {
                source.recycle()
            }
            return scaledBitmap
        }
        else {
            val scale : Float = size.toFloat()/source.width
            val newSize = Math.round(source.height*scale)
            val scaledBitmap = Bitmap.createScaledBitmap(source, newSize, size, true)
            if(scaledBitmap!=source) {
                source.recycle()
            }
            return scaledBitmap
        }
    }

}
