package com.wcsm.wcsmfinanceiro.presentation.ui.view.bills

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Expand
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wcsm.wcsmfinanceiro.R
import com.wcsm.wcsmfinanceiro.domain.entity.Bill
import com.wcsm.wcsmfinanceiro.domain.entity.BillType
import com.wcsm.wcsmfinanceiro.domain.entity.Category
import com.wcsm.wcsmfinanceiro.domain.entity.PaymentType
import com.wcsm.wcsmfinanceiro.presentation.ui.component.DateRangeFilter
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.MoneyGreenColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnBackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnSecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.RedColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.TertiaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color
import com.wcsm.wcsmfinanceiro.presentation.util.toBrazilianDateString
import com.wcsm.wcsmfinanceiro.presentation.util.toBrazilianReal

@Composable
fun BillsView(
    billsViewModel: BillsViewModel = viewModel()
) {
    val configuration = LocalConfiguration.current

    val filterSelectedDateRange by billsViewModel.filterSelectedDateRange.collectAsStateWithLifecycle()

    var textFilter by remember { mutableStateOf("") }
    val textFilterFocusRequester = remember { FocusRequester() }

    val bills by billsViewModel.bills.collectAsStateWithLifecycle()
    val billModalState by billsViewModel.billModalState.collectAsStateWithLifecycle()
    val isBillModalStateValid by billsViewModel.isBillModalStateValid.collectAsStateWithLifecycle()

    var showRegisterOrEditBillDialog by remember { mutableStateOf(false) }

    var selectedBillToModal: Bill? by remember { mutableStateOf(null) }

    val deviceScreenHeight = configuration.screenHeightDp.dp

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
            onDateSelected = { startDate, endDate ->
                billsViewModel.updateFilterSelectedDateRange(
                    startDate = startDate,
                    endDate = endDate
                )
            },
            onClearFilter = { billsViewModel.clearFilters() },
            onFilter = { startDate, endDate ->
                billsViewModel.applyDateRangeFilter(
                    startDate = startDate,
                    endDate = endDate
                )
            }
        )

        HorizontalDivider(
            modifier = Modifier.padding(16.dp),
            color = OnBackgroundColor
        )

        OutlinedTextField(
            value = textFilter,
            onValueChange = {
                textFilter = it
                billsViewModel.applyTextFilter(textFilter)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .focusRequester(textFilterFocusRequester),
            placeholder = {
                Text(
                    text = "Digite para filtrar"
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.FilterAlt,
                    contentDescription = "Ícone de filtrar",
                    tint = White06Color,
                )
            },
            trailingIcon = {
                if(textFilter.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Ícone de x",
                        modifier = Modifier
                            .clickable {
                                textFilter = ""
                                textFilterFocusRequester.requestFocus()
                                billsViewModel.clearFilters()
                            },
                        tint = White06Color
                    )
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),

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
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // ABRIR DIALOG PASSANDO BILL PARA EDIÇÃO
        if (showRegisterOrEditBillDialog) {
            AddOrEditBillDialog(
                bill = selectedBillToModal,
                billModalStateInput = billModalState,
                billModalStateValidation = { billModalStateToValidate ->
                    billsViewModel.validateBillModalState(billModalStateToValidate)
                },
                isBillModalStateValidationValid = isBillModalStateValid,
                deviceScreenHeight = deviceScreenHeight,
                onConfirm = { bill ->
                    if(selectedBillToModal != null) {
                        billsViewModel.updateBill(bill)
                    } else {
                        billsViewModel.saveBill(bill)
                    }
                }
            ) {
                billModalState.resetErrorMessages()
                selectedBillToModal = null
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
private fun BillCard(
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
                        text = bill.date.toBrazilianDateString(extendedYear = false),
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