package com.wcsm.wcsmfinanceiro.presentation.ui.view.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.wcsm.wcsmfinanceiro.data.entity.Account
import com.wcsm.wcsmfinanceiro.data.entity.AccountCard
import com.wcsm.wcsmfinanceiro.presentation.ui.component.AppLoader
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.ErrorColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color

@Composable
fun AddOrEditAccountDialog(
    account: Account? = null,
    deviceScreenHeight: Dp,
    onDismiss: () -> Unit
) {
    var isModalLoading by remember { mutableStateOf(account != null) }

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
            Text(
                //text = if (bill != null) "EDITAR CONTA" else "ADICIONAR CONTA",
                text = "ADICIONAR CARTEIRA",
                modifier = Modifier.padding(bottom = 8.dp),
                color = PrimaryColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (isModalLoading) {
                    AppLoader(modifier = Modifier.size(80.dp))
                } else {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        modifier = Modifier
                            .width(280.dp),
                            //.focusRequester(focusRequester[0]),
                        label = {
                            Text(
                                text = "Título*",
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        placeholder = {
                            Text(
                                text = "Digite o título da carteira"
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Wallet,
                                contentDescription = "Ícone de carteira",
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
                            .width(280.dp),
                        //.focusRequester(focusRequester[0]),
                        label = {
                            Text(
                                text = "Valor*",
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        placeholder = {
                            Text(
                                text = "Digite o valor que você tem nesta carteira"
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

                    Column(
                       modifier = Modifier
                           .width(280.dp)
                           .border(1.dp, White06Color, RoundedCornerShape(10.dp))
                           .padding(8.dp),
                       horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Adicionar Cartão",
                            color = PrimaryColor,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )

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
                            readOnly = true,
                            singleLine = true,
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ErrorColor
                                )
                            ) {
                                Text(
                                    text = "CANCELAR"
                                )
                            }

                            Button(onClick = {}) {
                                Text(
                                    text = "ADICIONAR"
                                )
                            }
                        }
                    }

                    Column(
                       modifier =  Modifier
                           .width(280.dp)
                           .padding(8.dp),
                       horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Cartões",
                            color = PrimaryColor,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        val accountCard = AccountCard(
                            id = 1,
                            title = "Cartão de Crédito",
                            total = 5000.00,
                            spent = 1500.00,
                            remaining = 3500.00
                        )


                        LazyRow(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, White06Color, RoundedCornerShape(10.dp))
                                .padding(8.dp)
                        ) {
                            items(4) {
                                CardContainer(
                                    modifier = Modifier.scale(0.9f),
                                    card = accountCard
                                )
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
private fun AddOrEditBillDialogPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {

        val configuration = LocalConfiguration.current
        val deviceScreenHeight = configuration.screenHeightDp.dp

        Column(
            modifier = Modifier.fillMaxSize().background(BackgroundColor)
        ) {
            AddOrEditAccountDialog(null, deviceScreenHeight) { }
        }
    }
}
