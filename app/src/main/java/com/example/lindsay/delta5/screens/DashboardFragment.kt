package com.example.lindsay.delta5.screens

import android.content.Intent
import android.database.DataSetObserver
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
import android.widget.TextView
import com.example.lindsay.delta5.screens.MainActivity
import com.example.lindsay.delta5.R
import com.example.lindsay.delta5.entities.Mole
import com.example.lindsay.delta5.utils.ImageUtils
import io.realm.RealmBaseAdapter
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_dashboard.*
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
    lateinit var mainActivity: MainActivity
    lateinit var moleAdapter: MoleHistoryPlantAdapter
    lateinit var moleDataObserver: MoleDataObserver


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as MainActivity

        add_new_mole.setOnClickListener {
            mainActivity.switchFragment(MainActivity.Screen.MOLE_INFO)
        }

//        moleAdapter = MoleHistoryPlantAdapter(mainActivity.application.getMoles())

//        moleDataObserver = MoleDataObserver()
//        moleAdapter.registerDataSetObserver(moleDataObserver)
//        moleList = activity!!.findViewById(layoutID)
//        plantList.adapter = plantAdapter

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

    inner class MoleDataObserver: DataSetObserver() {
        override fun onChanged() {
            super.onChanged()

            // Set the no moles message to be visible or not
        }
    }

    inner class MoleHistoryPlantAdapter(plants: RealmResults<Mole>): RealmBaseAdapter<Mole>(plants) {

        private inner class ViewHolder {
            internal var moleName: TextView? = null
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            lateinit var returnView: View
            lateinit var viewHolder: ViewHolder

            // Setup the view so we can edit the internal fields
            if(convertView == null) {
                returnView = LayoutInflater.from(parent?.context).inflate(R.layout.mole_list_layout, parent, false)

                viewHolder = ViewHolder()
                viewHolder.moleName = returnView.findViewById(R.id.mole_name)

                returnView.tag = viewHolder
            } else {
                returnView = convertView
                viewHolder = returnView.tag as ViewHolder
            }

            // Actually set the internal fields of the view
            val mole = adapterData!![position]

            viewHolder.moleName?.text = mole.moleName

            if(mole.imagePath == null) {
//                viewHolder.plantImage?.setImageResource(R.drawable.flower)
            } else {
//                viewHolder.plantImage?.setImageBitmap(ImageUtils.getImageBitmap(plant.imagePath, 150))

//                if(viewHolder.plantImage?.drawable == null) {
//                    viewHolder.plantImage?.setImageResource(R.drawable.flower)
//                }
            }

            return returnView
        }

    }
}
