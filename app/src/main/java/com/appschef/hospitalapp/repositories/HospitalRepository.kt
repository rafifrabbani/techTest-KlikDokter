package com.appschef.hospitalapp.repositories

import com.appschef.hospitalapp.data.remote.model.HospitalList
import com.appschef.hospitalapp.data.remote.model.HospitalListItem
import com.appschef.hospitalapp.data.remote.model.LoginResponse
import com.appschef.hospitalapp.data.remote.model.RegisterResponse
import okhttp3.RequestBody
import retrofit2.Response

interface HospitalRepository {
    suspend fun getHospitalList(): Response<HospitalList>

    suspend fun getHospitalById(id: Int): Response<HospitalList>

    suspend fun addNewHospital(
        hospitalName: String,
        latitude: Double,
        longitude: Double,
        hospitalAddress: String
    ): Response<HospitalListItem>

    suspend fun deleteHospital(id: Int): Response<HospitalListItem>

    suspend fun registerUser(
        url: String,
        email: RequestBody,
        password: RequestBody
    ): Response<RegisterResponse>

    suspend fun login(
        url: String,
        email: RequestBody,
        password: RequestBody
    ): Response<LoginResponse>
}