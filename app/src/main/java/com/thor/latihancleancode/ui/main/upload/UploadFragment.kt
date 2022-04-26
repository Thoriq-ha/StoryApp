package com.thor.latihancleancode.ui.main.upload

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.thor.latihancleancode.R
import com.thor.latihancleancode.data.preferences.datastore.DataStoreSession
import com.thor.latihancleancode.databinding.FragmentUploadBinding
import com.thor.latihancleancode.ui.main.CameraActivity
import com.thor.latihancleancode.utils.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject
import java.io.File


class UploadFragment : Fragment(R.layout.fragment_upload) {
    private val binding by viewBinding(FragmentUploadBinding::bind)
    private val viewModel: UploadViewModel by inject()
    private val session: DataStoreSession by inject()
    private var getFile: File? = null
    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value
            }
            if (granted) {
                activity?.let {
                    val intent = Intent(it, CameraActivity::class.java)
                    launcherIntentCameraX.launch(intent)
                }
            } else {
                Toast.makeText(
                    requireContext(), "Belum  mendapatkan permission.", Toast.LENGTH_LONG
                ).show()
            }
        }

    companion object {
        const val CAMERA_X_RESULT = 200
        var PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonCamera.setOnClickListener { startCameraX() }
        binding.buttonGallery.setOnClickListener { startGallery() }
        binding.buttonUpload.setOnClickListener {
            val caption = binding.captionText.text.toString()
            if (caption.isNotEmpty()) uploadImage(caption) else {
                Toast.makeText(
                    requireContext(), "Caption belum ditambahkan.", Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.stateList.observe(viewLifecycleOwner, observerStateList)
        session.userFlow.asLiveData().observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.setToken("Bearer ${it.token}")
            }
        }
    }

    private val observerStateList = Observer<UploadState> {
        showLoading(it == UploadState.OnLoading)
        when (it) {
            is UploadState.OnSuccess -> {
                Toast.makeText(requireContext(), it.uploadResponse.message, Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(UploadFragmentDirections.actionUploadFragmentToHomeFragment())
            }
            is UploadState.OnError -> {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
            UploadState.OnLoading -> {}
        }
    }

    private fun uploadImage(caption: String) {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val description =
                caption.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            viewModel.uploadImage(imageMultipart, description)
        } else {
            Toast.makeText(requireContext(), "Gambar belum ditambahkan", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun startCameraX() {
        activity?.let {
            val intent = Intent(it, CameraActivity::class.java)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                launcherIntentCameraX.launch(intent)
            }
            if (hasPermissions(activity as Context, PERMISSIONS)) {
                launcherIntentCameraX.launch(intent)
            } else {
                permReqLauncher.launch(
                    PERMISSIONS
                )
            }
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            var uriFile: Uri? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                uriFile = saveImageInQ(result, requireContext())
            } else saveImageInLegacy(result, requireContext())
            val fileRotated = uriToFile(Uri.parse(uriFile.toString()), requireContext())
            getFile = fileRotated
            binding.imageView.setImageBitmap(result)
        }
    }


    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            getFile = myFile
            binding.imageView.setImageURI(selectedImg)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}