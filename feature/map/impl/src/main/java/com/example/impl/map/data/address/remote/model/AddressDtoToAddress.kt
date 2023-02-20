package com.example.impl.map.data.address.remote.model

import com.example.impl.map.domain.entity.ContactAddress

internal fun AddressDto.toAddress(): ContactAddress? =
    response.geoObjectCollection.featureMember
        .firstOrNull()?.geoObject?.metaDataProperty?.geocoderMetaData?.text
        ?.let(::ContactAddress)

