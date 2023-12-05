package com.uniandes.unimaps.ui.Tutor

import com.google.firebase.Timestamp

import android.os.Parcel
import android.os.Parcelable

data class Tutor(
    private var id: String, // An identifier for the event, you can choose a suitable type
    val name: String,
    val description: String,
    val materia: String,
    val date: Timestamp?, // You can use a suitable date representation, e.g., String or Date
    val location: String,
    val imageUrl: String,
    val numBusquedas:String,
    val phone: String
) : Parcelable {
    constructor() : this("", "", "", "", null, "", "", "","")

    // Setter para el campo "id" en Firestore
    fun setId(id: String) {
        this.id = id
    }

    fun getId(): String {
        return this.id
    }
    fun aumentarBusquedas(): String{
        return (numBusquedas.toInt() +1).toString()
    }

    // Implement Parcelable methods
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(Timestamp::class.java.classLoader),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(materia)

        parcel.writeParcelable(date, flags)
        parcel.writeString(location)
        parcel.writeString(imageUrl)
        parcel.writeString(numBusquedas)
        parcel.writeString(phone)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Tutor> {
        override fun createFromParcel(parcel: Parcel): Tutor {
            return Tutor(parcel)
        }

        override fun newArray(size: Int): Array<Tutor?> {
            return arrayOfNulls(size)
        }
    }
}
