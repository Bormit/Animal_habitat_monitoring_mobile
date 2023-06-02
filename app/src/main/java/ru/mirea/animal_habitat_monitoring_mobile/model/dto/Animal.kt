package ru.mirea.animal_habitat_monitoring_mobile.model.dto

data class Animal(val latitude: Double, val longitude: Double, val species: String, val time: String, val userID: String) {}