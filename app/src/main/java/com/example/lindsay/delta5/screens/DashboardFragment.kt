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
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import java.io.File
import java.io.IOException


/**
 *
 * @author Marshall Asch
 * @version 1.0
 * @since 2019-01-26
 */
class DashboardFragment : Fragment() {

    lateinit var profileButton: Button
    lateinit var checkMoleButton: Button
    lateinit var mainActivity: MainActivity

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as MainActivity


        profileButton = activity!!.findViewById(R.id.profile_button)
        checkMoleButton = activity!!.findViewById(R.id.check_mole_button)

        profileButton.setOnClickListener {
            mainActivity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ProfileFragment())
                    .addToBackStack(null)
                    .commit()
        }

        checkMoleButton.setOnClickListener {
            mainActivity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, NewMoleFragment())
                    .addToBackStack(null)
                    .commit()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                DashboardFragment()

    }
}
