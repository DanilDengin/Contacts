package com.example.lessons.contactMap.data.address.remote.model

import com.example.lessons.contacts.domain.entity.ContactAddress

internal fun AddressDto.toAddress(): ContactAddress? =
    response.geoObjectCollection.featureMember
        .firstOrNull()?.geoObject?.metaDataProperty?.geocoderMetaData?.text
        ?.let(::ContactAddress)

