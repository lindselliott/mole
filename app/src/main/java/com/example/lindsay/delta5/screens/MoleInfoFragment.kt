package com.example.lindsay.delta5.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.FileProvider
import android.support.v4.view.ViewPager
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
import com.example.lindsay.delta5.utils.ImageUtils
import java.io.File
import java.io.IOException


/**
 *
 * @author Marshall Asch
 * @version 1.0
 * @since 2019-01-26
 */
class MoleInfoFragment : Fragment() {

    companion object {
        val MOLE_KEY = "MOLE_KEY"
    }

    lateinit var mainActivity: MainActivity

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        // Inflate the layout for this fragment

        mainActivity = activity as MainActivity

        val view: View = inflater.inflate(R.layout.mole_info_fragment, container, false)

        val bundle = this.arguments

        if (bundle != null) {
            // get the mole thing

            val moleKey: String? = bundle.getString(MOLE_KEY)

            var mole: Mole? =  if (moleKey == null) null else MoleModel.getMole((mainActivity.application as Application).getRealm(), moleKey)
            var imageView: ImageView = view.findViewById(R.id.mole_image)

            if (mole != null) {
                imageView.setImageBitmap(ImageUtils.getImageBitmap(mole.imagePath, 150))
            }
        }

        return view;
    }
}
