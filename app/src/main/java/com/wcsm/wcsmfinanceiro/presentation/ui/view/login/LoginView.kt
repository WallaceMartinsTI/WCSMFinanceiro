package com.wcsm.wcsmfinanceiro.presentation.ui.view.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wcsm.wcsmfinanceiro.R
import com.wcsm.wcsmfinanceiro.presentation.ui.component.CustomCheckbox
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnBackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color

@Composable
fun LoginView(
    onCreateAccount: () -> Unit,
    onLogin: () -> Unit
) {
    val loginViewModel: LoginViewModel = hiltViewModel()

    val loginState by loginViewModel.loginStateFlow.collectAsStateWithLifecycle()
    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()

    val focusRequester = remember { FocusRequester() }

    var showPassword by remember { mutableStateOf(false) }

    var keepLogin by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        if(uiState.success) {
            onLogin()
            loginViewModel.resetUiState()

        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.wcsm_financeiro_logo),
            contentDescription = "WCSM Financeiro logo",
            modifier = Modifier.size(width = 250.dp, height = 150.dp)
        )
        
        Text(
            text = "Acesse sua conta para desbloquear todas as funcionalidades do WCSM Financeiro e gerenciar suas finanças com facilidade.",
            color = OnBackgroundColor,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Justify,
            modifier = Modifier.width(280.dp).padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = loginState.email,
            onValueChange = {
                loginViewModel.updateLoginState(
                    loginState.copy(
                        email = it
                    )
                )
            },
            modifier = Modifier.width(280.dp),
            label = {
                Text(
                    text = "E-mail",
                    style = MaterialTheme.typography.labelMedium
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
                if(loginState.email.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Ícone de x",
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .clickable {
                                loginViewModel.updateLoginState(
                                    loginState.copy(
                                        email = ""
                                    )
                                )
                                focusRequester.requestFocus()
                            },
                        tint = White06Color
                    )
                }
            },
            singleLine = true,
            isError = loginState.emailErrorMessage.isNotBlank(),
            supportingText = {
                if(loginState.emailErrorMessage.isNotBlank()) {
                    Text(
                        text = loginState.emailErrorMessage
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )

        OutlinedTextField(
            value = loginState.password,
            onValueChange = {
                loginViewModel.updateLoginState(
                    loginState.copy(
                        password = it
                    )
                )
            },
            label = {
                Text(
                    text = "Senha",
                    style = MaterialTheme.typography.labelMedium
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
            isError = loginState.passwordErrorMessage.isNotBlank(),
            supportingText = {
                if(loginState.passwordErrorMessage.isNotBlank()) {
                    Text(
                        text = loginState.passwordErrorMessage
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            visualTransformation = if(showPassword) VisualTransformation.None
            else PasswordVisualTransformation()
        )

        CustomCheckbox(
            checkboxText = "Continuar Logado?",
            alreadyChecked = keepLogin
        ) { isChecked ->
            keepLogin = isChecked
        }

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
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable { onCreateAccount() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                loginViewModel.loginUser(loginState)
            },
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
        LoginView(
            onCreateAccount = {},
            onLogin = {}
        )
    }
}