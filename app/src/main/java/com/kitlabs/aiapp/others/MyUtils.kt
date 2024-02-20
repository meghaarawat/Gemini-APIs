package com.kitlabs.aiapp.others

import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MyUtils {
    val min = 0
    val max = 3


    fun viewGone(view: View?) {
        if (view != null) {
            view.visibility = View.GONE

        }
    }

    fun viewInvisible(view: View?) {
        if (view != null && view.visibility == View.VISIBLE) {
            view.visibility = View.INVISIBLE
        }
    }

    fun viewVisible(view: View?) {
        if (view != null && (view.visibility == View.INVISIBLE || view.visibility == View.GONE)) {
            view.visibility = View.VISIBLE

        }
    }

    fun isEmptyString(value: String?): Boolean {
        return TextUtils.isEmpty(value) || TextUtils.isEmpty(value?.trim())
    }

    companion object {

        fun viewGone(view: View?) {
            if (view != null) {
                view.visibility = View.GONE
            }
        }

        fun viewsGone(view: List<View?>?) {
            if (!view.isNullOrEmpty()) {
                for (item in view) {
                    viewGone(item)
                }
            }
        }

        fun viewsVisible(view: List<View?>?) {
            if (!view.isNullOrEmpty()) {
                for (item in view) {
                    viewVisible(item)
                }
            }
        }

        fun viewInvisible(view: View?) {
            if (view != null) {
                view.visibility = View.INVISIBLE

            }
        }

        fun viewVisible(view: View?) {
            if (view != null && (view.visibility == View.INVISIBLE || view.visibility == View.GONE)) {
                view.visibility = View.VISIBLE

            }
        }

        fun isEmptyString(value: String?): Boolean {
            return TextUtils.isEmpty(value) || TextUtils.isEmpty(value?.trim())
        }

        fun contains(value: String?, match: String?): Boolean {
            return !isEmptyString(value) && !isEmptyString(match) && value!!.contains(match!!)
        }

        fun setText(view: TextView?, value: String?) {
            viewVisible(view)
            if (view != null) {
                if (value != null && value != "") {
                    view.text = Html.fromHtml(value, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    view.text = "--"
                }
            }
        }

        fun isValidPassword(password: String): Boolean {
            val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{7,}\$".toRegex()
            return passwordPattern.matches(password)
        }

        fun isValidEmail(email: String): Boolean {
            val emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$".toRegex()
            return emailPattern.matches(email)
        }

        fun capitalize(input: String): String {
            val words = input.lowercase().split(" ").toTypedArray()
            val builder = java.lang.StringBuilder()
            for (i in words.indices) {
                val word = words[i]
                if (i > 0 && word.isNotEmpty()) {
                    builder.append(" ")
                }
                val cap = word.substring(0, 1).uppercase() + word.substring(1)
                builder.append(cap)
            }
            return builder.toString()
        }

        fun hideStatusBar(window: Window) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val decorView = window.decorView
                val insetsController = decorView.windowInsetsController
                insetsController?.hide(WindowInsets.Type.statusBars())
            } else {
                window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
        }

        fun estToLocal(datesToConvert: String?): String? {
            val dateFormatInPut: String = "yyyy-MM-dd HH:mm:ss"
            val dateFormatOutPut: String = "yyyy-MM-dd HH:mm:ss"
            var dateToReturn = datesToConvert
            val sdf = SimpleDateFormat(dateFormatInPut)
            sdf.timeZone = TimeZone.getTimeZone("America/New_York")
            var gmt: Date? = null
            val sdfOutPutToSend = SimpleDateFormat(dateFormatOutPut)
            sdfOutPutToSend.timeZone = TimeZone.getDefault()
            try {
                gmt = sdf.parse(datesToConvert)
                dateToReturn = sdfOutPutToSend.format(gmt)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return dateToReturn
        }

    }
}
