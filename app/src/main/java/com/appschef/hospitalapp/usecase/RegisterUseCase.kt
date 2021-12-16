package com.appschef.hospitalapp.usecase

import com.appschef.hospitalapp.data.remote.model.RegisterResponse
import com.appschef.hospitalapp.repositories.HospitalRepositoryImpl
import com.appschef.hospitalapp.util.Endpoints
import com.appschef.hospitalapp.util.Result
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.Exception
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: HospitalRepositoryImpl
) {
    suspend operator fun invoke(email: String, password: String): Result<RegisterResponse> {
        val response = repository.registerUser(
            Endpoints.USER_BASE_URL + "register",
            email.toRequestBody("text/plain".toMediaType()),
            password.toRequestBody("text/plain".toMediaType())
        )
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Result.Success(result)
            }
        }
        return Result.Failure("Error registering account")
    }
}