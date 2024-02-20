package com.kitlabs.aiapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.kitlabs.aiapp.base.BaseActivity
import com.kitlabs.aiapp.databinding.ActivitySplashBinding
import com.kitlabs.aiapp.ui.MainActivity

class Splash : BaseActivity() {

    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@Splash, MainActivity::class.java))
            finish()
        }, 3000)
    }

}