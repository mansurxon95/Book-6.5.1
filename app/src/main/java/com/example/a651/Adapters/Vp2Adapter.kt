package com.example.a651.Adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.a651.RVFragment
import com.example.a651.UserList
import com.example.a651.Usercontact

class Vp2Adapter(var list:ArrayList<UserList>, fragmentManager: FragmentActivity):
    FragmentStateAdapter(fragmentManager){
    override fun getItemCount(): Int {


        return list.size
    }

    override fun createFragment(position: Int): Fragment {

        return  RVFragment.newInstance(list[position].list)
    }

}