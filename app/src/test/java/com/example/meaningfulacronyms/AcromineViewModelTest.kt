package com.example.meaningfulacronyms

import com.example.meaningfulacronyms.model.Acromine
import com.example.meaningfulacronyms.model.LongForm
import com.example.meaningfulacronyms.model.Variation
import com.example.meaningfulacronyms.network.AcromineRepository
import com.example.meaningfulacronyms.vm.AcromineViewModel
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class AcromineViewModelTest {

    private val acromineRepository = mockk<AcromineRepository>()
    private val acromineViewModel = AcromineViewModel(acromineRepository)

    @Before
    fun setup(){
        MockKAnnotations.init(this)
      //  Dispatchers.setMain(Dispatchers.Unconfined)

    }

    private val longFormList = listOf(
        LongForm("polymerase chain reaction","56576","1990", emptyList()),
        LongForm("polymerase chain reaction","56576","1990", emptyList()),
        LongForm("polymerase chain reaction","56576","1990", emptyList()),
    )

    val acromineResponse = Acromine("pcr",longFormList)

    @Test
    fun `when network call is a success - should return a non-empty list of meanings`() = runBlocking {
        val expectedResponse = listOf(acromineResponse)
        coEvery { acromineRepository.getMeanings("pcr") } returns Response.success(expectedResponse)
        acromineViewModel.getMeanings("pcr")
        coVerify { acromineRepository.getMeanings("pcr") }
        assert(acromineViewModel.wordMeanings.value == expectedResponse)
    }

    @Test
    fun ` when network is a success - should return an empty list`() = runBlocking {
        val expectedResponse = emptyList<Acromine>()
        coEvery { acromineRepository.getMeanings("pcr") } returns Response.success(expectedResponse)
        acromineViewModel.getMeanings("pcr")
        coVerify { acromineRepository.getMeanings("pcr") }
        assert(acromineViewModel.wordMeanings.value == expectedResponse)
    }


    @After
    fun tearDown(){
       //  Dispatchers.resetMain()
        clearAllMocks()

    }
}