package com.example.lindsay.delta5.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.lindsay.delta5.MainActivity


/**
 *
 * @author Marshall Asch
 * @version 1.0
 * @since 2019-01-26
 */
class NewMoleFragment : Fragment() {

    lateinit var takePicture: Button
    lateinit var mainActivity: MainActivity

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as MainActivity

        takePicture = activity!!.findViewById(R.id.take_picture_button)



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.new_entry_fragment, container, false)
    }
}
