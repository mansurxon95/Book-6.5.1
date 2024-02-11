package com.example.a651.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.a651.R
import com.example.a651.Usercontact
import com.example.a651.databinding.ItemRvBinding

class RvAdapter(var onClik: OnClik): ListAdapter<Usercontact, RvAdapter.VH>(MyDiffUtill()) {

    inner class VH(var itemview : ItemRvBinding): RecyclerView.ViewHolder(itemview.root){

        fun onBind(user: Usercontact,position: Int){

            itemview.itemName.text = user.name
            itemview.itemImage.setImageURI(user.image)
            if (user.save==1){
                itemview.btnSaveIcon.setImageResource(R.drawable.heart0)
            }else{
                itemview.btnSaveIcon.setImageResource(R.drawable.heart1)
            }

            itemview.btnOzgartirish.setOnClickListener {
                onClik.edit(user,position,itemview.btnOzgartirish.rootView)
            }
            itemview.btnOchirish.setOnClickListener {
                onClik.delet(user, position)
            }
            itemview.btnSaveIcon.setOnClickListener {
                onClik.save(user, position)
            }

            itemview.btnView.setOnClickListener {
                onClik.view(user, position)
            }
        }

    }

    class MyDiffUtill: DiffUtil.ItemCallback<Usercontact>(){
        override fun areItemsTheSame(oldItem: Usercontact, newItem: Usercontact): Boolean {
            return  oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Usercontact, newItem: Usercontact): Boolean {
            return oldItem.equals(newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemRvBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.onBind(item,position)
    }

    interface OnClik{
        fun edit(contact: Usercontact,position: Int,view: View){

        }
        fun delet(contact: Usercontact,position: Int){

        }
        fun save(contact: Usercontact,position: Int){

        }
        fun view(contact: Usercontact,position: Int){

        }
    }


}