package com.wcsm.wcsmfinanceiro.presentation.ui.view.bills

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Expand
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wcsm.wcsmfinanceiro.R
import com.wcsm.wcsmfinanceiro.domain.model.Bill
import com.wcsm.wcsmfinanceiro.domain.model.BillType
import com.wcsm.wcsmfinanceiro.domain.model.Category
import com.wcsm.wcsmfinanceiro.domain.model.PaymentType
import com.wcsm.wcsmfinanceiro.presentation.model.BillModalState
import com.wcsm.wcsmfinanceiro.presentation.ui.component.AppDatePicker
import com.wcsm.wcsmfinanceiro.presentation.ui.component.DateRangeFilter
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.ErrorColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.GrayColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.MoneyGreenColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnBackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnSecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnSurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.RedColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.TertiaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color
import com.wcsm.wcsmfinanceiro.presentation.util.formatDateInMillisToBrazillianDate
import com.wcsm.wcsmfinanceiro.presentation.util.getBillTypeFromString
import com.wcsm.wcsmfinanceiro.presentation.util.getPaymentTypeFromString
import com.wcsm.wcsmfinanceiro.presentation.util.toBill
import com.wcsm.wcsmfinanceiro.presentation.util.toBrazilianReal

@Composable
fun BillsView(
    billsViewModel: BillsViewModel = viewModel()
) {
    val filterSelectedDateRange by billsViewModel.filterSelectedDateRange.collectAsStateWithLifecycle()
    val bills by billsViewModel.bills.collectAsStateWithLifecycle()
    val billModalState by billsViewModel.billModalState.collectAsStateWithLifecycle()
    val isBillModalStateValid by billsViewModel.isBillModalStateValid.collectAsStateWithLifecycle()

    var selectedFilterDate by remember { mutableStateOf("Selecione uma data") }

    var showRegisterOrEditBillDialog by remember { mutableStateOf(false) }

    var selectedBillToModal: Bill? by remember { mutableStateOf(null) }


    LaunchedEffect(filterSelectedDateRange) {
        if (filterSelectedDateRange.isNotEmpty()) {
            selectedFilterDate = filterSelectedDateRange
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CONTAS",
            color = PrimaryColor,
            modifier = Modifier.padding(vertical = 8.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            fontFamily = PoppinsFontFamily
        )

        DateRangeFilter(
            filterSelectedDateRange = filterSelectedDateRange,
            onDateSelected = { selectedDate ->
                billsViewModel.updateFilterSelectedDateRange(
                    dateRange = selectedDate
                )
            },
            onFilter = { TODO("Ao realizar o filtro") }
        )

        HorizontalDivider(
            modifier = Modifier.padding(16.dp),
            color = OnBackgroundColor
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = bills ?: emptyList(),
                    key = { bill -> bill.id }
                ) { bill ->
                    BillCard(bill = bill) {
                        selectedBillToModal = bill
                        showRegisterOrEditBillDialog = true
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }

            FloatingActionButton(
                onClick = {
                    selectedBillToModal = null
                    showRegisterOrEditBillDialog = true
                },
                modifier = Modifier.align(Alignment.BottomEnd),
                containerColor = PrimaryColor,
                contentColor = OnSecondaryColor
            ) {
                Icon(
                    painter = painterResource(R.drawable.add_bill),
                    contentDescription = "Ícone de adicionar conta",
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        // ABRIR DIALOG PASSANDO BILL PARA EDIÇÃO
        if (showRegisterOrEditBillDialog) {
            RegisterOrEditBillDialog(
                bill = selectedBillToModal,
                billModalStateInput = billModalState,
                billModalStateValidation = { billModalStateToValidate ->
                    billsViewModel.validateBillModalState(billModalStateToValidate)
                },
                isBillModalStateValidationValid = isBillModalStateValid,
                onConfirm = { bill ->
                    if(selectedBillToModal != null) {
                        billsViewModel.updateBill(bill)
                    } else {
                        billsViewModel.saveBill(bill)
                    }
                }
            ) {
                showRegisterOrEditBillDialog = false
            }
        }
    }
}

@Preview
@Composable
private fun BillsViewPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        BillsView()
    }
}

@Composable
fun BillCard(
    bill: Bill,
    onExpandBillCard: () -> Unit
) {
    val titleAndPriceColor = if (bill.billType == BillType.INCOME) MoneyGreenColor else RedColor
    ElevatedCard(
        modifier = Modifier.clickable { onExpandBillCard() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = bill.title,
                        color = titleAndPriceColor,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = PoppinsFontFamily,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = bill.date.formatDateInMillisToBrazillianDate(extendedYear = false),
                        fontFamily = PoppinsFontFamily
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = bill.value.toBrazilianReal(),
                        color = titleAndPriceColor,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PoppinsFontFamily
                    )

                    Text(
                        text = bill.category?.title ?: "Sem categoria",
                        color = TertiaryColor,
                        fontFamily = PoppinsFontFamily
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.Expand,
                contentDescription = "Ícone de expandir",
                tint = White06Color
            )
        }
    }
}

@Preview
@Composable
private fun BillCardPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        val incomeBill = Bill(
            id = 1,
            billType = BillType.INCOME,
            origin = "Trabalho",
            title = "Salário",
            value = 2624.72,
            description = "Salário do mês de Janeiro",
            date = 1736208000000, // Mon Jan 06 2025 21:00:00.000
            category = Category(1, "Trabalho"),
            dueDate = 1739588400000, // Sat Feb 15 2025 00:00:00.000
            expired = false,
            paid = true,
            paymentType = PaymentType.MONEY,
            tags = listOf("Salario", "Trabalho")
        )

        val expenseBill = Bill(
            id = 1,
            billType = BillType.EXPENSE,
            origin = "Mercado",
            title = "Compra do Mês",
            value = 975.35,
            description = "Compra do mês de Janeiro",
            date = 1736208000000, // Mon Jan 06 2025 21:00:00.000
            category = Category(1, "Mercado"),
            dueDate = 1739588400000, // Sat Feb 15 2025 00:00:00.000
            expired = false,
            paid = true,
            paymentType = PaymentType.CARD,
            tags = listOf("Mercado")
        )

        Column(
            modifier = Modifier
                .size(width = 400.dp, height = 250.dp)
                .background(BackgroundColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BillCard(bill = incomeBill) {}

            Spacer(Modifier.height(16.dp))

            BillCard(bill = expenseBill) {}
        }
    }
}

