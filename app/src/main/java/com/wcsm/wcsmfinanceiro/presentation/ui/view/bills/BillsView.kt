package com.wcsm.wcsmfinanceiro.presentation.ui.view.bills

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wcsm.wcsmfinanceiro.R
import com.wcsm.wcsmfinanceiro.presentation.model.BillOperationType
import com.wcsm.wcsmfinanceiro.presentation.model.WalletOperationType
import com.wcsm.wcsmfinanceiro.presentation.ui.component.AppLoader
import com.wcsm.wcsmfinanceiro.presentation.ui.component.DateRangeFilter
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnBackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnSecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color
import com.wcsm.wcsmfinanceiro.presentation.util.showToastMessage
import com.wcsm.wcsmfinanceiro.presentation.util.toBillState

@Composable
fun BillsView() {
    val billsViewModel: BillsViewModel = hiltViewModel()

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val configuration = LocalConfiguration.current

    var textFilter by remember { mutableStateOf("") }

    val bills by billsViewModel.bills.collectAsStateWithLifecycle()
    val uiState by billsViewModel.uiState.collectAsStateWithLifecycle()
    val filterSelectedDateRange by billsViewModel.filterSelectedDateRange.collectAsStateWithLifecycle()

    var showAddOrEditBillDialog by remember { mutableStateOf(false) }

    val deviceScreenHeight = configuration.screenHeightDp.dp

    var isLoading by remember { mutableStateOf(uiState.isLoading) }

    LaunchedEffect(textFilter) {
        if(filterSelectedDateRange != null && textFilter.isNotBlank()) {
            billsViewModel.clearFilter()
        }
    }

    LaunchedEffect(uiState) {

        isLoading = uiState.isLoading

        uiState.error?.let { responseErrorMessage ->
            billsViewModel.updateBillState(
                billsViewModel.billStateFlow.value.copy(
                    responseErrorMessage = responseErrorMessage
                )
            )
        }

        if(uiState.success) {
            uiState.operationType?.let { operationType ->
                when(operationType) {
                    BillOperationType.SAVE -> {
                        showToastMessage(context, "Conta salva!")
                    }
                    BillOperationType.UPDATE -> {
                        showToastMessage(context, "Conta atualizada!")

                    }
                    BillOperationType.DELETE -> {
                        showToastMessage(context, "Conta removida!")

                    }
                }
            }

            billsViewModel.resetUiState()
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
            onDateSelected = { startDate, endDate ->
                billsViewModel.updateFilterSelectedDateRange(
                    Pair(startDate, endDate)
                )
            },
            onClearFilter = {
                textFilter = ""
                billsViewModel.clearFilter()
            },
            onFilter = { startDate, endDate ->
                billsViewModel.applyDateRangeFilter(
                    startDate = startDate,
                    endDate = endDate
                )
                textFilter = ""
                focusManager.clearFocus()
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
                .padding(horizontal = 16.dp),
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
                if(textFilter.isNotBlank()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Ícone de x",
                        modifier = Modifier
                            .clickable {
                                textFilter = ""
                                billsViewModel.clearFilter()
                            },
                        tint = White06Color
                    )
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                }
            )
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
            if(isLoading) {
                AppLoader(
                    modifier = Modifier.size(80.dp).align(Alignment.Center)
                )
            } else {
                if(bills?.isEmpty() == true) {
                    Text(
                        text = "Sem contas no momento.",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = bills ?: emptyList()
                        ) { bill ->
                            BillCard(bill = bill) {
                                billsViewModel.updateBillState(
                                    bill.toBillState()
                                )
                                showAddOrEditBillDialog = true
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(60.dp))
                        }
                    }
                }
                FloatingActionButton(
                    onClick = {
                        showAddOrEditBillDialog = true
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
        }

        if (showAddOrEditBillDialog) {
            AddOrEditBillDialog(
                billStateFlow = billsViewModel.billStateFlow,
                uiStateFlow = billsViewModel.uiState,
                onValueChange = { updatedValue ->
                    billsViewModel.updateBillState(updatedValue)
                },
                deviceScreenHeight = deviceScreenHeight,
                onAddBill = { billState ->
                    billsViewModel.saveBill(billState)
                },
                onUpdateBill = { billState ->
                    billsViewModel.updateBill(billState)
                },
                onDeleteTag = { tagToDelete ->
                    billsViewModel.deleteTag(tagToDelete)
                },
                onDeleteBill = { billState ->
                    billsViewModel.deleteBill(billState)
                },
                onDismiss = {
                    billsViewModel.resetBillState()
                    showAddOrEditBillDialog = false
                }
            )
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