package com.example.database_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.example.database_app.FirestoreHelper.updateDepo
import com.example.database_app.ui.theme.Database_appTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ürünAlActivity : ComponentActivity() {
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Database_appTheme {

                val inputid = intent.getStringExtra("inputid") ?: ""
                teslimAl(inputid)


            }
        }
    }
}
@Composable
fun teslimAl(input: String) {
    val context = LocalContext.current

    var kullaniciAdi by remember { mutableStateOf("") }
    var kullaniciSoyadi by remember { mutableStateOf("") }
    var kullaniciSicilNo by remember { mutableStateOf("") }
    var kullaniciDept by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = kullaniciAdi,
            onValueChange = { kullaniciAdi = it },
            label = { Text(text = "Adınızı giriniz") }
        )
        OutlinedTextField(
            value = kullaniciSoyadi,
            onValueChange = { kullaniciSoyadi = it },
            label = { Text(text = "Soyadınızı giriniz") }
        )
        OutlinedTextField(
            value = kullaniciDept,
            onValueChange = { kullaniciDept = it },
            label = { Text(text = "Biriminizi giriniz") }
        )
        OutlinedTextField(
            value = kullaniciSicilNo,
            onValueChange = { kullaniciSicilNo = it },
            label = { Text(text = "Sicil no giriniz") }
        )
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val depoCollection = Firebase.firestore.collection("Depo")
                    val documentSnapshot = depoCollection.document(input).get().await()
                    val existingDepo = documentSnapshot.toObject(Depo::class.java)

                    if (existingDepo != null) {
                        val updatedDepo = existingDepo.copy(
                            kullanici_isim = kullaniciAdi,
                            kullanici_sisim = kullaniciSoyadi,
                            kullanici_sicilno = kullaniciSicilNo,
                            kullanici_dept = kullaniciDept,
                            isInStorge=false
                        )
                        updateDepo(updatedDepo)
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(context, "Başarılı", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(context, "Ürün bulunamadı", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "Hata oluştu: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }) {
            Text(text = "Ürünü Al")
        }
    }
}