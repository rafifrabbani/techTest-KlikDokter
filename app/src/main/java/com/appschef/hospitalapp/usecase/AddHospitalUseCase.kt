package com.appschef.hospitalapp.usecase

import com.appschef.hospitalapp.data.remote.model.HospitalListItem
import com.appschef.hospitalapp.repositories.HospitalRepositoryImpl
import com.appschef.hospitalapp.util.Result
import javax.inject.Inject

class AddHospitalUseCase @Inject constructor(
    private val repository: HospitalRepositoryImpl
) {
    suspend operator fun invoke(
        hospitalName: String,
        latitude: Double,
        longitude: Double,
        hospitalAddress: String
    ): Result<HospitalListItem> {
        val response = repository.addNewHospital(
            hospitalName, latitude, longitude, hospitalAddress
        )
        if (response.isSuccessful) {
            response.body()?.let { hospitalItem ->
                return Result.Success(hospitalItem)
            }
        }
        return Result.Failure("Error add item")
    }
}