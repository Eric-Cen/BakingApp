package com.eightmin4mile.goandroid.bakingapp.utils

import android.os.Parcel
import android.os.Parcelable
import androidx.work.Data

// reference: https://stackoverflow.com/questions/50798329/workmanager-data-builder-does-not-support-parcelable
fun Data.Builder.putParcelable(key: String, item: Parcelable): Data.Builder {
    val parcelObject = Parcel.obtain()
    try {
        item.writeToParcel(parcelObject, 0)
        putByteArray(key, parcelObject.marshall())
    } finally {
        parcelObject.recycle()
    }
    return this
}

fun Data.Builder.putParcelableList(key: String, parcelableItems: List<Parcelable>): Data.Builder {
    parcelableItems.forEachIndexed { i, item ->
        putParcelable("$key$i", item)
    }
    return this
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Parcelable> Data.getParcelable(key: String): T? {
    val parcelObject = Parcel.obtain()
    try {
        val bytes = getByteArray(key) ?: return null
        parcelObject.unmarshall(bytes, 0, bytes.size)
        parcelObject.setDataPosition(0)
        val creator = T::class.java.getField("CREATOR").get(null) as Parcelable.Creator<T>
        return creator.createFromParcel(parcelObject)
    } finally {
        parcelObject.recycle()
    }
}

inline fun <reified T : Parcelable> Data.getParcelableList(key: String): MutableList<T> {
    val parcelableItems = mutableListOf<T>()
    while (keyValueMap.containsKey("$key${parcelableItems.size}")) {
        parcelableItems.add(getParcelable<T>("$key${parcelableItems.size}") ?: break)
    }
    return parcelableItems
}
