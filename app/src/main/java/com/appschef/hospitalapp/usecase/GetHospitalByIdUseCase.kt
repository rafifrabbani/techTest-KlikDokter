package com.appschef.hospitalapp.usecase

import com.appschef.hospitalapp.data.remote.model.HospitalListItem
import com.appschef.hospitalapp.repositories.HospitalRepositoryImpl
import com.appschef.hospitalapp.util.Result
import javax.inject.Inject

class GetHospitalByIdUseCase @Inject constructor(
    private val repository: HospitalRepositoryImpl
) {
    suspend operator fun invoke(id: Int): Result<List<HospitalListItem>> {
        val response = repository.getHospitalById(id)
        if (response.isSuccessful) {
            response.body()?.let { hospitalList ->
                return Result.Success(hospitalList.toList())
            }
        }

        return Result.Failure("Error getting data")
    }
}