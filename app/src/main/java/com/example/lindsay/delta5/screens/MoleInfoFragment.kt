package com.example.lindsay.delta5.screens

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.FileProvider
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.example.lindsay.delta5.Application
import com.example.lindsay.delta5.screens.MainActivity
import com.example.lindsay.delta5.R
import com.example.lindsay.delta5.entities.Mole
import com.example.lindsay.delta5.models.MoleModel
import com.example.lindsay.delta5.utils.DateUtils
import com.example.lindsay.delta5.utils.ImageUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


/**
 *
 * @author Marshall Asch
 * @version 1.0
 * @since 2019-01-26
 */
class MoleInfoFragment : Fragment() {

    companion object {
        val MOLE_KEY = "MOLE_KEY"

        @JvmStatic
        fun newInstance() = MoleInfoFragment()
    }

    private val REQUEST_IMAGE_CAPTURE = 1

    private var mImageUri: Uri? = null
    private var filePath: String? = null

    private var mole: Mole? = null

    lateinit var imageView: ImageView
    lateinit var mainActivity: MainActivity

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        // Inflate the layout for this fragment

        mainActivity = activity as MainActivity

        val view: View = inflater.inflate(R.layout.mole_info_fragment, container, false)

        imageView = view.findViewById(R.id.mole_image)

        sendCameraIntent()

        return view;
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {

                var imageFile: File? = null
                var selectedImage: Uri? = null


                Log.d("Delta", "image taken ")
                //Log.d("deltahacks", filePath)


                if (resultCode == Activity.RESULT_OK) {

                    if (mImageUri != null) {
                        mainActivity.revokeUriPermission(mImageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

                        imageFile = File(filePath)

                    } else {
                        selectedImage = data?.data

                        try {
                            val inStream = mainActivity.contentResolver.openInputStream(selectedImage!!)
                            imageFile = ImageUtils.createImageFile(mainActivity, "jpeg")

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

                    val uuid = UUID.randomUUID().toString()

                    if (imageFile != null) {
                        Log.d("Delta", "image exists ")

                        var mole = Mole(
                                uuid,
                                "name_placeholder",
                                "body_placeholder",
                                "notes_holder",
                                imageFile.absolutePath,
                                DateUtils.currentDate()
                        )

                        MoleModel.saveMole( (mainActivity.application as Application).getRealm(), mole)
                    }


                    if (mole != null) {
                        imageView.setImageBitmap(ImageUtils.getImageBitmap(mole!!.imagePath, 150))
                    }


                    filePath = null
                    mImageUri = null

                    var frag = MoleInfoFragment()
                    var bundle = Bundle();
                    bundle.putString(MoleInfoFragment.MOLE_KEY, uuid)
                    frag.arguments = bundle


                    Log.d("deltahacks", "onActivityResult - frag ready")

                    filePath = null
                    mImageUri = null
                    // return
                }

            }
        }
        if (resultCode == AppCompatActivity.RESULT_CANCELED && filePath != null) {
            Log.d("Delta", "Deleting temp file after cancel.")
            ImageUtils.deleteImage(filePath!!)
        }
    }

    fun sendCameraIntent() {

        val file = ImageUtils.createImageFile(mainActivity, "jpeg")
        filePath = file.getAbsolutePath()

        Log.d("deltahacks", filePath)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mImageUri = FileProvider.getUriForFile(mainActivity, "com.example.lindsay.delta5", file)

            val packageManager = mainActivity.packageManager
            val activities = packageManager.queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY)
            for (resolvedIntentInfo in activities) {
                val packageName = resolvedIntentInfo.activityInfo.packageName
                mainActivity.grantUriPermission(packageName, mImageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                Log.d("CAMERA_TEST", "grant  permissions to: $packageName")
            }

            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
        }
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
    }
}
