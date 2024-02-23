package com.example.meaningfulacronyms.network

import com.example.meaningfulacronyms.model.AcromineResponse
import retrofit2.Response

class AcromineRepository {
    suspend fun getMeanings(wordAbbreviation: String): Response<AcromineResponse> {
        return RetrofitInstance.ACROMINE_API.getMeanings(wordAbbreviation)
    }
}