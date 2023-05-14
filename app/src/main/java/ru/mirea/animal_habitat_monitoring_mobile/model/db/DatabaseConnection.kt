package ru.mirea.animal_habitat_monitoring_mobile.model.db

import android.content.Context
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ru.mirea.animal_habitat_monitoring_mobile.model.dto.Animal

class DatabaseConnection() {
    private lateinit var dbref : DatabaseReference

    fun getDataFromFirebase() {
        dbref = FirebaseDatabase.getInstance().getReference("animal")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {

                    for (userSnapshot in snapshot.children) {


                        val animal = userSnapshot.getValue(Animal::class.java)


                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(context, "Ошибка подключения к базе данных!", Toast.LENGTH_SHORT)
//                    .show()
            }
        })
    }


    fun saveDataToFirebase(form: Animal) {
        val database = Firebase.database.reference
        val id = form.userID + "_" + form.time
        val animalRef = database.child("animal")

        val idRef = animalRef.child(id)
        idRef.child("latitude").setValue(form.latitude)
        idRef.child("longitude").setValue(form.longitude)
        idRef.child("species").setValue(form.species)
        idRef.child("time").setValue(form.time)
        idRef.child("userID").setValue(form.userID)
    }
}