package com.appschef.hospitalapp.data.remote.model


import com.google.gson.annotations.SerializedName

data class HospitalListItem(
    val createdAt: String,
    @SerializedName("hospital_address")
    val hospitalAddress: String,
    @SerializedName("hospital_name")
    val hospitalName: String,
    val id: String,
    val latitude: String,
    val longitude: String,
    var isExpandable: Boolean = false
)