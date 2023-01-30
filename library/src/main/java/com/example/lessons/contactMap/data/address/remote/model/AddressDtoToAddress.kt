package com.example.lessons.contactMap.data.address.remote.model

import com.example.lessons.contacts.domain.entity.Address

internal fun AddressDto.toAddress(): Address? =
    response.geoObjectCollection.featureMember
        .firstOrNull()?.geoObject?.metaDataProperty?.geocoderMetaData?.text
        ?.let(::Address)

