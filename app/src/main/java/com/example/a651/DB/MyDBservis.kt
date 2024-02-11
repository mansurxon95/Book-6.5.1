package com.example.a651.DB


import com.example.a651.Usercontact
import com.example.a651.interfases.Usertab

interface MyDBservis {

    fun addypx(user:Usercontact)
    fun editypx(user:Usercontact):Int
    fun deletypx(user:Usercontact)
    fun delettab(tab:Usertab)
    fun getalltab(): ArrayList<Usertab>
    fun addtab(tabname:String)
    fun editypxsave(user: Usercontact, save: Int): Int
    fun getallypx(tableId: Int): ArrayList<Usercontact>
    fun getallsaveypx(): ArrayList<Usercontact>
    fun getallypx2(id:Int): ArrayList<Usercontact>
}