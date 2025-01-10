package com.wcsm.wcsmfinanceiro.presentation.ui.view.bills

import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Expand
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.MoneyGreenColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnBackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnSecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.RedColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.TertiaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color
import com.wcsm.wcsmfinanceiro.presentation.util.formatDateInMillisToBrazillianDate
import com.wcsm.wcsmfinanceiro.presentation.util.getPaymentTypeFromString
import com.wcsm.wcsmfinanceiro.presentation.util.toBrazilianReal

@Composable
fun BillsView(
    billsViewModel: BillsViewModel = viewModel()
) {
    val filterSelectedDateRange by billsViewModel.filterSelectedDateRange.collectAsState()
    val bills by billsViewModel.bills.collectAsState()

    var selectedFilterDate by remember { mutableStateOf("Selecione uma data") }

    var showRegisterOrEditBillDialog by remember { mutableStateOf(false) }

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
                        showRegisterOrEditBillDialog = true
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }

            FloatingActionButton(
                onClick = {
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
            RegisterOrEditBillDialog() {
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
private fun MinimizedBillCardPreview() {
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
    onDismiss: () -> Unit
) {
    val validatedBillModalState = if (bill != null) {
        BillModalState(
            id = bill.id,
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
            expired = bill.expired ?: false
        )
    } else {
        BillModalState(
            id = 0,
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
            expired = false
        )
    }

    val radioChooserOptions = listOf(PaymentType.MONEY.displayName, PaymentType.CARD.displayName)

    val focusRequester = remember { List(9) { FocusRequester() } }

    var billModalState by remember { mutableStateOf(validatedBillModalState) }

    var selectedDate by remember { mutableStateOf("") }
    var showDatePickerDialog by remember { mutableStateOf(false) }

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

            // Data DatePicker
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
                    }
                    .padding(bottom = 8.dp),
                label = {
                    Text(
                        text = "Data*",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
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
                                    selectedDate = "Selecione uma data"
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
                value = billModalState.value.toBrazilianReal(),
                onValueChange = { newValue ->
                    if(newValue.all { it.isDigit() }) {
                        billModalState = billModalState.copy(
                            value = newValue.toDoubleOrNull() ?: 0.0
                        )
                    }
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

            RadioButtonChooser(
                optionsList = radioChooserOptions,
                modifier = Modifier.width(280.dp)
            ) { selectedValue ->
                billModalState = billModalState.copy(
                    paymentType = getPaymentTypeFromString(selectedValue) ?: PaymentType.MONEY
                )
            }

            // Category DROPDOWN

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

            // Data Vencimento DatePicker

            Row(
                modifier = Modifier.width(280.dp),
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

        }
    }
}

@Preview
@Composable
private fun RegisterOrEditBillDialogPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        RegisterOrEditBillDialog {}
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
            .background(SurfaceColor)
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