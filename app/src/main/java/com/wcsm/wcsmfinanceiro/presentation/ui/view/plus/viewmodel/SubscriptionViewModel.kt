package com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wcsm.wcsmfinanceiro.data.local.entity.Subscription
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.usecase.plus.subscriptions.DeleteSubscriptionUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.plus.subscriptions.GetSubscriptionsUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.plus.subscriptions.SaveSubscriptionUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.plus.subscriptions.UpdateSubscriptionUseCase
import com.wcsm.wcsmfinanceiro.presentation.model.CrudOperationType
import com.wcsm.wcsmfinanceiro.presentation.model.UiState
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
    val subscriptionsList = listOf(
        Subscription(
            subscriptionId = 994L,
            title = "Netflix",
            startDate = 1L,
            dueDate = 1L,
            durationInMonths = 3,
            price = 75.90,
            expired = false,
            automaticRenewal = true
        ),
        Subscription(
            subscriptionId = 995L,
            title = "Twitch",
            startDate = 1L,
            dueDate = 1L,
            durationInMonths = 3,
            price = 75.90,
            expired = false,
            automaticRenewal = true
        ),
        Subscription(
            subscriptionId = 996L,
            title = "Amazon Music",
            startDate = 1L,
            dueDate = 1L,
            durationInMonths = 3,
            price = 75.90,
            expired = false,
            automaticRenewal = true
        ),
        Subscription(
            subscriptionId = 997L,
            title = "Netflix",
            startDate = 1L,
            dueDate = 1L,
            durationInMonths = 3,
            price = 75.90,
            expired = false,
            automaticRenewal = true
        ),
        Subscription(
            subscriptionId = 998L,
            title = "Twitch",
            startDate = 1L,
            dueDate = 1L,
            durationInMonths = 3,
            price = 75.90,
            expired = false,
            automaticRenewal = true
        ),
        Subscription(
            subscriptionId = 999L,
            title = "Amazon Music",
            startDate = 1L,
            dueDate = 1L,
            durationInMonths = 3,
            price = 75.90,
            expired = false,
            automaticRenewal = true
        )
    )

    private val _uiState = MutableStateFlow(UiState<CrudOperationType>())
    val uiState = _uiState.asStateFlow()

    private val _subscriptionStateFlow = MutableStateFlow(SubscriptionState())
    val subscriptionStateFlow = _subscriptionStateFlow.asStateFlow()

    private val _subscriptions = MutableStateFlow<List<Subscription>?>(null)
    val subscriptions = _subscriptions.asStateFlow()

    fun updateSubscriptionState(updatedState: SubscriptionState) {
        _subscriptionStateFlow.value = updatedState
    }

    fun updateUiState(uiState: UiState<CrudOperationType>) {
        _uiState.value = uiState
    }

    fun resetSubscriptionState() {
        _subscriptionStateFlow.value = SubscriptionState()
    }

    fun resetUiState() {
        _uiState.value = UiState()
    }

    fun resetSubscriptionStateErrorMessage() {
        updateSubscriptionState(
            subscriptionStateFlow.value.copy(
                titleErrorMessage = "",
                startDateErrorMessage = "",
                dueDateErrorMessage = "",
                priceErrorMessage = "",
                durationInMonthsErrorMessage = "",
                responseErrorMessage = ""
            )
        )
    }

    private fun onLoadingResponse() {
        updateUiState(uiState.value.copy(isLoading = true))
    }

    private fun onErrorResponse(errorMessage: String) {
        updateUiState(uiState.value.copy(isLoading = false, error = errorMessage))
    }

    private fun onSuccessResponse(onSuccess: () -> Unit) {
        onSuccess()

        updateUiState(uiState.value.copy(
            isLoading = false,
            success = true
        ))
    }

    fun getSubscriptions() {
        viewModelScope.launch(Dispatchers.IO) {
            getSubscriptionsUseCase().collect { result ->
                when(result) {
                    is Response.Loading -> onLoadingResponse()
                    is Response.Error -> onErrorResponse(result.message)
                    is Response.Success -> onSuccessResponse {
                        _subscriptions.value = result.data.reversed()
                    }
                }
            }
        }
    }

    fun saveSubscription(subscriptionState: SubscriptionState) {
        resetSubscriptionStateErrorMessage()

        viewModelScope.launch(Dispatchers.IO) {
            updateUiState(uiState.value.copy(operationType = CrudOperationType.SAVE))

            if(isSubscriptionStateValid()) {
                val subscription = subscriptionState.toSubscription()
                saveSubscriptionUseCase(subscription).collect { result ->
                    when(result) {
                        is Response.Loading -> onLoadingResponse()
                        is Response.Error -> onErrorResponse(result.message)
                        is Response.Success -> onSuccessResponse {
                            resetSubscriptionState()
                            getSubscriptions()
                        }
                    }
                }
            }
        }
    }

    fun updateSubscription(subscriptionState: SubscriptionState) {
        resetSubscriptionStateErrorMessage()

        viewModelScope.launch(Dispatchers.IO) {
            updateUiState(uiState.value.copy(operationType = CrudOperationType.UPDATE))

            if(isSubscriptionStateValid()) {
                val subscription = subscriptionState.toSubscription()
                updateSubscriptionUseCase(subscription).collect { result ->
                    when(result) {
                        is Response.Loading -> onLoadingResponse()
                        is Response.Error -> onErrorResponse(result.message)
                        is Response.Success -> onSuccessResponse {
                            resetSubscriptionState()
                            getSubscriptions()
                        }
                    }
                }
            }
        }
    }

    fun deleteSubscription(subscriptionState: SubscriptionState) {
        resetSubscriptionStateErrorMessage()

        viewModelScope.launch(Dispatchers.IO) {
            updateUiState(uiState.value.copy(operationType = CrudOperationType.DELETE))

            if(isSubscriptionStateValid()) {
                val subscription = subscriptionState.toSubscription()
                deleteSubscriptionUseCase(subscription).collect { result ->
                    when(result) {
                        is Response.Loading -> onLoadingResponse()
                        is Response.Error -> onErrorResponse(result.message)
                        is Response.Success -> onSuccessResponse {
                            resetSubscriptionState()
                            getSubscriptions()
                        }
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