package com.appschef.hospitalapp.usecase

import com.appschef.hospitalapp.data.remote.model.HospitalListItem
import com.appschef.hospitalapp.repositories.HospitalRepositoryImpl
import com.appschef.hospitalapp.util.Result
import okio.IOException
import javax.inject.Inject

class GetHospitalListUseCase @Inject constructor(
    private val repository: HospitalRepositoryImpl
) {
    suspend operator fun invoke(): Result<List<HospitalListItem>> {
        val response = repository.getHospitalList()
        try {
            if (response.isSuccessful) {
                response.body()?.let { hospitalList ->
                    return Result.Success(hospitalList.toList().reversed())
                }
            }

            return Result.Failure("Error getting data")
        } catch (exception: IOException) {
            return Result.Failure("Error getting data")
        }
    }
}