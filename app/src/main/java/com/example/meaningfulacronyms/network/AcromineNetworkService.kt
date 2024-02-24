package com.example.meaningfulacronyms.network

import com.example.meaningfulacronyms.model.Acromine
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AcromineNetworkService {
    @GET("dictionary.py")
    suspend fun getMeanings( @Query("sf")
                    wordAbbreviation: String ) : Response<List<Acromine>>
}