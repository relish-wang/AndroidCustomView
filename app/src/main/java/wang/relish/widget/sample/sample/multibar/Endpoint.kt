package wang.relish.widget.sample.sample.multibar

import android.os.Parcel
import android.os.Parcelable
import wang.relish.widget.multibar.IEndpoint

data class Endpoint(
    val value: Double = 0.0,
    val name: String = ""
) : IEndpoint, Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readString() ?: ""
    )

    override fun value(): Double {
        return value
    }

    override fun name(): String {
        return name
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(value)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Endpoint> {
        override fun createFromParcel(parcel: Parcel): Endpoint {
            return Endpoint(parcel)
        }

        override fun newArray(size: Int): Array<Endpoint?> {
            return arrayOfNulls(size)
        }
    }
}