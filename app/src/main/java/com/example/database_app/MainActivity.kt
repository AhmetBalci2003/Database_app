package com.example.database_app


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.database_app.FirestoreHelper.updateDepo
import com.example.database_app.ui.theme.Database_appTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Database_appTheme {


                    MainScreen()


            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainScreen() {
    var showDialog by remember { mutableStateOf(false) }
    var showDialog2 by remember { mutableStateOf(false) }

    var inputid by remember {
        mutableStateOf("")
    }
    var inputid2 by remember {
        mutableStateOf("")
    }

    val firestore = FirebaseFirestore.getInstance()
    val coroutineScope = rememberCoroutineScope()


    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {

            val intent = Intent(context, listActivity2::class.java)
            context.startActivity(intent)
        }) {
            Text("Listele", fontSize = 20.sp)
        }
        Button(onClick = {

            val intent = Intent(context, AddActivity::class.java)
            context.startActivity(intent)
        }) {
            Text("ekle", fontSize = 20.sp)
        }
        Button(onClick = {
            showDialog =true
        }) {
            Text("ürün al ", fontSize = 20.sp)
        }
        Button(onClick = {
            showDialog2=true

        }) {
            Text("ürün teslim et ", fontSize = 20.sp)
        }


    }
    if (showDialog) {

        AlertDialog(
            onDismissRequest = { showDialog = false },
            text = {
                OutlinedTextField(value = inputid,
                    onValueChange = {inputid=it},
                    label = { Text(text ="bir id giriniz",)})
                   },

            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            var exists:Boolean = ürün_var_mi(firestore, inputid)
                            if(exists)
                            {



                                val intent=Intent(context,ürünAlActivity::class.java)
                                intent.putExtra("inputid",inputid)
                                context.startActivity(intent)


                            }
                            else if(exists==false)
                            {
                                Toast.makeText(context,"ürün depoda yok",Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(context,"bir hata oluştu",Toast.LENGTH_LONG).show()

                            }




                            showDialog = false
                        }

                        showDialog = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(
                    onClick = {

                        showDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )

    }
    else if (showDialog2)
    {
        AlertDialog(onDismissRequest = { showDialog2=false },
            text = {
                OutlinedTextField(value = inputid2,
                    onValueChange = {inputid2=it},
                    label = { Text(text ="bir id giriniz",)})
            },
            confirmButton = {
                coroutineScope.launch {
                    ürünteslimet(context,inputid2)
                   }
                },
            dismissButton = {
                Button(
                    onClick = {

                        showDialog2 = false
                    }
                ) {
                    Text("Cancel")
                }
            })

    }

}
suspend fun ürün_var_mi(firestore: FirebaseFirestore, inputID: String): Boolean {
    return try {
        val querySnapshot = firestore.collection("Depo")
            .whereEqualTo("id", inputID)
            .get()
            .await()



        !querySnapshot.isEmpty
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}
suspend fun ürünteslimet(context:Context,input:String)
{
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val depoCollection = Firebase.firestore.collection("Depo")
            val documentSnapshot = depoCollection.document(input).get().await()
            val existingDepo = documentSnapshot.toObject(Depo::class.java)

            if (existingDepo != null) {
                val updatedDepo = existingDepo.copy(
                    kullanici_isim = "",
                    kullanici_sisim = "",
                    kullanici_sicilno = "",
                    kullanici_dept = "",
                    isInStorge=true
                )
                updateDepo(updatedDepo)
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Başarılı", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, "Hata oluştu: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}