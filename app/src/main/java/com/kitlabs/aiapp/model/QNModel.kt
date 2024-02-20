package com.kitlabs.aiapp.model

import android.graphics.Bitmap

data class QNModel(
    val type: String,
    val data: String,
    val bitmapList: ArrayList<Bitmap>? = null
)
