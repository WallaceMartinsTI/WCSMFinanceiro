package com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wcsm.wcsmfinanceiro.data.local.entity.Subscription
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.usecase.plus.subscriptions.DeleteSubscriptionUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.plus.subscriptions.GetSubscriptionsUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.plus.subscriptions.SaveSubscriptionUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.plus.subscriptions.UpdateSubscriptionUseCase
import com.wcsm.wcsmfinanceiro.presentation.model.plus.SubscriptionState
import com.wcsm.wcsmfinanceiro.util.toSubscription
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val getSubscriptionsUseCase: GetSubscriptionsUseCase,
    private val saveSubscriptionUseCase: SaveSubscriptionUseCase,
    private val updateSubscriptionUseCase: UpdateSubscriptionUseCase,
    private val deleteSubscriptionUseCase: DeleteSubscriptionUseCase
) : ViewModel() {
    private val _subscriptionStateFlow = MutableStateFlow(SubscriptionState())
    val subscriptionStateFlow = _subscriptionStateFlow.asStateFlow()

    private val _subscriptions = MutableStateFlow<List<Subscription>?>(null)
    val subscriptions = _subscriptions.asStateFlow()

    fun updateSubscriptionState(updatedState: SubscriptionState) {
        _subscriptionStateFlow.value = updatedState
    }

    fun resetSubscriptionState() {
        _subscriptionStateFlow.value = SubscriptionState()
    }

    fun getSubscriptions() {
        viewModelScope.launch(Dispatchers.IO) {
            getSubscriptionsUseCase().collect { result ->
                when(result) {
                    is Response.Loading -> {}
                    is Response.Error -> {}
                    is Response.Success -> {
                        _subscriptions.value = result.data
                    }
                }
            }
        }
    }

    fun saveSubscription(subscriptionState: SubscriptionState) {
        viewModelScope.launch(Dispatchers.IO) {

            if(isSubscriptionStateValid()) {
                val subscription = subscriptionState.toSubscription()
                saveSubscriptionUseCase(subscription).collect { result ->
                    when(result) {
                        is Response.Loading -> {}
                        is Response.Error -> {}
                        is Response.Success -> {}
                    }
                }
            }
        }
    }

    fun updateSubscription(subscriptionState: SubscriptionState) {
        viewModelScope.launch(Dispatchers.IO) {

            if(isSubscriptionStateValid()) {
                val subscription = subscriptionState.toSubscription()
                updateSubscriptionUseCase(subscription).collect { result ->
                    when(result) {
                        is Response.Loading -> {}
                        is Response.Error -> {}
                        is Response.Success -> {}
                    }
                }
            }
        }
    }

    fun deleteSubscription(subscriptionState: SubscriptionState) {
        viewModelScope.launch(Dispatchers.IO) {

            if(isSubscriptionStateValid()) {
                val subscription = subscriptionState.toSubscription()
                deleteSubscriptionUseCase(subscription).collect { result ->
                    when(result) {
                        is Response.Loading -> {}
                        is Response.Error -> {}
                        is Response.Success -> {}
                    }
                }
            }
        }
    }

    private fun isSubscriptionStateValid(): Boolean {
        val isTitleValid = validateTitle(subscriptionStateFlow.value.title)
        val isStartDateValid = validateStartDate(subscriptionStateFlow.value.startDate)
        val isDueDateValid = validateDueDate(subscriptionStateFlow.value.dueDate)
        val isPriceValid = validatePrice(subscriptionStateFlow.value.price)
        val isDurationInMonthsValid = validateDurationInMonths(subscriptionStateFlow.value.durationInMonths)

        updateSubscriptionState(
            subscriptionStateFlow.value.copy(
                titleErrorMessage = isTitleValid.second,
                startDateErrorMessage = isStartDateValid.second,
                dueDateErrorMessage = isDueDateValid.second,
                priceErrorMessage = isPriceValid.second,
                durationInMonthsErrorMessage = isDurationInMonthsValid.second
            )
        )

        return isTitleValid.first && isStartDateValid.first && isDueDateValid.first && isPriceValid.first && isDurationInMonthsValid.first
    }

    private fun validateTitle(title: String): Pair<Boolean, String> {
        return if(title.isBlank()) {
            Pair(false, "O título não pode ser vazio.")
        } else if(title.length < 3) {
            Pair(false, "O título é muito curto (min. 3 caracteres).")
        } else if(title.length > 20) {
            Pair(false, "O título é muito grande (${title.length}/20 caracteres).")
        } else {
            Pair(true, "")
        }
    }

    private fun validateStartDate(startDate: Long): Pair<Boolean, String> {
        return if(startDate == 0L) {
            Pair(false, "Você deve escolher a data inicial.")
        } else if(startDate < 0) {
            Pair(false, "Data inválida.")
        } else {
            Pair(true, "")
        }
    }

    private fun validateDueDate(dueDate: Long): Pair<Boolean, String> {
        return if(dueDate == 0L) {
            Pair(false, "Você deve escolher uma data de vencimento.")
        } else if(dueDate < 0) {
            Pair(false, "Data inválida.")
        } else {
            Pair(true, "")
        }
    }

    private fun validatePrice(price: Double): Pair<Boolean, String> {
        return if(price == 0.0) {
            Pair(false, "Você deve informar um preço maior que 0.")
        } else if(price < 0) {
            Pair(false, "Valor inválido.")
        } else {
            Pair(true, "")
        }
    }

    private fun validateDurationInMonths(durationInMonths: Int): Pair<Boolean, String> {
        return if(durationInMonths == 0) {
            Pair(false, "Você deve informar uma duração maior que 0.")
        } else if(durationInMonths < 0) {
            Pair(false, "Duração inválido.")
        } else {
            Pair(true, "")
        }
    }

}