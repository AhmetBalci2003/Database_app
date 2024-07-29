package com.example.database_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.database_app.ui.theme.Database_appTheme
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AddActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Database_appTheme {
                AddScreen()

            }
        }
    }
}

@Composable
fun AddScreen() {
    val scope = rememberCoroutineScope()
    var isim by remember { mutableStateOf("") }
    var Raf by remember { mutableStateOf("") }
    var ürünSapNo by remember { mutableStateOf("") }
    var stokAdet by remember { mutableStateOf("") }
    var depo by remember { mutableStateOf(true) }
    var kullaniciIsim by remember { mutableStateOf("") }
    var kullaniciSisim by remember { mutableStateOf("") }
    var kullaniciSicilno by remember { mutableStateOf("") }
    var kullaniciDept by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = isim,
            onValueChange = { isim = it },
            label = { Text("İsim") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = Raf,
            onValueChange = { Raf = it },
            label = { Text("Raf") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = ürünSapNo,
            onValueChange = { ürünSapNo = it },
            label = { Text("Ürün SAP No") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = stokAdet,
            onValueChange = { stokAdet = it },
            label = { Text("Stok Adet") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            scope.launch {
                val newId = getNextId()
                val depo = Depo(
                    isim = isim,
                    ID = newId,
                    Raf = Raf,
                    ürün_sap_no = ürünSapNo,
                    stok_adet = stokAdet.toIntOrNull() ?: 0,
                    isInStorge = true,
                    kullanici_isim = kullaniciIsim,
                    kullanici_sisim = kullaniciSisim,
                    kullanici_sicilno = kullaniciSicilno,
                    kullanici_dept = kullaniciDept,
                    verilme_tarihi = Timestamp.now().toDate()
                )
                FirestoreHelper.addDepo(depo)
            }
        }) {
            Text(" Ekle", fontSize = 20.sp)
        }
    }
}

suspend fun getNextId(): String {
    val db = FirebaseFirestore.getInstance()
    val counterDoc = db.collection("counters").document("depoCounter")

    // Atomik olarak sayacı artır
    return db.runTransaction { transaction ->
        val snapshot = transaction.get(counterDoc)
        val currentId = snapshot.getLong("currentId") ?: 0
        val newId = currentId + 1
        transaction.set(counterDoc, mapOf("currentId" to newId))
        newId.toString()
    }.await()
}