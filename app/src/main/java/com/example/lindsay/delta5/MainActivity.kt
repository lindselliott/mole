package com.example.lindsay.delta5

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.EXTRA_OUTPUT
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.Toast
import com.example.lindsay.delta5.Fragments.MoleInfoFragment
import com.example.lindsay.delta5.Fragments.NewMoleFragment
import com.example.lindsay.delta5.utils.ImageUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1

    private var mImageUri: Uri? = null
    private var filePath: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = NewMoleFragment()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {

                var imageFile: File? = null
                var selectedImage: Uri? = null


                Log.d("deltahacks", "image taken ")
                //Log.d("deltahacks", filePath)


                if (resultCode == Activity.RESULT_OK) {

                    if (mImageUri != null) {
                        Log.d("CAMERA_TEST", "revoke permissions")
                        revokeUriPermission(mImageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

                        imageFile = File(filePath)

                    } else {
                        selectedImage = data?.data

                        try {
                            val inStream = contentResolver.openInputStream(selectedImage!!)
                            imageFile = ImageUtils.createImageFile(this, "jpeg")

                            val os = FileOutputStream(imageFile)

                            val buffer = ByteArray(1000)
                            while (inStream != null && inStream.read(buffer, 0, buffer.size) >= 0) {
                                os.write(buffer, 0, buffer.size)
                            }
                            os.close()
                            inStream?.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: NullPointerException) {
                            e.printStackTrace()
                        }
                    }


                    // use imageFile

                    if (imageFile != null) {
                        Log.d("deltahacks", "image exists ")

                    }


                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    val fragment = MoleInfoFragment()
                    fragmentTransaction.replace(R.id.fragment_container, fragment)
                    fragmentTransaction.commit()




                    filePath = null
                    mImageUri = null
                    return
                }

            }
        }
        if (resultCode == RESULT_CANCELED && filePath != null) {
            Log.d("deltahacks", "Deleting temp file after cancel.")
            ImageUtils.deleteImage(filePath!!)
        }
    }



    fun sendCameraIntent() {

        val file = ImageUtils.createImageFile(this, "jpeg")
        filePath = file.getAbsolutePath()

        Log.d("deltahacks", filePath)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mImageUri = FileProvider.getUriForFile(this, "com.example.lindsay.delta5", file)

            val packageManager = packageManager
            val activities = packageManager.queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY)
            for (resolvedIntentInfo in activities) {
                val packageName = resolvedIntentInfo.activityInfo.packageName
                grantUriPermission(packageName, mImageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                Log.d("CAMERA_TEST", "grant  permissions to: $packageName")
            }

            cameraIntent.putExtra(EXTRA_OUTPUT, mImageUri)
        }
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
    }
}
