package com.example.lindsay.delta5

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.lindsay.delta5.Fragments.MoleInfoFragment
import com.example.lindsay.delta5.Fragments.NewMoleFragment
import com.example.lindsay.delta5.utils.ImageUtils
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private var tempImagePath: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = NewMoleFragment()
        fragmentTransaction.add(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == RESULT_OK && tempImagePath != null) {

                    val bitmap = ImageUtils.getImageBitmap(tempImagePath!!)

                    // do something with the image that was just taken here

                    // switch fragments to the mole info frag

                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    val fragment = MoleInfoFragment()
                    fragmentTransaction.add(R.id.fragment_container, fragment)
                    fragmentTransaction.commit()
                }
            }
        }
        if (resultCode == RESULT_CANCELED && tempImagePath != null) {
            Log.d("deltaHac", "Deleting temp file after cancel.")
            ImageUtils.deleteImage(tempImagePath!!)
        }
    }
}
