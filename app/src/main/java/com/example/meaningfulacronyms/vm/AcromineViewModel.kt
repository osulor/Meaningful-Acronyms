package com.example.meaningfulacronyms.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meaningfulacronyms.model.AcromineResponse
import com.example.meaningfulacronyms.network.AcromineRepository
import com.example.meaningfulacronyms.util.ApiCallState
import kotlinx.coroutines.launch
import retrofit2.Response


class AcromineViewModel(val acromineRepository: AcromineRepository): ViewModel() {

    private val _wordMeanings = MutableLiveData<ApiCallState<AcromineResponse>>()
    val wordMeanings: LiveData<ApiCallState<AcromineResponse>>
        get() = _wordMeanings

    private suspend fun getMeanings(wordAbbreviation: String){
        viewModelScope.launch {
            _wordMeanings.value = ApiCallState.Loading()
            val response = acromineRepository.getMeanings(wordAbbreviation)
            handleApiResponse(response)
        }
    }

    private fun handleApiResponse(response: Response<AcromineResponse>) : ApiCallState<AcromineResponse> {
        if(response.isSuccessful){
            response.body()?.let {resultResponse ->
                _wordMeanings.value = ApiCallState.Success(resultResponse)
                return ApiCallState.Success(resultResponse)
            }
        }
        return ApiCallState.Error(response.message())
    }
}