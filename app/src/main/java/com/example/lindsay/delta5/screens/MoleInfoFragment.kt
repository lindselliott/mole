package com.example.lindsay.delta5.screens

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.example.lindsay.delta5.Application
import com.example.lindsay.delta5.entities.Mole
import com.example.lindsay.delta5.models.MoleModel
import com.example.lindsay.delta5.network.HttpConnection
import com.example.lindsay.delta5.network.HttpResponce
import com.example.lindsay.delta5.utils.DateUtils
import com.example.lindsay.delta5.utils.ImageUtils
import io.realm.RealmObject
import kotlinx.android.synthetic.main.mole_info_fragment.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

import com.example.lindsay.delta5.R


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
    val MY_PERMISSIONS_REQUEST_CAMERA = 100
    val MY_PERMISSIONS_REQUEST_STORAGE = 101


    private var mImageUri: Uri? = null
    private var filePath: String? = null

    private var mole: Mole? = null

    lateinit var mainActivity: MainActivity

    private var isEditMode = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as MainActivity
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        LocalBroadcastManager.getInstance(mainActivity).registerReceiver(
                broadcastReceiver, IntentFilter(HttpConnection.AI_RESPONCE_ACTION));

        // Reset the fields
        mole_image.setImageResource(0)
        mole_location_field.setText("")
        mole_nickname_field.setText("")
        mole_date_taken_field.setText("")
        details_field.setText("")

        if (arguments != null && !arguments!!.isEmpty) {
            Log.d("deltahacks", "There are arguments so set up the mole from the database")
            mole = (mainActivity.application as Application).moles.where().equalTo("_ID", arguments!!.getString("id")).findFirst()!!

            mole_image.setImageBitmap(ImageUtils.getImageBitmap(mole!!.imagePath, 150))
            Log.d("deltahacks", "Mole: ${mole}")
        } else {
            Log.d("deltahacks", "This is a new mole so create a new mole")
            val moleID = UUID.randomUUID().toString()

            MoleModel.saveMole((mainActivity.application as Application).realm, Mole(_ID = moleID, date = DateUtils.currentDate()))
            mole = MoleModel.getMole((mainActivity.application as Application).realm, moleID)

            isEditMode = true

            mole_nickname_field.isEnabled = isEditMode
            mole_location_field.isEnabled = isEditMode
            
            if (checkCameraPermission(MY_PERMISSIONS_REQUEST_CAMERA) && checkStoragePermission(MY_PERMISSIONS_REQUEST_STORAGE)) {
                sendCameraIntent()
            }
        }

        if (mainActivity.menu != null) {
            mainActivity.menu!!.findItem(R.id.action_profile).isVisible = false
            mainActivity.menu!!.findItem(R.id.save_mole).isVisible = isEditMode
            mainActivity.menu!!.findItem(R.id.edit_mole).isVisible = !isEditMode
        }

        mole_location_field.setText(mole!!.bodyLocation)
        mole_nickname_field.setText(mole!!.moleName)
        details_field.setText(mole!!.notes)
        mole_date_taken_field.setText(DateUtils.getFormattedStringFromEpochTime(mole!!.date!!))

        mole!!.addChangeListener<RealmObject> { _ ->
            Log.d("deltahacks", "Mole Image: ${mole_image}")
            mole_image.setImageBitmap(ImageUtils.getImageBitmap(mole!!.imagePath, 150))
        }

        upload_button.setOnClickListener{ _ ->

            var file = File(mole!!.imagePath)
            HttpConnection().post(file, mainActivity)

            Log.d("deltahacks", "uploading...")
        }
    }

    open fun toggleEditMode() {
        isEditMode = !isEditMode

        if(mainActivity.menu != null) {
            mainActivity.menu!!.findItem(R.id.save_mole).isVisible = isEditMode
            mainActivity.menu!!.findItem(R.id.edit_mole).isVisible = !isEditMode
        }

        mole_nickname_field.isEnabled = isEditMode
        mole_location_field.isEnabled = isEditMode
    }

    open fun saveMole() {
        val toSave = Mole(
                mole!!._ID,
                mole_nickname_field.text.toString(),
                mole_location_field.text.toString(),
                details_field.text.toString(),
                mole!!.imagePath,
                mole!!.date,
                mole!!.confidenceMalignant,
                mole!!.confidenceBenign
        )

        MoleModel.saveMole((mainActivity.application as Application).realm, toSave)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.mole_info_fragment, container, false)

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(mainActivity).unregisterReceiver(broadcastReceiver);

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {

                var imageFile: File? = null
                var selectedImage: Uri? = null

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

                    if (imageFile != null) {


                        // shrink the file if the image is too big
                        ImageUtils.shrinkImage(imageFile)


                        (mainActivity.application as Application).realm.beginTransaction()
                        mole!!.imagePath = imageFile.absolutePath
                        (mainActivity.application as Application).realm.commitTransaction()
                    }


                    if (mole != null) {
                        mole_image.setImageBitmap(ImageUtils.getImageBitmap(mole!!.imagePath, 150))
                    }


                    filePath = null
                    mImageUri = null

                    filePath = null
                    mImageUri = null
                    return
                }

            }
        }
        if (resultCode == AppCompatActivity.RESULT_CANCELED && filePath != null) {
            ImageUtils.deleteImage(filePath!!)
        }
    }

    fun sendCameraIntent() {

        val file = ImageUtils.createImageFile(mainActivity, "jpeg")
        filePath = file.getAbsolutePath()

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mImageUri = FileProvider.getUriForFile(mainActivity, "com.example.lindsay.delta5", file)

            val packageManager = mainActivity.packageManager
            val activities = packageManager.queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY)
            for (resolvedIntentInfo in activities) {
                val packageName = resolvedIntentInfo.activityInfo.packageName
                mainActivity.grantUriPermission(packageName, mImageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }

            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
        }
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
    }


    /**
     * Check if the app has permission to use the camera and request it if it does not have permission.
     * @return `true` if permission is granted otherwise `false`
     */
    private fun checkCameraPermission(permissionFor: Int): Boolean {
        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(mainActivity, Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(mainActivity)
                        .setTitle(R.string.title_camera_permission)
                        .setMessage(R.string.text_camera_permission)
                        .setPositiveButton(R.string.okay, { dialogInterface: DialogInterface, i: Int ->
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(mainActivity,
                                    arrayOf(Manifest.permission.CAMERA),
                                    MY_PERMISSIONS_REQUEST_CAMERA)
                        })
                        .create()
                        .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(mainActivity,
                        arrayOf(Manifest.permission.CAMERA),
                        permissionFor)
            }
            return false
        } else {
            return true
        }
    }


    private fun checkStoragePermission(permissionFor: Int): Boolean {
        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(mainActivity)
                        .setTitle(R.string.title_storage_permission)
                        .setMessage(R.string.text_storage_permission)
                        .setPositiveButton(R.string.okay, { dialogInterface: DialogInterface, i: Int ->
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(mainActivity,
                                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                    MY_PERMISSIONS_REQUEST_STORAGE)
                        })
                        .create()
                        .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(mainActivity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        permissionFor)
            }
            return false
        } else {
            return true
        }
    }

    /**
     * The result from requesting camera permissions.
     *
     * @param requestCode the code that was sent for the permission request
     * @param permissions the permissions that were requested
     * @param grantResults if the permissions were granted or not
     */
    override fun onRequestPermissionsResult(requestCode: Int,
                                   permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CAMERA -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        // start the camera
                        sendCameraIntent()
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
            MY_PERMISSIONS_REQUEST_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        // start the camera
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action.equals(HttpConnection.AI_RESPONCE_ACTION)) {

                //progressBar.visibility = View.GONE

                if (!intent.getBooleanExtra(HttpConnection.SUCCESS_EXTRA, false)) {
                    Toast.makeText(mainActivity, "Error", Toast.LENGTH_SHORT).show()
                } else {

                    var str = ""

                    var predictions: ArrayList<HttpResponce.Prediction>

                    predictions = intent.getParcelableArrayListExtra(HttpConnection.PREDICTIONS_EXTRA)

                    for (p in predictions) {
                        str += p.tag + " : " + p.probability + "\n"
                    }

                    details_field.setText(str)

                    saveMole()

                    Log.d("deltahacks", str)

                }
            }
        }
    }
}
