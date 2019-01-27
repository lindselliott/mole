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
import com.example.lindsay.delta5.screens.MainActivity
import com.example.lindsay.delta5.R
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

    lateinit var mainActivity: MainActivity

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        mainActivity = activity as MainActivity
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.mole_info_fragment, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                MoleInfoFragment()

    }
}
