package com.wcsm.wcsmfinanceiro.presentation.ui.view.bills

import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Expand
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wcsm.wcsmfinanceiro.R
import com.wcsm.wcsmfinanceiro.data.entity.Bill
import com.wcsm.wcsmfinanceiro.data.model.BillType
import com.wcsm.wcsmfinanceiro.data.model.PaymentType
import com.wcsm.wcsmfinanceiro.presentation.ui.component.AppLoader
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
import com.wcsm.wcsmfinanceiro.presentation.util.toBillState
import com.wcsm.wcsmfinanceiro.presentation.util.toBrazilianDateString
import com.wcsm.wcsmfinanceiro.presentation.util.toBrazilianReal
import kotlinx.coroutines.delay

@Composable
fun BillsView() {
    val billsViewModel: BillsViewModel = hiltViewModel()

    val focusManager = LocalFocusManager.current
    val configuration = LocalConfiguration.current

    var textFilter by remember { mutableStateOf("") }

    val bills by billsViewModel.bills.collectAsStateWithLifecycle()
    val isLoading by remember { mutableStateOf(false) }
    val filterSelectedDateRange by billsViewModel.filterSelectedDateRange.collectAsStateWithLifecycle()

    var showAddOrEditBillDialog by remember { mutableStateOf(false) }

    val deviceScreenHeight = configuration.screenHeightDp.dp

    LaunchedEffect(textFilter) {
        if(filterSelectedDateRange != null && textFilter.isNotBlank()) {
            billsViewModel.clearFilter()
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
                                billsViewModel.updateBillDialogState(
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
                billState = billsViewModel.billDialogState,
                isAddOrEditSuccess = billsViewModel.isAddOrEditSuccess,
                isBillDeleted = billsViewModel.isBillDeleted,
                onValueChange = { updatedValue ->
                    billsViewModel.updateBillDialogState(updatedValue)
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
                    billsViewModel.resetBillDialogState()
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