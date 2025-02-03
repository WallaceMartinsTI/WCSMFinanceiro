package com.wcsm.wcsmfinanceiro.presentation.ui.view

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wcsm.wcsmfinanceiro.R
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnBackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme

@Composable
fun WelcomeView(
    onContinue: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(
        label = "Blinking 'Vamos começar' text"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Blinking 'Vamos começar' text"
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
            text = "Organize suas finanças de forma simples, prática e eficiente. Estamos aqui para ajudar você a acompanhar suas contas, gerenciar sua carteira e alcançar seus objetivos financeiros.",
            modifier = Modifier.width(280.dp),
            color = OnBackgroundColor,
            textAlign = TextAlign.Justify,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Vamos começar?",
            color = OnBackgroundColor.copy(alpha = alpha),
            fontFamily = PoppinsFontFamily,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )

        Button(
            onClick = { onContinue() },
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
                    text = "CONTINUAR",
                    style = MaterialTheme.typography.bodyMedium
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Ícone de seta para direita",
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun WelcomeViewPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        WelcomeView {  }
    }
}