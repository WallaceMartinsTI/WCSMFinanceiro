package com.wcsm.wcsmfinanceiro.presentation.ui.view.bills.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wcsm.wcsmfinanceiro.data.local.model.BillType
import com.wcsm.wcsmfinanceiro.data.local.model.PaymentType
import com.wcsm.wcsmfinanceiro.presentation.model.CrudOperationType
import com.wcsm.wcsmfinanceiro.presentation.model.UiState
import com.wcsm.wcsmfinanceiro.presentation.model.bills.BillState
import com.wcsm.wcsmfinanceiro.presentation.ui.component.AppDatePicker
import com.wcsm.wcsmfinanceiro.presentation.ui.component.AppLoader
import com.wcsm.wcsmfinanceiro.presentation.ui.component.ConfirmDeletionDialog
import com.wcsm.wcsmfinanceiro.presentation.ui.component.CustomCheckbox
import com.wcsm.wcsmfinanceiro.presentation.ui.component.MonetaryInputField
import com.wcsm.wcsmfinanceiro.presentation.ui.component.RadioButtonChooser
import com.wcsm.wcsmfinanceiro.presentation.ui.component.XIcon
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.ErrorColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnBackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color
import com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet.WalletViewModel
import com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet.components.WalletDropdownChooser
import com.wcsm.wcsmfinanceiro.util.brazilianDateToTimeInMillis
import com.wcsm.wcsmfinanceiro.util.getBillTypeFromString
import com.wcsm.wcsmfinanceiro.util.getFormattedTags
import com.wcsm.wcsmfinanceiro.util.getPaymentTypeFromString
import com.wcsm.wcsmfinanceiro.util.toBrazilianDateString
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AddOrEditBillDialog(
    walletViewModel: WalletViewModel,
    billStateFlow: StateFlow<BillState>,
    uiStateFlow: StateFlow<UiState<CrudOperationType>>,
    onValueChange: (updatedValue: BillState) -> Unit,
    deviceScreenHeight: Dp,
    onAddBill: (billState: BillState) -> Unit,
    onUpdateBill: (billState: BillState) -> Unit,
    onDeleteTag: (tag: String) -> Unit,
    onDeleteBill: (billState: BillState) -> Unit,
    onDismiss: () -> Unit
) {
    val billDialogState by billStateFlow.collectAsStateWithLifecycle()
    val uiState by uiStateFlow.collectAsStateWithLifecycle()

    val paymentTypeRadioChooserOptions =
        listOf(PaymentType.MONEY.displayName, PaymentType.CARD.displayName)

    val billTypeRadioChooserOptions =
        listOf(BillType.INCOME.displayName, BillType.EXPENSE.displayName)

    val focusRequester = remember { List(7) { FocusRequester() } }

    var selectedDate by remember { mutableStateOf("") }
    var showDatePickerDialog by remember { mutableStateOf(false) }

    var selectedDueDate by remember { mutableStateOf("") }
    var showDueDatePickerDialog by remember { mutableStateOf(false) }

    var tagsToAdd by remember { mutableStateOf("") }

    val isBillToEdit by remember { mutableStateOf(billDialogState.billId != "") }
    var isModalLoading by remember { mutableStateOf(isBillToEdit) }

    var showConfirmBillDeletionDialog by remember { mutableStateOf(false) }

    var billHasTag: Boolean? by remember { mutableStateOf(null) }

    var showExpenseFormData by remember { mutableStateOf(false) }

    var selectedWallet: String? by remember { mutableStateOf(null) }

    // Wallet Communication
    val walletsWithCards by walletViewModel.walletsWithCards.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        billHasTag = billDialogState.tags.isNotEmpty()

        if (isBillToEdit) {
            selectedDate = if(billDialogState.date == 0L) "" else (billDialogState.date.toBrazilianDateString())
            selectedDueDate = if(billDialogState.dueDate == 0L) "" else (billDialogState.dueDate.toBrazilianDateString())

            val walletWithBill = walletsWithCards?.find { walletWithCard ->
                walletWithCard.wallet.walletBills.any { it.first == billDialogState.billId }
            }
            if(walletWithBill != null) {
                selectedWallet = walletWithBill.wallet.title
            }

            delay(1500)
            isModalLoading = false
        }
    }

    LaunchedEffect(billDialogState.billType) {
        showExpenseFormData = billDialogState.billType == BillType.EXPENSE
    }

    LaunchedEffect(billDialogState.tags) {
        if(billDialogState.tags.isEmpty()) {
            billHasTag = false
        }
    }

    LaunchedEffect(billDialogState.date, billDialogState.dueDate) {
        onValueChange(
            billDialogState.copy(
                expired = billDialogState.dueDate != 0L && billDialogState.date > billDialogState.dueDate
            )
        )
    }

    LaunchedEffect(uiState) {
        uiState.operationType?.let {
            if(uiState.success) {
                onDismiss()
            }
        }
    }

    LaunchedEffect(tagsToAdd) {
        if(tagsToAdd.isNotBlank()) {
            onValueChange(
                billDialogState.copy(
                    tags = getFormattedTags(tagsToAdd)
                )
            )
        }
    }

    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        val dialogHeight = deviceScreenHeight * 0.8f // 80% of device height
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .height(dialogHeight)
                .background(SurfaceColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = if (isBillToEdit) "EDITAR CONTA" else "ADICIONAR CONTA",
                    modifier = Modifier.align(Alignment.Center),
                    color = PrimaryColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )

                XIcon(
                    modifier = Modifier.align(Alignment.CenterEnd).size(40.dp)
                ) {
                    onDismiss()
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = OnBackgroundColor
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    RadioButtonChooser(
                        inputedOption = if (isBillToEdit) billDialogState.billType.displayName else null,
                        optionsList = billTypeRadioChooserOptions,
                        modifier = Modifier
                            .width(280.dp)
                            .padding(bottom = 16.dp)
                    ) { selectedValue ->
                        onValueChange(
                            billDialogState.copy(
                                billType = getBillTypeFromString(selectedValue)
                            )
                        )
                    }

                    //if(!isModalLoading) {
                        WalletDropdownChooser(
                            walletWithCards = walletsWithCards ?: emptyList(),
                            selectedWallet = selectedWallet,
                            isError = billDialogState.walletWithCardsErrorMessage.isNotBlank(),
                            errorMessage = billDialogState.walletWithCardsErrorMessage
                        ) { selectedWallet ->
                            onValueChange(
                                billDialogState.copy(
                                    walletWithCards = selectedWallet
                                )
                            )
                        }
                    //}

                    BillCategoriesDropdown(
                        inputtedOption = if (isBillToEdit) billDialogState.category else null,
                        isError = billDialogState.categoryErrorMessage.isNotBlank(),
                        errorMessage = billDialogState.categoryErrorMessage,
                        modifier = Modifier.padding(bottom = 8.dp),
                        onValueSelected = { selectedCategory ->
                            onValueChange(
                                billDialogState.copy(
                                    category = selectedCategory
                                )
                            )
                        }
                    )

                    OutlinedTextField(
                        value = billDialogState.origin,
                        onValueChange = { newValue ->
                            if(newValue.length <= 30) {
                                onValueChange(
                                    billDialogState.copy(
                                        origin = newValue
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .width(280.dp)
                            .focusRequester(focusRequester[0]),
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
                            if (billDialogState.origin.isNotBlank()) {
                                XIcon {
                                    onValueChange(
                                        billDialogState.copy(
                                            origin = ""
                                        )
                                    )
                                    focusRequester[0].requestFocus()
                                }
                            }
                        },
                        singleLine = true,
                        supportingText = {},
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                    )

                    OutlinedTextField(
                        value = billDialogState.title,
                        onValueChange = { newValue ->
                            if (newValue.length <= 50) {
                                onValueChange(
                                    billDialogState.copy(
                                        title = newValue
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .width(280.dp)
                            .focusRequester(focusRequester[1]),
                        label = {
                            Text(
                                text = "Título*",
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        placeholder = {
                            Text(
                                text = "Digite o título da conta",
                                fontFamily = PoppinsFontFamily
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.NoteAlt,
                                contentDescription = "Ícone de anotação",
                                tint = White06Color
                            )
                        },
                        trailingIcon = {
                            if (billDialogState.title.isNotBlank()) {
                                XIcon {
                                    onValueChange(
                                        billDialogState.copy(
                                            title = ""
                                        )
                                    )
                                    focusRequester[1].requestFocus()
                                }
                            }
                        },
                        singleLine = true,
                        isError = billDialogState.titleErrorMessage.isNotBlank(),
                        supportingText = {
                            if (billDialogState.titleErrorMessage.isNotBlank()) {
                                Text(
                                    text = billDialogState.titleErrorMessage,
                                    fontFamily = PoppinsFontFamily,
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                    )

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
                            if (billDialogState.dateErrorMessage.isNotBlank()) {
                                Text(
                                    text = billDialogState.dateErrorMessage,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        },
                        isError = billDialogState.dateErrorMessage.isNotBlank(),
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
                                    onValueChange(
                                        billDialogState.copy(
                                            date = 0L
                                        )
                                    )
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
                            selectedDate = selectedDateResult.toBrazilianDateString()
                            onValueChange(
                                billDialogState.copy(
                                    date = selectedDate.brazilianDateToTimeInMillis() ?: 0L
                                )
                            )
                        }
                    }

                    MonetaryInputField(
                        label = "Valor*",
                        alreadyExistsDoubleValue = isBillToEdit,
                        alreadyDoubleValue = billDialogState.value,
                        isError = billDialogState.valueErrorMessage.isNotBlank(),
                        errorMessage = billDialogState.valueErrorMessage,
                        onMonetaryValueChange = { doubleMonetaryValue ->
                            onValueChange(
                                billDialogState.copy(
                                    value = doubleMonetaryValue
                                )
                            )
                        },
                        modifier = Modifier.width(280.dp)
                    )

                    OutlinedTextField(
                        value = billDialogState.description,
                        onValueChange = { newValue ->
                            if(newValue.length <= 150) {
                                onValueChange(
                                    billDialogState.copy(
                                        description = newValue
                                    )
                                )
                            }
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
                            if (billDialogState.description.isNotBlank()) {
                                XIcon {
                                    onValueChange(
                                        billDialogState.copy(
                                            description = ""
                                        )
                                    )
                                    focusRequester[3].requestFocus()
                                }
                            }
                        },
                        maxLines = 8,
                        supportingText = {},
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                    )

                    if(showExpenseFormData) {
                        OutlinedTextField(
                            value = selectedDueDate,
                            onValueChange = {},
                            modifier = Modifier
                                .width(280.dp)
                                .focusRequester(focusRequester[5])
                                .onFocusEvent {
                                    if (it.isFocused) {
                                        showDueDatePickerDialog = true
                                        focusRequester[5].freeFocus()
                                    }
                                },
                            label = {
                                Text(
                                    text = "Data de Vencimento",
                                    style = MaterialTheme.typography.labelMedium
                                )
                            },
                            supportingText = {},
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = "Ícone de Calendário",
                                    tint = White06Color
                                )
                            },
                            trailingIcon = {
                                if (selectedDueDate != "") {
                                    XIcon {
                                        selectedDueDate = ""
                                        onValueChange(
                                            billDialogState.copy(
                                                dueDate = 0L
                                            )
                                        )
                                        focusRequester[5].requestFocus()
                                    }
                                }
                            },
                            readOnly = true
                        )
                        if (showDueDatePickerDialog) {
                            AppDatePicker(
                                onDismiss = {
                                    showDueDatePickerDialog = false
                                }
                            ) { selectedDueDateResult ->
                                selectedDueDate = selectedDueDateResult.toBrazilianDateString()
                                onValueChange(
                                    billDialogState.copy(
                                        dueDate = selectedDueDate.brazilianDateToTimeInMillis() ?: 0L
                                    )
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .width(280.dp)
                                .padding(bottom = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = billDialogState.expired,
                                onCheckedChange = {},
                                enabled = false
                            )

                            Text(
                                text = "Conta Vencida.",
                                fontFamily = PoppinsFontFamily,
                                color = if (billDialogState.expired) PrimaryColor else White06Color
                            )
                        }

                        RadioButtonChooser(
                            inputedOption = if (isBillToEdit) billDialogState.paymentType.displayName else null,
                            optionsList = paymentTypeRadioChooserOptions,
                            modifier = Modifier.width(280.dp)
                        ) { selectedValue ->
                            onValueChange(
                                billDialogState.copy(
                                    paymentType = getPaymentTypeFromString(selectedValue)
                                )
                            )
                        }
                    }

                    CustomCheckbox(
                        checkboxText = if(showExpenseFormData) "Paga?" else "Recebida?",
                        alreadyChecked = billDialogState.paid
                    ) { isChecked ->
                        onValueChange(
                            billDialogState.copy(
                                paid = isChecked
                            )
                        )
                    }

                    if(billHasTag == true) {
                        BillTagsContainer(tags = billDialogState.tags) { tagToDelete ->
                            onDeleteTag(tagToDelete)
                        }
                    } else {
                        OutlinedTextField(
                            value = tagsToAdd,
                            onValueChange = { newValue ->
                                if(newValue.length <= 60) {
                                    tagsToAdd = newValue
                                }
                            },
                            modifier = Modifier
                                .width(280.dp)
                                .focusRequester(focusRequester[6]),
                            label = {
                                Text(
                                    text = "Tags",
                                    style = MaterialTheme.typography.labelMedium
                                )
                            },
                            placeholder = {
                                Text(
                                    text = "Digite as tags separadas por ,",
                                    fontFamily = PoppinsFontFamily
                                )
                            },
                            supportingText = {
                                Text(
                                    text = "Exemplo: entrada, trabalho, bonificação, 2025",
                                    fontFamily = PoppinsFontFamily
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Tag,
                                    contentDescription = "Ícone de tag",
                                    tint = White06Color
                                )
                            },
                            trailingIcon = {
                                if (tagsToAdd.isNotEmpty()) {
                                    XIcon {
                                        tagsToAdd = ""
                                        focusRequester[6].requestFocus()
                                    }
                                }
                            },
                            maxLines = 3,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    if(billDialogState.responseErrorMessage.isNotBlank()) {
                        Text(
                            text = "Erro: ${billDialogState.responseErrorMessage}",
                            color = ErrorColor,
                            fontFamily = PoppinsFontFamily,
                            modifier = Modifier.width(280.dp).padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    Button(
                        onClick = {
                            if (isBillToEdit) {
                                onUpdateBill(billDialogState)
                            } else {
                                onAddBill(billDialogState)
                            }
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
                            if(isBillToEdit) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Ícone de editar."
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Save,
                                    contentDescription = "Ícone de salvar."
                                )
                            }

                            Text(
                                text = if (isBillToEdit) "ATUALIZAR CONTA" else "SALVAR CONTA",
                                color = Color.White,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }

                    }

                    if (isBillToEdit) {
                        Button(
                            onClick = { showConfirmBillDeletionDialog = true },
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

                if (isModalLoading) {
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

                if(showConfirmBillDeletionDialog) {
                    ConfirmDeletionDialog(
                        dialogTitle = "EXCLUIR CONTA",
                        dialogMessage = "Tem certeza que deseja excluir a conta: ${billDialogState.title} ?",
                        onConfirmDeletion = { onDeleteBill(billDialogState) },
                        onDismiss = { showConfirmBillDeletionDialog = false }
                    )
                }
            }
        }
    }
}

/*
@Preview
@Composable
private fun AddOrEditBillDialogPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        val billsViewModel: BillsViewModel = hiltViewModel()

        val configuration = LocalConfiguration.current
        val deviceScreenHeight = configuration.screenHeightDp.dp
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AddOrEditBillDialog(
                walletViewModel = hiltViewModel(),
                billStateFlow = billsViewModel.billStateFlow,
                uiStateFlow = billsViewModel.uiState,
                onValueChange = {},
                deviceScreenHeight = deviceScreenHeight,
                onAddBill = {},
                onUpdateBill = {},
                onDeleteTag = {},
                onDeleteBill = {},
                onDismiss = {}
            )
        }
    }
}*/
