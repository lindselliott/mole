package com.example.lindsay.delta5.screens

import android.content.res.AssetManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.lindsay.delta5.R
import com.example.lindsay.delta5.network.HttpConnection
import com.example.lindsay.delta5.utils.ImageUtils
import java.io.File


/**
 *
 * @author Marshall Asch
 * @version 1.0
 * @since 2019-01-26
 */
class DashboardFragment : Fragment() {

    lateinit var profileButton: Button
    lateinit var checkMoleButton: Button
    lateinit var uploadButton: Button

    lateinit var mainActivity: MainActivity

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as MainActivity


        profileButton = activity!!.findViewById(R.id.profile_button)
        checkMoleButton = activity!!.findViewById(R.id.check_mole_button)

        uploadButton = activity!!.findViewById(R.id.upload)

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

        uploadButton.setOnClickListener {


            var am: AssetManager = mainActivity.assets

            am.openFd("mole_tmp.jpg")

            var f:File =  ImageUtils.createImageFile(mainActivity, "jpeg");

            HttpConnection.writeBytesToFile(am.open("mole_tmp.jpg"), f)

            HttpConnection().post(f, mainActivity)
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
