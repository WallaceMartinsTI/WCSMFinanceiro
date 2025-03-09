package com.wcsm.wcsmfinanceiro.presentation.model.plus

data class SubscriptionState(
    var subscriptionId: Long = 0,
    var title: String = "",
    var titleErrorMessage: String = "",
    var startDate: Long = 0L,
    var startDateErrorMessage: String = "",
    var dueDate: Long = 0L,
    var dueDateErrorMessage: String = "",
    var price: Double = 0.0,
    var priceErrorMessage: String = "",
    var durationInMonths: Int = 0,
    var durationInMonthsErrorMessage: String = "",
    var expired: Boolean = false,
    var automaticRenewal: Boolean = false,
    var responseErrorMessage: String = ""
)