@Composable
fun RegisterOrEditBillDialog(
    bill: Bill? = null,
    billModalStateInput: BillModalState,
    billModalStateValidation: (billModalState: BillModalState) -> Unit,
    isBillModalStateValidationValid: Boolean,
    onConfirm: (bill: Bill) -> Unit,
    onDismiss: () -> Unit
) {
    val paymentTypeRadioChooserOptions = listOf(PaymentType.MONEY.displayName, PaymentType.CARD.displayName)

    val billTypeRadioChooserOptions = listOf(BillType.INCOME.displayName, BillType.EXPENSE.displayName)

    val focusRequester = remember { List(9) { FocusRequester() } }

    var selectedDate by remember { mutableStateOf("") }
    var showDatePickerDialog by remember { mutableStateOf(false) }

    var selectedDueDate by remember { mutableStateOf("") }
    var showDueDatePickerDialog by remember { mutableStateOf(false) }

    var tagsToAdd by remember { mutableStateOf("") }

    var billModalState by remember { mutableStateOf(billModalStateInput) }

    LaunchedEffect(Unit) {
        if(bill != null) {
            billModalState = billModalStateInput.copy(
                id = bill.id,
                billType = bill.billType,
                origin = bill.origin ?: "",
                title = bill.title,
                titleErrorMessage = "",
                date = bill.date,
                dateErrorMessage = "",
                description = bill.description ?: "",
                value = bill.value,
                valueErrorMessage = "",
                paymentType = bill.paymentType ?: PaymentType.MONEY,
                category = bill.category ?: Category(0L, ""),
                paid = bill.paid ?: false,
                dueDate = bill.dueDate ?: 0L,
                expired = bill.expired ?: false,
                tags = bill.tags ?: emptyList()
            )
        }
    }

    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .fillMaxWidth()
                .background(SurfaceColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if(bill != null) "EDITAR CONTA" else "ADICIONAR CONTA",
                modifier = Modifier.padding(bottom = 8.dp),
                color = PrimaryColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                RadioButtonChooser(
                    optionsList = billTypeRadioChooserOptions,
                    modifier = Modifier.width(280.dp).padding(bottom = 16.dp)
                ) { selectedValue ->
                    billModalState = billModalState.copy(
                        billType = getBillTypeFromString(selectedValue) ?: BillType.INCOME
                    )
                }

                OutlinedTextField(
                    value = billModalState.origin,
                    onValueChange = {
                        if(billModalState.origin.length < 50) {
                            billModalState = billModalState.copy(
                                origin = it
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
                            text = "Digite a origem da compra"
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
                        if(billModalState.origin.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Ícone de x",
                                modifier = Modifier
                                    .clickable {
                                        billModalState = billModalState.copy(
                                            origin = ""
                                        )
                                        focusRequester[0].requestFocus()
                                    },
                                tint = White06Color
                            )
                        }
                    },
                    singleLine = true,
                    supportingText = {},
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                )

                OutlinedTextField(
                    value = billModalState.title,
                    onValueChange = {
                        if(billModalState.title.length < 50) {
                            billModalState = billModalState.copy(
                                title = it
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
                            text = "Digite o título da conta"
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
                        if(billModalState.title.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Ícone de x",
                                modifier = Modifier
                                    .clickable {
                                        billModalState = billModalState.copy(
                                            title = ""
                                        )
                                        focusRequester[1].requestFocus()
                                    },
                                tint = White06Color
                            )
                        }
                    },
                    singleLine = true,
                    isError = billModalState.titleErrorMessage.isNotEmpty(),
                    supportingText = {
                        if(billModalState.titleErrorMessage.isNotEmpty()) {
                            Text(
                                text = billModalState.titleErrorMessage
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
                        .focusRequester(focusRequester[2])
                        .onFocusEvent {
                            if(it.isFocused) {
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
                    supportingText = {},
                    isError = billModalState.dateErrorMessage.isNotEmpty(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Ícone de Calendário",
                            tint = White06Color
                        )
                    },
                    trailingIcon = {
                        if(selectedDate != "") {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Ícone de x",
                                modifier = Modifier
                                    .clickable {
                                        selectedDate = ""
                                        focusRequester[2].requestFocus()
                                    },
                                tint = White06Color
                            )
                        }
                    },
                    readOnly = true
                )
                if(showDatePickerDialog) {
                    AppDatePicker(
                        onDismiss = {
                            showDatePickerDialog = false
                        }
                    ) { selectedDateResult ->
                        selectedDate = selectedDateResult
                    }
                }

                OutlinedTextField(
                    value = billModalState.description,
                    onValueChange = {
                        if(billModalState.description.length < 100) {
                            billModalState = billModalState.copy(
                                description = it
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
                            text = "Descreva a conta"
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
                        if(billModalState.description.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Ícone de x",
                                modifier = Modifier
                                    .clickable {
                                        billModalState = billModalState.copy(
                                            description = ""
                                        )
                                        focusRequester[3].requestFocus()
                                    },
                                tint = White06Color
                            )
                        }
                    },
                    singleLine = true,
                    supportingText = {},
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                )

                OutlinedTextField(
                    //value = billModalState.value.toBrazilianReal(),
                    value = if(billModalState.value == 0.0) "" else billModalState.value.toString(),
                    onValueChange = { newValue ->
                        /*if(newValue.all { it.isDigit() }) {
                            billModalState = billModalState.copy(
                                value = newValue.toDoubleOrNull() ?: 0.0
                            )
                        }*/
                    },
                    modifier = Modifier
                        .width(280.dp)
                        .focusRequester(focusRequester[4]),
                    label = {
                        Text(
                            text = "Valor*",
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    placeholder = {
                        Text(
                            text = "Digite o valor"
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = "Ícone de dinheiro",
                            tint = White06Color
                        )
                    },
                    trailingIcon = {
                        if(billModalState.value != 0.0) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Ícone de x",
                                modifier = Modifier
                                    .clickable {
                                        billModalState = billModalState.copy(
                                            value = 0.0
                                        )
                                        focusRequester[4].requestFocus()
                                    },
                                tint = White06Color
                            )
                        }
                    },
                    singleLine = true,
                    isError = billModalState.valueErrorMessage.isNotEmpty(),
                    supportingText = {
                        if(billModalState.valueErrorMessage.isNotEmpty()) {
                            Text(
                                text = billModalState.valueErrorMessage
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                )

                // Data Vencimento DatePicker
                OutlinedTextField(
                    value = selectedDueDate,
                    onValueChange = {},
                    modifier = Modifier
                        .focusRequester(focusRequester[5])
                        .onFocusEvent {
                            if(it.isFocused) {
                                showDatePickerDialog = true
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
                        if(selectedDueDate != "") {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Ícone de x",
                                modifier = Modifier
                                    .clickable {
                                        selectedDueDate = ""
                                        focusRequester[5].requestFocus()
                                    },
                                tint = White06Color
                            )
                        }
                    },
                    readOnly = true
                )
                if(showDueDatePickerDialog) {
                    AppDatePicker(
                        onDismiss = {
                            showDueDatePickerDialog = false
                        }
                    ) { selectedDateResult ->
                        selectedDueDate = selectedDateResult
                    }
                }

                CategoriesDropdown(modifier = Modifier.padding(bottom = 8.dp))

                RadioButtonChooser(
                    optionsList = paymentTypeRadioChooserOptions,
                    modifier = Modifier.width(280.dp)
                ) { selectedValue ->
                    billModalState = billModalState.copy(
                        paymentType = getPaymentTypeFromString(selectedValue) ?: PaymentType.MONEY
                    )
                }

                Row(
                    modifier = Modifier.width(280.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = billModalState.paid,
                        onCheckedChange = {
                            billModalState = billModalState.copy(
                                paid = !billModalState.paid
                            )
                        }
                    )

                    Text(
                        text = "Paga?",
                        fontFamily = PoppinsFontFamily,
                        color = if(billModalState.paid) PrimaryColor else White06Color
                    )
                }

                // DEVE PREENCHER AUTOMATICAMENTE
                Row(
                    modifier = Modifier.width(280.dp).padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = billModalState.expired,
                        onCheckedChange = {
                            billModalState = billModalState.copy(
                                expired = !billModalState.expired
                            )
                        }
                    )

                    Text(
                        text = "Vencida?",
                        fontFamily = PoppinsFontFamily,
                        color = if(billModalState.expired) PrimaryColor else White06Color
                    )
                }

                // TAGS
                if(bill != null) {
                    TagsContainer(tags = billModalState.tags)
                } else {
                    OutlinedTextField(
                        value = tagsToAdd,
                        onValueChange = {
                            tagsToAdd = it
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
                                text = "Digite as tags separadas por ,"
                            )
                        },
                        supportingText = {
                            Text(
                                text = "Exemplo: entrada, trabalho, bonificação, 2025"
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
                            if(tagsToAdd.isNotEmpty()) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Ícone de x",
                                    modifier = Modifier
                                        .clickable {
                                            tagsToAdd = ""
                                            focusRequester[6].requestFocus()
                                        },
                                    tint = White06Color
                                )
                            }
                        },
                        maxLines = 3,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                    )
                }

                Spacer(Modifier.heightIn(16.dp))

                Button(
                    onClick = {
                        // RESET ERROR MESSAGES
                        billModalState.resetErrorMessages()

                        // VALIDATION
                        billModalStateValidation(billModalState)

                        if(isBillModalStateValidationValid) {
                            if(billModalState.id == -1L) {
                                // NEW BILL
                                onConfirm(billModalState.toBill())
                            } else {
                                // UPDATE BILL
                                onConfirm(billModalState.toBill())
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = if(bill != null) "ATUALIZAR" else "SALVAR"
                    )
                }

                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ErrorColor
                    )
                ) {
                    Text(
                        text = "CANCELAR"
                    )
                }

            }
        }
    }
}

@Preview
@Composable
private fun RegisterOrEditBillDialogPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        val billModalState = BillModalState(
            id = 0,
            billType = BillType.INCOME,
            origin = "",
            title = "",
            titleErrorMessage = "",
            date = 0L,
            dateErrorMessage = "",
            description = "",
            value = 0.0,
            valueErrorMessage = "",
            paymentType = PaymentType.MONEY,
            category = Category(0L, ""),
            paid = false,
            dueDate = 0L,
            expired = false,
            tags = emptyList()
        )
        RegisterOrEditBillDialog(
            billModalStateInput = billModalState,
            billModalStateValidation = {},
            isBillModalStateValidationValid = true,
            onConfirm = {}
        ) {}
    }
}


@Composable
fun RadioButtonChooser(
    optionsList: List<String>,
    modifier: Modifier = Modifier,
    onOptionSelected: (String) -> Unit
) {
    val radioOptions by remember { mutableStateOf(optionsList) }
    val (selectedOption, setOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .border(1.dp, White06Color, RoundedCornerShape(15.dp))
            .selectableGroup()
    ) {
        radioOptions.forEach { text ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            setOptionSelected(text)
                            onOptionSelected(text)
                        },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = null
                )
                Text(
                    text = text,
                    color = if(text == selectedOption) PrimaryColor else White06Color,
                    fontFamily = PoppinsFontFamily,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun RadioButtonChooserPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        val options = listOf(PaymentType.MONEY.displayName, PaymentType.CARD.displayName)
        RadioButtonChooser(optionsList = options) {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesDropdown(
    modifier: Modifier = Modifier
) {
    var category by remember { mutableStateOf("") }

    val categoriesDrowpdownOptions = listOf(
        Category(0, "Saúde", Icons.Default.Healing),
        Category(1, "Mercado", Icons.Default.ShoppingCart),
        Category(2, "Farmácia", Icons.Default.LocalPharmacy),
        Category(3, "Lazer", Icons.Default.ShoppingBag),
        Category(4, "Manutenção", Icons.Default.Build),
    )
    var showCategoriesDropdown by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        ExposedDropdownMenuBox(
            expanded = showCategoriesDropdown,
            onExpandedChange = { showCategoriesDropdown = !showCategoriesDropdown }
        ) {
            OutlinedTextField(
                modifier = Modifier.menuAnchor().width(280.dp),
                value = category,
                onValueChange = {
                    showCategoriesDropdown = !showCategoriesDropdown
                },
                label = {
                    Text(
                        text = "Categoria",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                singleLine = true,
                //isError = installmentFieldErrorMessage.isNotEmpty(),
                supportingText = {},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Category,
                        contentDescription = "Ícone de categoria",
                        tint = White06Color
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector =
                        if (category != "") Icons.Filled.KeyboardArrowUp
                        else Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Ícone de seta para cima ou para baixo",
                        tint = White06Color
                    )
                },
                readOnly = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.None)
            )

            ExposedDropdownMenu(
                expanded = showCategoriesDropdown,
                onDismissRequest = { showCategoriesDropdown = false }
            ) {
                categoriesDrowpdownOptions.forEach { categorySelected ->
                    DropdownMenuItem(
                        text = {
                            Text(text = categorySelected.title)
                        },
                        onClick = {
                            category = categorySelected.title
                            showCategoriesDropdown = false
                        }
                    )
                }
            }
        }
    }

}

@Preview
@Composable
private fun CategoriesDropdownPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        Column(
            modifier = Modifier.size(350.dp, 100.dp).background(BackgroundColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CategoriesDropdown()
        }
    }
}

@Composable
fun Tag(
    tag: String,
    onDeleteTag: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(25.dp))
            .background(GrayColor)
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = tag,
            color = OnSurfaceColor,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(end = 8.dp).weight(1f)
        )

        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = "Ícone de x",
            tint = White06Color,
            modifier = Modifier
                .clip(CircleShape)
                .clickable {
                    onDeleteTag()
                }
        )
    }
}

@Preview
@Composable
private fun TagPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        Tag("Lazer") {}
    }
}

@Composable
fun TagsContainer(tags: List<String>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .heightIn(0.dp, 200.dp)
            .clip(RoundedCornerShape(15.dp))
            .border(1.dp, White06Color, RoundedCornerShape(15.dp))
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(tags) { tag ->
            Tag(tag) { /* ON DELETe TAG */ }
        }
    }
}

@Preview
@Composable
private fun TagsContainerPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        val tags = listOf("Entrada", "Trabalho", "Bonificação", "2025")

        Column(
            modifier = Modifier.size(350.dp, 100.dp).background(BackgroundColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TagsContainer(tags = tags)
        }
    }
}
