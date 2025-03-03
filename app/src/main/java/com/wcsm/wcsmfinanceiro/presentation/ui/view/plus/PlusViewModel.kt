package com.wcsm.wcsmfinanceiro.presentation.ui.view.plus

import androidx.lifecycle.ViewModel
import com.wcsm.wcsmfinanceiro.domain.usecase.plus.GetConvertedCurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlusViewModel @Inject constructor(
    private val getConvertedCurrencyUseCase: GetConvertedCurrencyUseCase
) : ViewModel() {

}