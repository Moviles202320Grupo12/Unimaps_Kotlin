package com.uniandes.unimaps.ui.Tutor
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp

data class Tutor(
    private var id: String, // An identifier for the event, you can choose a suitable type
    val name: String,
    val description: String,
    val materia: String,
    val date: Timestamp?, // You can use a suitable date representation, e.g., String or Date
    val location: String,
    val imageUrl: String
) : Parcelable {

    constructor() : this("", "", "", "", null, "", "")

    fun setId(id: String) {
        this.id = id
    }

    fun getId(): String {
        return this.id
    }

    // Implement Parcelable methods
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(name)
        dest.writeString(description)
        dest.writeString(materia)
        dest.writeString(date.toString())
        dest.writeString(location)
        dest.writeString(imageUrl)
    }

    // Companion object for CREATOR
    companion object CREATOR : Parcelable.Creator<Tutor> {
        override fun createFromParcel(parcel: Parcel): Tutor {
            return Tutor(
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readSerializable() as Timestamp?,
                parcel.readString() ?: "",
                parcel.readString() ?: ""
            )
        }

        override fun newArray(size: Int): Array<Tutor?> {
            return arrayOfNulls(size)
        }
    }

    override fun describeContents(): Int {
        return 0
    }
}
