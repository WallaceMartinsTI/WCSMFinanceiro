package com.wcsm.wcsmfinanceiro.presentation.ui.view.bills

import android.util.Log
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wcsm.wcsmfinanceiro.data.model.BillType
import com.wcsm.wcsmfinanceiro.data.model.Category
import com.wcsm.wcsmfinanceiro.data.model.PaymentType
import com.wcsm.wcsmfinanceiro.presentation.model.BillState
import com.wcsm.wcsmfinanceiro.presentation.ui.component.AppDatePicker
import com.wcsm.wcsmfinanceiro.presentation.ui.component.AppLoader
import com.wcsm.wcsmfinanceiro.presentation.ui.component.RadioButtonChooser
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.ErrorColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.GrayColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnBackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnSurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color
import com.wcsm.wcsmfinanceiro.presentation.util.CurrencyVisualTransformation
import com.wcsm.wcsmfinanceiro.presentation.util.brazilianDateToTimeInMillis
import com.wcsm.wcsmfinanceiro.presentation.util.getBillTypeFromString
import com.wcsm.wcsmfinanceiro.presentation.util.getDoubleForStringPrice
import com.wcsm.wcsmfinanceiro.presentation.util.getFormattedTags
import com.wcsm.wcsmfinanceiro.presentation.util.getPaymentTypeFromString
import com.wcsm.wcsmfinanceiro.presentation.util.toBrazilianDateString
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrEditBillDialog(
    billState: StateFlow<BillState>,
    isAddOrEditSuccess: StateFlow<Boolean>,
    isBillDeleted: StateFlow<Boolean>,
    onValueChange: (updatedValue: BillState) -> Unit,
    deviceScreenHeight: Dp,
    onAddBill: (billState: BillState) -> Unit,
    onUpdateBill: (billState: BillState) -> Unit,
    onDeleteTag: (tag: String) -> Unit,
    onDeleteBill: (billState: BillState) -> Unit,
    onDismiss: () -> Unit
) {
    val paymentTypeRadioChooserOptions =
        listOf(PaymentType.MONEY.displayName, PaymentType.CARD.displayName)

    val billTypeRadioChooserOptions =
        listOf(BillType.INCOME.displayName, BillType.EXPENSE.displayName)

    val focusRequester = remember { List(9) { FocusRequester() } }

    var selectedDate by remember { mutableStateOf("") }
    var showDatePickerDialog by remember { mutableStateOf(false) }

    var selectedDueDate by remember { mutableStateOf("") }
    var showDueDatePickerDialog by remember { mutableStateOf(false) }

    var tagsToAdd by remember { mutableStateOf("") }
    var monetaryValue by remember { mutableStateOf("") }

    val billDialogState by billState.collectAsStateWithLifecycle()
    val addOrEditWithSuccess by isAddOrEditSuccess.collectAsStateWithLifecycle()
    val billDeleteWithSuccess by isBillDeleted.collectAsStateWithLifecycle()

    val isBillToEdit by remember { mutableStateOf(billDialogState.id != 0L) }
    var isModalLoading by remember { mutableStateOf(isBillToEdit) }

    var showConfirmBillDeletionDialog by remember { mutableStateOf(false) }

    var billHasTag: Boolean? by remember { mutableStateOf(null) }
    /*
    * Usar o LaunchedEffect em Unti(rodar apenas 1x) e salvar
    * o valor fixo se tem tags ou nao, e executar a adição (feita no lAunched de tagsTo Add)
    * apenas se não tiver tag.
    * */

    //LaunchedEffect(deviceScreenHeight) {
    LaunchedEffect(Unit) {
        billHasTag = billDialogState.tags.isNotEmpty()
    }

    LaunchedEffect(billHasTag) {
        Log.i("#-# TESTE #-#", "LE -> billHasTag: $billHasTag")
        Log.i("#-# TESTE #-#", "LE -> billHasTag == true: ${billHasTag == true}")
    }

    LaunchedEffect(billDialogState) {
        if (isBillToEdit) {
            selectedDate = billDialogState.date.toBrazilianDateString()
            selectedDueDate = billDialogState.dueDate.toBrazilianDateString()

            monetaryValue = billDialogState.value.toString().replace(".", "")

            delay(1500)
            isModalLoading = false
        }

    }

    LaunchedEffect(billDialogState.date, billDialogState.dueDate) {
        onValueChange(
            billDialogState.copy(
                expired = billDialogState.dueDate != 0L && billDialogState.date > billDialogState.dueDate
            )
        )
    }

    LaunchedEffect(monetaryValue) {
        if (monetaryValue.isNotBlank()) {
            onValueChange(
                billDialogState.copy(
                    value = getDoubleForStringPrice(monetaryValue)
                )
            )
        }
    }

    LaunchedEffect(addOrEditWithSuccess) {
        if (addOrEditWithSuccess) {
            onDismiss()
        }
    }

    LaunchedEffect(billDeleteWithSuccess) {
        if(billDeleteWithSuccess) {
            showConfirmBillDeletionDialog = false
            onDismiss()
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

                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Ícone de fechar",
                    tint = White06Color,
                    modifier = Modifier
                        .clickable { onDismiss() }
                        .align(Alignment.TopEnd)
                        .size(40.dp)
                )
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

                    OutlinedTextField(
                        value = billDialogState.origin,
                        onValueChange = {
                            if (billDialogState.origin.length < 50) {
                                onValueChange(
                                    billDialogState.copy(
                                        origin = it
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
                            if (billDialogState.origin.isNotEmpty()) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Ícone de x",
                                    modifier = Modifier
                                        .clickable {
                                            onValueChange(
                                                billDialogState.copy(
                                                    origin = ""
                                                )
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
                        value = billDialogState.title,
                        onValueChange = {
                            if (billDialogState.title.length < 50) {
                                onValueChange(
                                    billDialogState.copy(
                                        title = it
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
                            if (billDialogState.title.isNotEmpty()) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Ícone de x",
                                    modifier = Modifier
                                        .clickable {
                                            onValueChange(
                                                billDialogState.copy(
                                                    title = ""
                                                )
                                            )
                                            focusRequester[1].requestFocus()
                                        },
                                    tint = White06Color
                                )
                            }
                        },
                        singleLine = true,
                        isError = billDialogState.titleErrorMessage.isNotEmpty(),
                        supportingText = {
                            if (billDialogState.titleErrorMessage.isNotEmpty()) {
                                Text(
                                    text = billDialogState.titleErrorMessage,
                                    fontFamily = PoppinsFontFamily
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
                            if (billDialogState.dateErrorMessage.isNotEmpty()) {
                                Text(
                                    text = billDialogState.titleErrorMessage,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        },
                        isError = billDialogState.dateErrorMessage.isNotEmpty(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Ícone de Calendário",
                                tint = White06Color
                            )
                        },
                        trailingIcon = {
                            if (selectedDate != "") {
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

                    OutlinedTextField(
                        value = billDialogState.description,
                        onValueChange = {
                            if (billDialogState.description.length < 100) {
                                onValueChange(
                                    billDialogState.copy(
                                        description = it
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
                            if (billDialogState.description.isNotEmpty()) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Ícone de x",
                                    modifier = Modifier
                                        .clickable {
                                            onValueChange(
                                                billDialogState.copy(
                                                    description = ""
                                                )
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
                        value = monetaryValue, //billDialogState.value.toBrazilianReal(),
                        //value = if(billDialogState.value == 0.0) "" else billDialogState.value.toString(),
                        onValueChange = { newValue ->
                            //if(newValue.all { it.isDigit() }) {
                            /*billModalState = billModalState.copy(
                                value = newValue.toDoubleOrNull() ?: 0.0
                            )*/
                            monetaryValue = newValue
                            /*onValueChange(
                                billDialogState.copy(
                                    value = newValue.toDoubleOrNull() ?: 5.25
                                )
                            )*/
                            //}
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
                            if (billDialogState.value != 0.0) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Ícone de x",
                                    modifier = Modifier
                                        .clickable {
                                            /*billDialogState = billDialogState.copy(
                                                value = 0.0
                                            )
                                            focusRequester[4].requestFocus()*/
                                        },
                                    tint = White06Color
                                )
                            }
                        },
                        singleLine = true,
                        isError = billDialogState.valueErrorMessage.isNotEmpty(),
                        supportingText = {
                            if (billDialogState.valueErrorMessage.isNotEmpty()) {
                                Text(
                                    text = billDialogState.valueErrorMessage
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        visualTransformation = CurrencyVisualTransformation()
                    )

                    OutlinedTextField(
                        value = selectedDueDate,
                        onValueChange = {},
                        modifier = Modifier
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
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Ícone de x",
                                    modifier = Modifier
                                        .clickable {
                                            selectedDueDate = ""
                                            onValueChange(
                                                billDialogState.copy(
                                                    dueDate = 0L
                                                )
                                            )
                                            focusRequester[5].requestFocus()
                                        },
                                    tint = White06Color
                                )
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

                    CategoriesDropdown(
                        inputedOption = if (isBillToEdit) billDialogState.category else null,
                        modifier = Modifier.padding(bottom = 8.dp),
                        onValueSelected = { selectedCategory ->
                            onValueChange(
                                billDialogState.copy(
                                    category = selectedCategory
                                )
                            )
                        }
                    )

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

                    Row(
                        modifier = Modifier.width(280.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = billDialogState.paid,
                            onCheckedChange = {
                                onValueChange(
                                    billDialogState.copy(
                                        paid = !billDialogState.paid
                                    )
                                )
                            }
                        )

                        Text(
                            text = "Paga?",
                            fontFamily = PoppinsFontFamily,
                            color = if (billDialogState.paid) PrimaryColor else White06Color
                        )
                    }

                    Row(
                        modifier = Modifier
                            .width(280.dp)
                            .padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = billDialogState.expired,
                            onCheckedChange = {
                                onValueChange(
                                    billDialogState.copy(
                                        expired = !billDialogState.expired
                                    )
                                )
                            },
                            enabled = false
                        )

                        Text(
                            text = "Vencida?",
                            fontFamily = PoppinsFontFamily,
                            color = if (billDialogState.expired) PrimaryColor else White06Color
                        )
                    }

                    if(billHasTag == true) {
                        TagsContainer(tags = billDialogState.tags) { tagToDelete ->
                            onDeleteTag(tagToDelete)
                        }
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
                                if (tagsToAdd.isNotEmpty()) {
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
                            if (isBillToEdit) {
                                // UPDATE BILL
                                onUpdateBill(billDialogState)
                            } else {
                                // NEW BILL
                                onAddBill(billDialogState)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = if (isBillToEdit) "ATUALIZAR" else "SALVAR"
                        )
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
                            Text(
                                text = "EXCLUIR CONTA"
                            )
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
                    BasicAlertDialog(
                        onDismissRequest = { showConfirmBillDeletionDialog = false },
                        modifier = Modifier
                            .clip(RoundedCornerShape(15.dp))
                            .border(1.dp, OnSurfaceColor, RoundedCornerShape(15.dp))
                            .background(SurfaceColor)
                            .padding(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "EXCLUIR CONTA",
                                color = PrimaryColor,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Text(
                                text = "Tem certeza que deseja excluir a conta: ${billDialogState.title}",
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = {
                                        onDeleteBill(billDialogState)
                                    },
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = ErrorColor
                                    )
                                ) {
                                    Text("EXCLUIR")
                                }

                                Button(
                                    onClick = { showConfirmBillDeletionDialog = false },
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                ) {
                                    Text("CANCELAR")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun AddOrEditBillDialogPreview(
    billsViewModel: BillsViewModel = hiltViewModel()
) {
    WCSMFinanceiroTheme(dynamicColor = false) {
        val configuration = LocalConfiguration.current
        val deviceScreenHeight = configuration.screenHeightDp.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
        ) {
            AddOrEditBillDialog(
                billState = billsViewModel.billDialogState,
                isAddOrEditSuccess = billsViewModel.isAddOrEditSuccess,
                isBillDeleted = billsViewModel.isBillDeleted,
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoriesDropdown(
    inputedOption: String?,
    modifier: Modifier = Modifier,
    onValueSelected: (selectedCategory: String) -> Unit
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

    LaunchedEffect(inputedOption) {
        inputedOption?.let {
            category = inputedOption
        }
    }

    LaunchedEffect(category) {
        if (category.isNotBlank()) {
            onValueSelected(category)
        }
    }

    Box(
        modifier = modifier
    ) {
        ExposedDropdownMenuBox(
            expanded = showCategoriesDropdown,
            onExpandedChange = { showCategoriesDropdown = !showCategoriesDropdown }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .width(280.dp),
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
                        if (showCategoriesDropdown) Icons.Filled.KeyboardArrowUp
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
            modifier = Modifier
                .size(350.dp, 100.dp)
                .background(BackgroundColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CategoriesDropdown(inputedOption = null) {}
        }
    }
}

@Composable
private fun Tag(
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
            modifier = Modifier
                .padding(end = 8.dp)
                .weight(1f)
        )

        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = "Ícone de x",
            tint = White06Color,
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onDeleteTag() }
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
fun TagsContainer(
    tags: List<String>,
    onDeleteTag: (tagToDelete: String) -> Unit
) {
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
            Tag(tag) {
                onDeleteTag(tag)
            }
        }
    }
}

@Preview
@Composable
private fun TagsContainerPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        val tags = listOf("Entrada", "Trabalho", "Bonificação", "2025")

        Column(
            modifier = Modifier
                .size(350.dp, 100.dp)
                .background(BackgroundColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TagsContainer(tags = tags) {}
        }
    }
}