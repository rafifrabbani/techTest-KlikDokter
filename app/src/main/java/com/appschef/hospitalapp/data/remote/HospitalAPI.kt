package com.appschef.hospitalapp.data.remote

import com.appschef.hospitalapp.data.remote.model.HospitalList
import com.appschef.hospitalapp.data.remote.model.HospitalListItem
import com.appschef.hospitalapp.data.remote.model.LoginResponse
import com.appschef.hospitalapp.data.remote.model.RegisterResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface HospitalAPI {

    @GET("api/v1/hospitals")
    suspend fun getListHospital(): Response<HospitalList>

    @GET("api/v1/hospitals")
    suspend fun getHospitalById(
        @Query("id")
        id: Int
    ): Response<HospitalList>

    @FormUrlEncoded
    @POST("api/v1/hospitals")
    suspend fun addNewHospital(
        @Field("hospital_name")
        hospitalName: String,
        @Field("latitude")
        latitude: Double,
        @Field("longitude")
        longitude: Double,
        @Field("hospital_address")
        hospitalAddress: String
    ): Response<HospitalListItem>


    @HTTP(method = "DELETE", path = "/api/v1/hospitals/{id}", hasBody = true)
    suspend fun deleteHospital(
        @Path("id")
        id: Int
    ): Response<HospitalListItem>

    @Multipart
    @POST
    suspend fun registerUser(
        @Url url: String,
        @Part("email")
        email: RequestBody,
        @Part("password")
        password: RequestBody
    ): Response<RegisterResponse>

    @Multipart
    @POST
    suspend fun logIn(
        @Url url: String,
        @Part("email")
        email: RequestBody,
        @Part("password")
        password: RequestBody
    ): Response<LoginResponse>
}