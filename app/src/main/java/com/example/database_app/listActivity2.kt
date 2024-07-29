package com.example.database_app


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.database_app.ui.theme.Database_appTheme
import kotlinx.coroutines.launch

class listActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Database_appTheme {


                   ListScreen()

            }
        }
    }
}


@Composable
fun ListScreen() {
    val scope = rememberCoroutineScope()
    var depoList by remember { mutableStateOf(emptyList<Depo>()) }
    var isDataLoaded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val fetchedList = FirestoreHelper.getDepoList()
                if (fetchedList.isEmpty()) {
                    // Veri olmadığında kullanıcıyı bilgilendirme
                    Toast.makeText(context, "Veri bulunamadı", Toast.LENGTH_SHORT).show()
                }
                depoList = fetchedList
            } catch (e: Exception) {
                // Hata durumunda kullanıcıya bilgi verme
                Toast.makeText(context, "Veri alınırken hata oluştu", Toast.LENGTH_SHORT).show()
            }
            isDataLoaded = true
        }
    }

    if (isDataLoaded) {
        LazyColumn(modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()) {
            items(depoList) { depo ->
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                    Text("İsim: ${depo.isim}")
                    Text("ID: ${depo.ID}")
                    Text("Raf: ${depo.Raf}")
                    Text("Ürün SAP No: ${depo.ürün_sap_no}")
                    Text("Stok Adet: ${depo.stok_adet}")
                    Text("Depo: ${depo.isInStorge}")
                    Text("Kullanıcı İsim: ${depo.kullanici_isim}")
                    Text("Kullanıcı Soyisim: ${depo.kullanici_sisim}")
                    Text("Kullanıcı Sicil No: ${depo.kullanici_sicilno}")
                    Text("Kullanıcı Departman: ${depo.kullanici_dept}")
                    Text("Verilme Tarihi: ${depo.verilme_tarihi}")
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    } else {
        // Veri yüklenirken bir şeyler göster
        CircularProgressIndicator()
    }
}