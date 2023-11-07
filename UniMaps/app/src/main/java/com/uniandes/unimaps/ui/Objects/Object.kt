package com.uniandes.unimaps.ui.Events

import com.google.firebase.Timestamp

import android.os.Parcel
import android.os.Parcelable

data class Object(
    private var id: String, // An identifier for the event, you can choose a suitable type
    val name: String,
    val description: String,
    val date: Timestamp?, // You can use a suitable date representation, e.g., String or Date
    val location: String,
    val url: String,
    val urlImage: String
) : Parcelable {
    constructor() : this("", "", "", null, "", "", "")

    // Setter para el campo "id" en Firestore
    fun setId(id: String) {
        this.id = id
    }

    fun getId(): String {
        return this.id
    }

    // Implement Parcelable methods
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(Timestamp::class.java.classLoader),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeParcelable(date, flags)
        parcel.writeString(location)
        parcel.writeString(url)
        parcel.writeString(urlImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Event> {
        override fun createFromParcel(parcel: Parcel): Event {
            return Event(parcel)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }
    }
}
