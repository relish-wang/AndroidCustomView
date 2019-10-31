package wang.relish.widget.sample.sample.multibar

import android.os.Parcel
import android.os.Parcelable
import wang.relish.widget.multibar.IEndpoint
import wang.relish.widget.multibar.IMultiPoints

/**
 * @author wangxin
 * @since 20191031
 */
data class MultiPoints(
    val min: Float,
    val max: Float,
    val centerColor: Int,
    val endPoints: ArrayList<IEndpoint>
) : IMultiPoints, Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readInt(),
        ArrayList(parcel.createTypedArrayList(Endpoint.CREATOR) ?: arrayListOf())
    )

    override fun min(): Float {
        return min
    }

    override fun max(): Float {
        return max
    }

    override fun centerColor(): Int {
        return centerColor
    }

    override fun endPoints(): List<IEndpoint> {
        return endPoints
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(min)
        parcel.writeFloat(max)
        parcel.writeInt(centerColor)
        parcel.writeTypedList(endPoints)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MultiPoints> {
        override fun createFromParcel(parcel: Parcel): MultiPoints {
            return MultiPoints(parcel)
        }

        override fun newArray(size: Int): Array<MultiPoints?> {
            return arrayOfNulls(size)
        }
    }
}