package com.example.lessons.contactMap.data.address.remote.model

import com.google.gson.annotations.SerializedName

internal data class AddressItem(
    @SerializedName("response")
    val response: Response
)

data class Response(
    @SerializedName("GeoObjectCollection")
    val geoObjectCollection: GeoObjectCollection
)

data class GeoObjectCollection(
    @SerializedName("featureMember")
    val featureMember: List<FeatureMember>
)

data class FeatureMember(
    @SerializedName("GeoObject")
    val geoObject: GeoObject
)

data class GeoObject(
    @SerializedName("metaDataProperty")
    val metaDataProperty: GeoObjectMetaDataProperty,
)

data class GeoObjectMetaDataProperty(
    @SerializedName("GeocoderMetaData")
    val geocoderMetaData: GeocoderMetaData
)

data class GeocoderMetaData(
    @SerializedName("text")
    val text: String,
    @SerializedName("kind")
    val kind: String
)
