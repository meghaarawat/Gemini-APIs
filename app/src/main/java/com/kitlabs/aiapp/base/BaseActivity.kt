package com.kitlabs.aiapp.base

import androidx.appcompat.app.AppCompatActivity
import com.kitlabs.aiapp.others.Loader

abstract class BaseActivity : AppCompatActivity() {

    private var loader: Loader? = null

    protected fun showLoader() {
        try{
            loader = null
            loader = Loader(this)
            if (loader != null && !loader?.isShowing!!) {
                loader?.show()
            }
        }
        catch(e :Exception){
            e.printStackTrace()
        }
    }

    protected fun hideLoader() {
        if (loader != null && loader?.isShowing!!) {
            loader?.dismiss()
        }
        loader = null
    }

}