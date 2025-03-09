package com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.wcsm.wcsmfinanceiro.data.local.entity.Subscription
import com.wcsm.wcsmfinanceiro.presentation.ui.component.AppDatePicker
import com.wcsm.wcsmfinanceiro.presentation.ui.component.AppLoader
import com.wcsm.wcsmfinanceiro.presentation.ui.component.ConfirmDeletionDialog
import com.wcsm.wcsmfinanceiro.presentation.ui.component.CustomCheckbox
import com.wcsm.wcsmfinanceiro.presentation.ui.component.MonetaryInputField
import com.wcsm.wcsmfinanceiro.presentation.ui.component.XIcon
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.ErrorColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnBackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color
import com.wcsm.wcsmfinanceiro.util.brazilianDateToTimeInMillis
import com.wcsm.wcsmfinanceiro.util.toBrazilianDateString
import com.wcsm.wcsmfinanceiro.util.toBrazilianReal

@Composable
fun SubscriptionsView() {

    var showAddOrEditSubscription by remember { mutableStateOf(false) }

    val subscriptions = listOf<Subscription>(
        Subscription(
            subscriptionId = 1L,
            title = "Netflix",
            startDate = 1L,
            dueDate = 1L,
            durationInMonths = 3,
            price = 75.90,
            expired = false,
            automaticRenewal = true
        ),
        Subscription(
            subscriptionId = 2L,
            title = "Twitch",
            startDate = 1L,
            dueDate = 1L,
            durationInMonths = 3,
            price = 75.90,
            expired = false,
            automaticRenewal = true
        ),
        Subscription(
            subscriptionId = 3L,
            title = "Amazon Music",
            startDate = 1L,
            dueDate = 1L,
            durationInMonths = 3,
            price = 75.90,
            expired = false,
            automaticRenewal = true
        )
    )

    Dialog(
        onDismissRequest = {}
    ) {
        Box(
          modifier = Modifier.fillMaxWidth().background(SurfaceColor)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ASSINATURAS",
                    color = PrimaryColor,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(16.dp))

                if(subscriptions.isEmpty()) {
                    Text(
                        text = "Sem assinaturas no momento",
                        style = MaterialTheme.typography.labelMedium
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(subscriptions) { subscription ->
                            SubscriptionItem(subscription)
                        }

                        item {
                            Spacer(modifier = Modifier.height(40.dp))
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = { showAddOrEditSubscription = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(42.dp)
                    .padding(bottom = 8.dp, end = 8.dp),
                containerColor = PrimaryColor,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Ícone de adicionar"
                )
            }
        }

        if(showAddOrEditSubscription) {}

    }
}

@Preview
@Composable
private fun SubscriptionsViewPreview() {
    WCSMFinanceiroTheme {
        SubscriptionsView()
    }
}

@Composable
private fun SubscriptionItem(
    subscription: Subscription
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White.copy(0.1f))
            .fillMaxWidth()
            .height(50.dp)
            .border(1.dp, PrimaryColor, RoundedCornerShape(15.dp))
            .clickable { },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Icon(
            imageVector = Icons.Default.Subscriptions,
            contentDescription = null,
            tint = PrimaryColor,
            modifier = Modifier.padding(horizontal = 8.dp).size(24.dp)
        )

        Text(
            text = subscription.title,
            color = PrimaryColor,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(80.dp)
        )

        Text(
            text = subscription.price.toBrazilianReal(),
            color = PrimaryColor,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.OpenInFull,
            contentDescription = "Ícone de abrir em tela cheia",
            tint = PrimaryColor,
            modifier = Modifier.padding(horizontal = 8.dp).size(24.dp)
        )
    }
}

@Preview
@Composable
private fun SubscriptionItemPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        val subscription = Subscription(
            subscriptionId = 1L,
            title = "Netflix",
            startDate = 1L,
            dueDate = 1L,
            durationInMonths = 3,
            price = 75.90,
            expired = false,
            automaticRenewal = true
        )

        Column(
            modifier = Modifier.width(300.dp)
        ) {
            SubscriptionItem(subscription = subscription)
            Spacer(Modifier.height(16.dp))
            SubscriptionItem(subscription = subscription.copy(title = "Amazon Music"))
            Spacer(Modifier.height(16.dp))
            SubscriptionItem(subscription = subscription.copy(title = "Assinatura da Amazon Music"))
        }
    }
}

