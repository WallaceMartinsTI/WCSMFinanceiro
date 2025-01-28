package com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.ErrorColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color

@Composable
fun AddOrEditWalletCardDialog(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .background(SurfaceColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),//.padding(bottom = 8.dp),
            ) {
                Text(
                    //text = if (bill != null) "EDITAR CONTA" else "ADICIONAR CONTA",
                    text = "ADICIONAR CARTÃO", // ou Editar
                    modifier = Modifier.align(Alignment.Center).padding(end = 16.dp),
                    color = PrimaryColor,
                    fontFamily = PoppinsFontFamily,
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

            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .width(272.dp),
                //.focusRequester(focusRequester[0]),
                label = {
                    Text(
                        text = "Título do Cartão*",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = "Ícone de carrinho de compra",
                        tint = White06Color
                    )
                },
                trailingIcon = {
                    /*if (billModalState.origin.isNotEmpty()) {
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
                    }*/
                },
                singleLine = true,
                supportingText = {
                    Text(
                        text = "Ex.: Cartão de Crédito",
                        color = White06Color
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
            )

            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .width(272.dp),
                //.focusRequester(focusRequester[0]),
                label = {
                    Text(
                        text = "Limite do Cartão*",
                        style = MaterialTheme.typography.labelMedium
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
                    /*if (billModalState.origin.isNotEmpty()) {
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
                    .width(272.dp),
                //.focusRequester(focusRequester[0]),
                label = {
                    Text(
                        text = "Gasto*",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                placeholder = {
                    Text(
                        text = "Digite o título da carteiraa"
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
                    /*if (billModalState.origin.isNotEmpty()) {
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
                    .width(272.dp),
                //.focusRequester(focusRequester[0]),
                label = {
                    Text(
                        text = "Livre",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                placeholder = {
                    Text(
                        text = "Digite o título da carteiraa"
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = "Ícone de dinheiro",
                        tint = White06Color
                    )
                },
                readOnly = true,
                singleLine = true,
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ErrorColor
                    )
                ) {
                    Text(
                        text = "CANCELAR"
                    )
                }

                Button(
                    onClick = {
                        // CRIAR CARTAO ...
                        onDismiss()
                    }
                ) {
                    Text(
                        text = "ADICIONAR"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AddOrEditWalletCardDialogPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        Column(
            modifier = Modifier.fillMaxSize().background(BackgroundColor)
        ) {
            AddOrEditWalletCardDialog {  }
        }
    }
}