package com.example.meaningfulacronyms.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meaningfulacronyms.model.Acromine
import com.example.meaningfulacronyms.network.AcromineRepository
import com.example.meaningfulacronyms.util.ApiCallState
import kotlinx.coroutines.launch
import retrofit2.Response


class AcromineViewModel(private val acromineRepository: AcromineRepository): ViewModel() {

    private val _wordMeanings = MutableLiveData<ApiCallState<List<Acromine>>>()
    val wordMeanings: LiveData<ApiCallState<List<Acromine>>>
        get() = _wordMeanings

     suspend fun getMeanings(wordAbbreviation: String){
        viewModelScope.launch {
            _wordMeanings.value = ApiCallState.Loading()
            val response = acromineRepository.getMeanings(wordAbbreviation)
            handleApiResponse(response)
        }
    }

    private fun handleApiResponse(response: Response<List<Acromine>>) : ApiCallState<List<Acromine>> {
        if(response.isSuccessful){
            response.body()?.let {resultResponse ->
                _wordMeanings.value = ApiCallState.Success(resultResponse)
                return ApiCallState.Success(resultResponse)
            }
        }
        return ApiCallState.Error(response.message())
    }
}