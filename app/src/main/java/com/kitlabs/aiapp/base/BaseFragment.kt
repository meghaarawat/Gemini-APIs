package com.kitlabs.aiapp.base

import android.app.Dialog
import android.view.View
import androidx.fragment.app.Fragment
import com.kitlabs.aiapp.others.Loader
import com.kitlabs.aiapp.others.MyUtils

open class BaseFragment : Fragment() {

    private lateinit var dialog: Dialog
    private lateinit var progressBar: View
    private lateinit var mainView: View
    private var loader: Loader? = null

    protected fun showLoader() {
       try{
           loader = null
           loader = context?.let { Loader(it) }
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

    protected fun showProgressBar() {
        MyUtils.viewGone(mainView)
        MyUtils.viewVisible(progressBar)

    }

    protected fun hideProgressBar() {
        MyUtils.viewGone(progressBar)
        MyUtils.viewVisible(mainView)
    }


    override fun onDestroy() {
        super.onDestroy()
        hideLoader()
    }
}