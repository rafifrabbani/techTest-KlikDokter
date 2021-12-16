package com.appschef.hospitalapp.usecase

import com.appschef.hospitalapp.data.remote.model.HospitalListItem
import com.appschef.hospitalapp.repositories.HospitalRepositoryImpl
import com.appschef.hospitalapp.util.Result
import javax.inject.Inject

class DeleteHospitalUseCase @Inject constructor(
    private val repository: HospitalRepositoryImpl
) {
    suspend operator fun invoke(id: Int): Result<HospitalListItem> {
        val response = repository.deleteHospital(id)
        if (response.isSuccessful) {
            response.body()?.let { hospitalItem ->
                return Result.Success(hospitalItem)
            }
        }
        return Result.Failure("Error deleting selected item")
    }
}