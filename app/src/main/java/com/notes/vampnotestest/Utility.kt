package com.notes.vampnotestest

import android.content.Context
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat

object Utility {

    fun getCollectionReferenceForNotes(): CollectionReference {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return FirebaseFirestore.getInstance().collection("notes")
            .document(currentUser!!.uid).collection("my_notes")
    }

    fun timestampToString(timestamp: Timestamp): String {
        return SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate())
    }



}