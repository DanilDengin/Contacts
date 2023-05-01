package com.example.impl.map.data.address.remote.model

import com.google.gson.annotations.SerializedName

internal class AddressDto(
    @SerializedName("response")
    val response: Response
) {

    class Response(
        @SerializedName("GeoObjectCollection")
        val geoObjectCollection: GeoObjectCollection
    ) {

        class GeoObjectCollection(
            @SerializedName("featureMember")
            val featureMember: List<FeatureMember>
        ) {

            class FeatureMember(
                @SerializedName("GeoObject")
                val geoObject: GeoObject
            ) {

                class GeoObject(
                    @SerializedName("metaDataProperty")
                    val metaDataProperty: GeoObjectMetaDataProperty
                ) {

                    class GeoObjectMetaDataProperty(
                        @SerializedName("GeocoderMetaData")
                        val geocoderMetaData: GeocoderMetaData
                    ) {

                        class GeocoderMetaData(
                            @SerializedName("text")
                            val text: String
                        )
                    }
                }
            }
        }
    }
}
