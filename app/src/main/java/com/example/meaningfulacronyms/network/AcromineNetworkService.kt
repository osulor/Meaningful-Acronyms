package com.example.meaningfulacronyms.network

import com.example.meaningfulacronyms.model.AcromineResponse
import com.example.meaningfulacronyms.model.LongForm
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AcromineNetworkService {

    @GET("dictionary.py")
    fun getMeanings( @Query("sf")
                    wordAbbreviation: String ) : Response<AcromineResponse>
}