package com.wcsm.wcsmfinanceiro.presentation.ui.view.bills

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.AssuredWorkload
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.EmojiTransportation
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.SetMeal
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.wcsmfinanceiro.presentation.model.Category
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillCategoriesDropdown(
    inputtedOption: String?,
    isError: Boolean,
    errorMessage: String,
    modifier: Modifier = Modifier,
    onValueSelected: (selectedCategory: String) -> Unit
) {
    var category by remember { mutableStateOf("Selecione uma categoria") }

    val categoriesDrowpdownOptions = listOf(
        Category(1, "Moradia", Icons.Default.Home),
        Category(2, "Alimentação", Icons.Default.SetMeal),
        Category(3, "Transporte", Icons.Default.EmojiTransportation),
        Category(4, "Saúde", Icons.Default.Healing),
        Category(5, "Educação", Icons.Default.Book),
        Category(6, "Lazer", Icons.Default.SportsEsports),
        Category(7, "Assinaturas", Icons.AutoMirrored.Filled.Assignment),
        Category(8, "Dívidas/Empréstimos", Icons.Default.MoneyOff),
        Category(9, "Impostos/Taxas", Icons.Default.AssuredWorkload),
        Category(10, "Salário", Icons.Default.AttachMoney),
        Category(11, "Freelancer/Autônomo", Icons.Default.AttachMoney),
        Category(12, "Investimentos", Icons.Default.Savings),
        Category(13, "Reembolso", Icons.Default.ShoppingBag),
        Category(14, "Outros", Icons.Default.Category)
    )
    var showCategoriesDropdown by remember { mutableStateOf(false) }

    LaunchedEffect(inputtedOption) {
        inputtedOption?.let {
            category = inputtedOption
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
                isError = isError,
                supportingText = {
                    if(errorMessage.isNotBlank()) {
                        Text(
                            text = errorMessage,
                            fontFamily = PoppinsFontFamily
                        )
                    }
                },
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
                categoriesDrowpdownOptions.forEach { selectedCategory ->
                    DropdownMenuItem(
                        text = {
                            Text(text = selectedCategory.title)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = selectedCategory.icon,
                                contentDescription = "Ícone de ${selectedCategory.icon}",
                                tint = White06Color
                            )
                        },
                        onClick = {
                            category = selectedCategory.title
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
private fun BillCategoriesDropdownPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BillCategoriesDropdown(
                inputtedOption = null,
                isError = false,
                errorMessage = ""
            ) {}
        }
    }
}