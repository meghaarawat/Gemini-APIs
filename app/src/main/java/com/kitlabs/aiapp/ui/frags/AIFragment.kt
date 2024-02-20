package com.kitlabs.aiapp.ui.frags

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import com.kitlabs.aiapp.R
import com.kitlabs.aiapp.adapter.ImageAdapter
import com.kitlabs.aiapp.adapter.QNAdapter
import com.kitlabs.aiapp.base.BaseFragment
import com.kitlabs.aiapp.base.MyPermission
import com.kitlabs.aiapp.base.PermissionManager
import com.kitlabs.aiapp.databinding.FragmentAIBinding
import com.kitlabs.aiapp.dialogs.SelectionMediaDialog
import com.kitlabs.aiapp.model.QNModel
import com.kitlabs.aiapp.others.Cons
import com.kitlabs.aiapp.others.FileHelper
import com.kitlabs.aiapp.others.MyUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class AIFragment : BaseFragment() {

    private val TAG = "AIFragment"
    private val binding by lazy { FragmentAIBinding.inflate(layoutInflater) }
    private lateinit var generativeModel: GenerativeModel
    private val permissionManager = PermissionManager.from(this)
    private val list : ArrayList<QNModel> = ArrayList()
    private val listImages : ArrayList<Bitmap> = ArrayList()
    private lateinit var adapter: QNAdapter
    private lateinit var adapterImages: ImageAdapter
    private var currentPhotoPath: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clicksHandler()
        setAdapter()
        MyUtils.viewVisible(binding.tvStartChat)
        MyUtils.viewInvisible(binding.rvChat)
    }

    private fun clicksHandler() {
        binding.ivDone.setOnClickListener {
            if(listImages.isEmpty()) {
                if(binding.etInput.text.toString().trim().isNotEmpty()) {
                    CoroutineScope(Dispatchers.Main).launch {
                        MyUtils.viewInvisible(binding.ivDone)
                        MyUtils.viewVisible(binding.loader)
                        binding.ivGallery.isClickable = false
                        binding.ivGallery.alpha = 0.5f
                        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(binding.etInput.windowToken, 0)
                        list.add(QNModel(Cons.QUES, binding.etInput.text.toString()))
                        generateResponse(binding.etInput.text.toString())
                    }
                }
                else {
                    Toast.makeText(context, "Query cannot left empty", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                if(binding.etInput.text.toString().trim().isNotEmpty()) {
                    CoroutineScope(Dispatchers.Main).launch {
                        MyUtils.viewInvisible(binding.ivDone)
                        MyUtils.viewVisible(binding.loader)
                        binding.ivGallery.isClickable = false
                        binding.ivGallery.alpha = 0.5f
                        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(binding.etInput.windowToken, 0)
                        val inputContent = content {
                            listImages.forEach { bitmap ->
                                image(bitmap)
                            }
                            text(binding.etInput.text.toString())
                        }
                        list.add(QNModel(Cons.QUES, binding.etInput.text.toString(), ArrayList(listImages)))
                        generateResponseFromImage(inputContent)
                    }
                }
                else {
                    Toast.makeText(context, "Query cannot left empty", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.ivGallery.setOnClickListener {
            SelectionMediaDialog(context = requireContext(),
                onCamera = {
                    permissionManager
                        .request(MyPermission.Camera)
                        .rationale("Camera Permission is required to capture an image")
                        .checkPermission { granted ->
                            if (granted) {
                                takePhotoFromCamera()
                            }
                        }
                },
                onGallery = {
                    if (Build.VERSION.SDK_INT >= 33) {
                        takePhotoFromGallery()
                    }
                    else {
                        permissionManager
                            .request(MyPermission.Storage)
                            .rationale("Storage Permissions are required to select an image")
                            .checkPermission { granted ->
                                if (granted) {
                                    takePhotoFromGallery()
                                }
                            }
                    }
                }
            ).show()
        }
    }

    private suspend fun generateResponse(query: String) {
        try {
            generativeModel = GenerativeModel(modelName = Cons.TEXT_ONLY_MODEL, apiKey = getString(R.string.api_key))
            binding.etInput.text.clear()
            listImages.clear()
            adapterImages.notifyDataSetChanged()
            val response = generativeModel.generateContent(query)
            list.add(QNModel(Cons.ANS, response.text.toString()))
            MyUtils.viewVisible(binding.rvChat)
            MyUtils.viewInvisible(binding.tvStartChat)
            adapter.notifyItemInserted(0)
            binding.rvChat.scrollToPosition(0)
            binding.ivGallery.isClickable = true
            binding.ivGallery.alpha = 1.0f
            MyUtils.viewVisible(binding.ivDone)
            MyUtils.viewInvisible(binding.loader)
        }
        catch (e: Exception) {
            hideLoader()
            Toast.makeText(context, "An error occurred, please try again later.", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun generateResponseFromImage(inputContent: Content) {
        try {
            generativeModel = GenerativeModel(modelName = Cons.TEXT_FROM_TEXT_IMAGE, apiKey = getString(R.string.api_key))
            binding.etInput.text.clear()
            listImages.clear()
            adapterImages.notifyDataSetChanged()
            val response = generativeModel.generateContent(inputContent)
            list.add(QNModel(Cons.ANS, response.text.toString()))
            MyUtils.viewVisible(binding.rvChat)
            MyUtils.viewInvisible(binding.tvStartChat)
            adapter.notifyItemInserted(0)
            binding.rvChat.scrollToPosition(0)
            binding.ivGallery.isClickable = true
            binding.ivGallery.alpha = 1.0f
            MyUtils.viewVisible(binding.ivDone)
            MyUtils.viewInvisible(binding.loader)
        }
        catch (e: Exception) {
            hideLoader()
            Toast.makeText(context, "An error occurred, please try again later.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        layoutManager.stackFromEnd = true
        binding.rvChat.layoutManager = layoutManager
        adapter = QNAdapter(list)
        binding.rvChat.adapter = adapter

        binding.rvImages.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapterImages = ImageAdapter(listImages)
        binding.rvImages.adapter = adapterImages
    }

    private fun takePhotoFromGallery() {
        val galleryIntent = if (Build.VERSION.SDK_INT >= 33) {
            Intent(MediaStore.ACTION_PICK_IMAGES)
        } else {
            Intent(Intent.ACTION_PICK)
        }
        galleryIntent.type = "image/*"
        resultLauncher.launch(galleryIntent)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let { uri ->
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                    listImages.add(bitmap)
                    adapterImages.notifyDataSetChanged()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun takePhotoFromCamera() {
        try {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            var photoFile: File? = null
            try {
                photoFile = FileHelper.createImageFile(requireContext())
                currentPhotoPath = photoFile.absolutePath
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(requireContext(), getString(R.string.authority), photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                cameraLauncher.launch(takePictureIntent)
            }
        }
        catch (e: Exception) {
            Log.d(TAG, "Issue in takePhotoFromGallery()")
            e.printStackTrace()
        }
    }

    private var cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
            listImages.add(bitmap)
            adapterImages.notifyDataSetChanged()
        }
        else Log.i(TAG, "Unable to capture image")
    }
}