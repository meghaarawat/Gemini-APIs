package com.kitlabs.aiapp.others

import android.content.Context
import android.os.Bundle
import android.view.Window
import com.kitlabs.aiapp.R
import com.kitlabs.aiapp.base.BaseDialog

class Loader(context: Context) : BaseDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_loading)
        setDimBlur(window)
    }
}