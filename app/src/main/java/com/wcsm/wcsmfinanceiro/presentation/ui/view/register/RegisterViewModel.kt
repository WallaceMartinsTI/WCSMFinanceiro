package com.wcsm.wcsmfinanceiro.presentation.ui.view.register

import androidx.lifecycle.ViewModel
import java.util.UUID

class RegisterViewModel : ViewModel() {

    fun getRandomId() : String {
        return UUID.randomUUID().toString()
    }

}