package com.appschef.hospitalapp.repositories

import com.appschef.hospitalapp.data.remote.HospitalAPI
import com.appschef.hospitalapp.data.remote.model.HospitalList
import com.appschef.hospitalapp.data.remote.model.HospitalListItem
import com.appschef.hospitalapp.data.remote.model.LoginResponse
import com.appschef.hospitalapp.data.remote.model.RegisterResponse
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class HospitalRepositoryImpl @Inject constructor(
    private val hospitalAPI: HospitalAPI
): HospitalRepository {

    override suspend fun getHospitalList(): Response<HospitalList> {
        return hospitalAPI.getListHospital()
    }

    override suspend fun getHospitalById(id: Int): Response<HospitalList> {
        return hospitalAPI.getHospitalById(id)
    }

    override suspend fun addNewHospital(
        hospitalName: String,
        latitude: Double,
        longitude: Double,
        hospitalAddress: String
    ): Response<HospitalListItem> {
        return hospitalAPI.addNewHospital(
            hospitalName, latitude, longitude, hospitalAddress
        )
    }

    override suspend fun deleteHospital(id: Int): Response<HospitalListItem> {
        return hospitalAPI.deleteHospital(id)
    }

    override suspend fun registerUser(
        url: String,
        email: RequestBody,
        password: RequestBody
    ): Response<RegisterResponse> {
        return hospitalAPI.registerUser(
            url, email, password
        )
    }

    override suspend fun login(
        url: String,
        email: RequestBody,
        password: RequestBody
    ): Response<LoginResponse> {
        return hospitalAPI.logIn(
            url, email, password
        )
    }
}