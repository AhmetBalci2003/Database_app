package com.example.database_app

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirestoreHelper {
    private val db = FirebaseFirestore.getInstance()
    private val depoCollection = db.collection("Depo")

    suspend fun addDepo(depo: Depo) {
        depoCollection.document(depo.ID).set(depo).await()
    }

    suspend fun getDepoList(): List<Depo> {
        return depoCollection.get().await().toObjects(Depo::class.java)
    }

    suspend fun updateDepo(depo: Depo) {
        depoCollection.document(depo.ID).set(depo).await()
    }

    suspend fun deleteDepo(depoID: String) {
        depoCollection.document(depoID).delete().await()
    }
}
