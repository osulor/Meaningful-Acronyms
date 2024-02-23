package com.example.meaningfulacronyms.network

import com.example.meaningfulacronyms.model.Acromine
import retrofit2.Response

class AcromineRepository {
     suspend fun getMeanings(wordAbbreviation: String): Response<List<Acromine>> {
        return RetrofitInstance.ACROMINE_API.getMeanings(wordAbbreviation)
    }
}