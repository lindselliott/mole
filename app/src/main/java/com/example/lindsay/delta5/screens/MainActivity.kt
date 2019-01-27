package com.example.lindsay.delta5.screens

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.lindsay.delta5.Application

import com.example.lindsay.delta5.R
import com.example.lindsay.delta5.utils.ImageUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    enum class Screen {
        DASHBOARD,
        NEW_ENTRY,
        MOLE_INFO,
        HISTORY,
        PROFILE
    }

    private val fragments: MutableMap<Screen, Fragment> = HashMap()

    private val REQUEST_IMAGE_CAPTURE = 1

    private var mImageUri: Uri? = null
    private var filePath: String? = null
    private lateinit var deltaApplication: Application

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(appBar) // appBar is the id of the toolbar in the layout file

        deltaApplication = application as Application

        initFragments()
    }

    private fun initFragments() {
        fragments[Screen.DASHBOARD] = DashboardFragment.newInstance()
        fragments[Screen.NEW_ENTRY] = NewMoleFragment.newInstance()
        fragments[Screen.MOLE_INFO] = MoleInfoFragment.newInstance()
        fragments[Screen.HISTORY] = MoleInfoFragment.newInstance() // FIXME: Make it the proper fragment
        fragments[Screen.PROFILE] = ProfileFragment.newInstance()

        switchFragment(Screen.DASHBOARD)
    }

    private fun switchFragment(screen: Screen, addToBackStack: Boolean = true, vararg params: String) {

        val fragment = fragments[screen] ?: return
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        // Set params if present
        if (params.isNotEmpty()) {
            val bundle = Bundle()
            for (i in params.indices) {
                bundle.putString("PARAM" + (i + 1), params[i])
            }
            fragment.arguments = bundle
        }
        
        if(addToBackStack) {
            fragmentTransaction.addToBackStack(null)
        }

        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        R.id.action_profile -> {
            switchFragment(Screen.PROFILE, true)
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
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

                    supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, MoleInfoFragment())
                            .addToBackStack(null)
                            .commit()

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

            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
        }
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
    }
}