@Composable
private fun AddOrEditSubscriptionDialog() {
    val focusRequester = remember { List(7) { FocusRequester() } }

    var selectedDate by remember { mutableStateOf("") }
    var showDatePickerDialog by remember { mutableStateOf(false) }

    var selectedDueDate by remember { mutableStateOf("") }
    var showDueDatePickerDialog by remember { mutableStateOf(false) }

    var isSubscriptionToEdit by remember { mutableStateOf(false) }
    var isModalLoading by remember { mutableStateOf(false) }
    var showConfirmSubscriptionDeletionDialog by remember { mutableStateOf(false) }

    var monetaryValue by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = {}
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .background(SurfaceColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "ADICIONAR ASSINATURA",
                    modifier = Modifier.align(Alignment.Center),
                    color = PrimaryColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )

                XIcon(
                    modifier = Modifier.align(Alignment.CenterEnd).size(40.dp)
                ) {
                    //onDismiss()
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = OnBackgroundColor
            )

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        modifier = Modifier
                            .width(280.dp),
                            //.focusRequester(focusRequester[0]),
                        label = {
                            Text(
                                text = "Origem",
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        placeholder = {
                            Text(
                                text = "Digite a origem da compra",
                                fontFamily = PoppinsFontFamily
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Ícone de carrinho de compra",
                                tint = White06Color
                            )
                        },
                        trailingIcon = {
                            /*if (billDialogState.origin.isNotBlank()) {
                                XIcon {
                                    onValueChange(
                                        billDialogState.copy(
                                            origin = ""
                                        )
                                    )
                                    focusRequester[0].requestFocus()
                                }
                            }*/
                        },
                        singleLine = true,
                        supportingText = {},
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                    )

                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        modifier = Modifier
                            .width(280.dp)
                            .focusRequester(focusRequester[2])
                            .onFocusEvent {
                                if (it.isFocused) {
                                    showDatePickerDialog = true
                                    focusRequester[2].freeFocus()
                                }
                            },
                        label = {
                            Text(
                                text = "Data*",
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        supportingText = {
                            /*if (billDialogState.dateErrorMessage.isNotBlank()) {
                                Text(
                                    text = billDialogState.dateErrorMessage,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }*/
                        },
                        isError = false,//billDialogState.dateErrorMessage.isNotBlank(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Ícone de Calendário",
                                tint = White06Color
                            )
                        },
                        trailingIcon = {
                            if (selectedDate != "") {
                                XIcon {
                                    selectedDate = ""
                                    focusRequester[2].requestFocus()
                                }
                            }
                        },
                        readOnly = true
                    )
                    if (showDatePickerDialog) {
                        AppDatePicker(
                            onDismiss = {
                                showDatePickerDialog = false
                            }
                        ) { selectedDateResult ->
                            /*selectedDate = selectedDateResult.toBrazilianDateString()
                            onValueChange(
                                billDialogState.copy(
                                    date = selectedDate.brazilianDateToTimeInMillis() ?: 0L
                                )
                            )*/
                        }
                    }

                    OutlinedTextField(
                        value = selectedDate,
                        onValueChange = {},
                        modifier = Modifier
                            .width(280.dp)
                            .focusRequester(focusRequester[2])
                            .onFocusEvent {
                                if (it.isFocused) {
                                    showDatePickerDialog = true
                                    focusRequester[2].freeFocus()
                                }
                            },
                        label = {
                            Text(
                                text = "Data*",
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        supportingText = {
                            /*if (billDialogState.dateErrorMessage.isNotBlank()) {
                                Text(
                                    text = billDialogState.dateErrorMessage,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }*/
                        },
                        isError = false,//billDialogState.dateErrorMessage.isNotBlank(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Ícone de Calendário",
                                tint = White06Color
                            )
                        },
                        trailingIcon = {
                            if (selectedDate != "") {
                                XIcon {
                                    selectedDate = ""
                                    focusRequester[2].requestFocus()
                                }
                            }
                        },
                        readOnly = true
                    )
                    if (showDatePickerDialog) {
                        AppDatePicker(
                            onDismiss = {
                                showDatePickerDialog = false
                            }
                        ) { selectedDateResult ->
                            /*selectedDate = selectedDateResult.toBrazilianDateString()
                            onValueChange(
                                billDialogState.copy(
                                    date = selectedDate.brazilianDateToTimeInMillis() ?: 0L
                                )
                            )*/
                        }
                    }

                    MonetaryInputField(
                        label = "Valor*",
                        alreadyExistsDoubleValue = false, //isBillToEdit,
                        alreadyDoubleValue = 0.0,//billDialogState.value,
                        isError = false,//billDialogState.valueErrorMessage.isNotBlank(),
                        errorMessage = "",//billDialogState.valueErrorMessage,
                        onMonetaryValueChange = { doubleMonetaryValue ->
                            /*onValueChange(
                                billDialogState.copy(
                                    value = doubleMonetaryValue
                                )
                            )*/
                        },
                        modifier = Modifier.width(280.dp)
                    )

                    OutlinedTextField(
                        value = "",//billDialogState.description,
                        onValueChange = { newValue ->
                            /*if(newValue.length <= 150) {
                                onValueChange(
                                    billDialogState.copy(
                                        description = newValue
                                    )
                                )
                            }*/
                        },
                        modifier = Modifier
                            .width(280.dp)
                            .focusRequester(focusRequester[3]),
                        label = {
                            Text(
                                text = "Descrição",
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        placeholder = {
                            Text(
                                text = "Descreva a conta",
                                fontFamily = PoppinsFontFamily
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = "Ícone de descrição",
                                tint = White06Color
                            )
                        },
                        trailingIcon = {
                            /*if (billDialogState.description.isNotBlank()) {
                                XIcon {
                                    onValueChange(
                                        billDialogState.copy(
                                            description = ""
                                        )
                                    )
                                    focusRequester[3].requestFocus()
                                }
                            }*/
                        },
                        maxLines = 8,
                        supportingText = {},
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                    )

                    CustomCheckbox(
                        checkboxText = "",
                        alreadyChecked = false,
                    ) { }

                    CustomCheckbox(
                        checkboxText = "",
                        alreadyChecked = false,
                    ) { }

                    Spacer(Modifier.height(16.dp))

                    /*if(billDialogState.responseErrorMessage.isNotBlank()) {
                        Text(
                            text = "Erro: ${billDialogState.responseErrorMessage}",
                            color = ErrorColor,
                            fontFamily = PoppinsFontFamily,
                            modifier = Modifier.width(280.dp).padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                    }*/

                    Button(
                        onClick = {
                            /*if (isBillToEdit) {
                                onUpdateBill(billDialogState)
                            } else {
                                onAddBill(billDialogState)
                            }*/
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            //if(isBillToEdit) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Ícone de editar."
                                )
                            /*} else {
                                Icon(
                                    imageVector = Icons.Default.Save,
                                    contentDescription = "Ícone de salvar."
                                )
                            }*/

                            Text(
                                text = "ADICIONAR CONTA", //if (isBillToEdit) "ATUALIZAR CONTA" else "SALVAR CONTA",
                                color = Color.White,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }

                    }

                    if (isSubscriptionToEdit) {
                        Button(
                            onClick = { showConfirmSubscriptionDeletionDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ErrorColor
                            )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Ícone de lixeira."
                                )

                                Text(
                                    text = "EXCLUIR CONTA",
                                    fontFamily = PoppinsFontFamily
                                )
                            }

                        }
                    }
                }

                if(isModalLoading) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(SurfaceColor),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AppLoader(modifier = Modifier.size(80.dp))
                    }
                }

                if(showConfirmSubscriptionDeletionDialog) {
                    ConfirmDeletionDialog(
                        dialogTitle = "EXCLUIR ASSINATURA",
                        dialogMessage = "Tem certeza que deseja excluir a assinatura: ... ?",
                        onConfirmDeletion = {},
                        onDismiss = { showConfirmSubscriptionDeletionDialog = false }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AddOrEditSubscriptionDialogPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        AddOrEditSubscriptionDialog()
    }
}