package com.kitlabs.aiapp.dialogs

import android.content.Context
import android.os.Bundle
import com.kitlabs.aiapp.base.BaseDialog
import com.kitlabs.aiapp.databinding.DialogMediaSelectionTypeBinding

class SelectionMediaDialog(context: Context, private val onCamera: () -> Unit,
                           private val onGallery: () -> Unit) : BaseDialog(context) {

    private val binding by lazy { DialogMediaSelectionTypeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setDimBlur(window)

        binding.choosesCamera.setOnClickListener {
            onCamera()
            dismiss()
        }
        binding.choosesGallery.setOnClickListener {
            onGallery()
            dismiss()
        }
    }
}