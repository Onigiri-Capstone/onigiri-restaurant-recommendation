package com.example.onigiri_restaurant_recommendation.data.remote.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

object Firebase {
    fun currentUser(): FirebaseUser = FirebaseAuth.getInstance().currentUser!!

    fun authInstance() = FirebaseAuth.getInstance()

    fun firestoreInstance() = FirebaseFirestore.getInstance()

    fun firebaseMessaging() = FirebaseMessaging.getInstance()

}