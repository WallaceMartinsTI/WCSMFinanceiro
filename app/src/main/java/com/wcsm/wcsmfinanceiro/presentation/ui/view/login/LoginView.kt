package com.wcsm.wcsmfinanceiro.presentation.ui.view.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wcsm.wcsmfinanceiro.R
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnBackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.TertiaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color
import com.wcsm.wcsmfinanceiro.presentation.ui.view.WelcomeView

@Composable
fun LoginView(onLogin: () -> Unit) {
    val focusRequester = remember { FocusRequester() }

    var email by remember { mutableStateOf("") }
    var emailErrorMessage by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordErrorMessage by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    val labelTextStyle = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Bold
    )

    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.wcsm_financeiro_logo),
            contentDescription = "WCSM Financeiro logo",
            modifier = Modifier.size(250.dp)
        )
        
        Text(
            text = "Entre com sua conta para ter acesso as funcionalidades do app",
            color = OnBackgroundColor,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(280.dp).padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                if(email.length < 255) email = it
            },
            modifier = Modifier.width(280.dp),
            label = {
                Text(
                    text = "E-mail",
                    color = TertiaryColor,
                    style = labelTextStyle
                )
            },
            placeholder = {
                Text(
                    text = "Digite seu e-mail"
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Ícone de e-mail",
                    tint = White06Color
                )
            },
            trailingIcon = {
                if(email.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Ícone de x",
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .clickable {
                                email = ""
                                focusRequester.requestFocus()
                            },
                        tint = White06Color
                    )
                }
            },
            singleLine = true,
            isError = emailErrorMessage.isNotEmpty(),
            supportingText = {
                if(emailErrorMessage.isNotEmpty()) {
                    Text(
                        text = emailErrorMessage
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(
                    text = "Senha",
                    color = TertiaryColor,
                    style = labelTextStyle
                )
            },
            placeholder = {
                Text(
                    text = "Digite sua senha"
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Password,
                    contentDescription = "Ícone de e-mail",
                    tint = White06Color
                )
            },
            trailingIcon = {
                if(showPassword) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "Ícone de x",
                        modifier = Modifier.clickable { showPassword = !showPassword },
                        tint = White06Color
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.VisibilityOff,
                        contentDescription = "Ícone de x",
                        modifier = Modifier.clickable { showPassword = !showPassword },
                        tint = White06Color
                    )
                }
            },
            singleLine = true,
            isError = passwordErrorMessage.isNotEmpty(),
            supportingText = {
                if(passwordErrorMessage.isNotEmpty()) {
                    Text(
                        text = passwordErrorMessage
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            visualTransformation = if(showPassword) VisualTransformation.None
            else PasswordVisualTransformation()
        )

        Row(
            modifier = Modifier
                .width(280.dp)
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Não tem uma conta?",
                color = OnBackgroundColor,
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Criar conta",
                color = PrimaryColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { },
            modifier = Modifier
                .background(BackgroundColor)
                .padding(top = 8.dp, bottom = 16.dp)
                .width(280.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ENTRAR",
                    style = MaterialTheme.typography.bodyMedium
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Login,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }

    }
}

@Preview
@Composable
private fun LoginViewPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        LoginView {  }
    }
}