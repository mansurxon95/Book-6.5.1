package com.example.a651


import android.net.Uri
import java.io.Serializable

class Usercontact : Serializable {
    var id:Int?=null
    var name:String?=null
    var tavsif:String?=null
    var image:Uri?=null
    var tableId:Int?=null
    var save:Int?=null


    constructor(id: Int?, name: String?, tavsif: String?, image: Uri?, save: Int?, tableId: Int?) {
        this.name = name
        this.tavsif = tavsif
        this.image = image
        this.tableId = tableId
        this.save = save
        this.id = id
    }

    constructor()

}