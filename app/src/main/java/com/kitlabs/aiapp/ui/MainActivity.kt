package com.kitlabs.aiapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import com.kitlabs.aiapp.base.BaseActivity
import com.kitlabs.aiapp.databinding.ActivityMainBinding
import com.kitlabs.aiapp.others.Toaster
import com.kitlabs.aiapp.ui.frags.AIFragment

class MainActivity : BaseActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().add(binding.container.id, AIFragment()).commit()
    }

    private var backPressTime: Long = 0
    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (System.currentTimeMillis() - backPressTime <= 200) {
            finish()
            finishAffinity()
        } else {
            Toaster.shortToast("Press twice to exit")
            backPressTime = System.currentTimeMillis()
        }
    }

}