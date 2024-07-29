package com.example.database_app
import com.google.firebase.Timestamp
import java.util.Date

data class Depo(
    var isim:String="",
    var ID:String="",
    var Raf:String="",
    var ürün_sap_no:String="",
    var stok_adet:Int=0,
    var isInStorge:Boolean=true,
    var kullanici_isim:String="",
    var kullanici_sisim:String="",
    var kullanici_sicilno:String="",
    var kullanici_dept:String="",
    var verilme_tarihi: Date =Timestamp.now().toDate()
     )

