package com.appschef.hospitalapp.usecase

import com.appschef.hospitalapp.data.remote.model.LoginResponse
import com.appschef.hospitalapp.repositories.HospitalRepositoryImpl
import com.appschef.hospitalapp.util.Endpoints
import com.appschef.hospitalapp.util.Result
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: HospitalRepositoryImpl
) {
    suspend operator fun invoke(email: String, password: String): Result<LoginResponse> {
        val response = repository.login(
            Endpoints.USER_BASE_URL + "auth/login",
            email.toRequestBody("text/plain".toMediaType()),
            password.toRequestBody("text/plain".toMediaType())
        )
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Result.Success(result)
            }
        }
        return Result.Failure("Error when sign in")
    }
}