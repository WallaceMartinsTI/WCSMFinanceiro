package com.wcsm.wcsmfinanceiro.presentation.ui.view.settings.components

import android.inputmethodservice.Keyboard
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.wcsm.wcsmfinanceiro.R
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.ErrorColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.GrayColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnBackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme

@Composable
fun AppInfoView(
    onEmailClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val specialWordStyle = SpanStyle(
        color = PrimaryColor,
        fontWeight = FontWeight.SemiBold
    )

    val appPresentationText = buildAnnotatedString {
        append("Gerencie suas finanças de forma simples e eficiente! Com este app, você pode acompanhar receitas, despesas e gráficos na cadastrar transações em ")
        withStyle(specialWordStyle) {
            append("Home")
        }
        append(", cadastrar transações em ")
        withStyle(specialWordStyle) {
            append("Contas")
        }
        append(", organizar seus bancos na ")
        withStyle(specialWordStyle) {
            append("Carteira")
        }
        append(", acessar ferramentas úteis no ")
        withStyle(specialWordStyle) {
            append("Plus")
        }
        append(" e personalizar seu perfil em ")
        withStyle(specialWordStyle) {
            append("Configurações")
        }
        append(". Tudo isso com segurança e praticidade. \uD83D\uDE80\uD83D\uDCB0")
    }

    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .fillMaxWidth()
                .background(SurfaceColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "INFORMAÇÕES DO APP",
                color = SecondaryColor,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(8.dp))

            Box {
                Text(
                    text = appPresentationText,
                    color = OnBackgroundColor,
                    textAlign = TextAlign.Justify
                )
            }

            Text(
                text = "Desenvolvedor",
                style = MaterialTheme.typography.labelMedium,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SecondaryColor, RoundedCornerShape(15.dp))
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.dev),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clip(CircleShape).size(40.dp)
                )

                Column(
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Wallace Martins",
                        color = SecondaryColor,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(15.dp))
                            .fillMaxWidth()
                            .background(GrayColor)
                            .clickable { onEmailClick() }
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Ícone de e-mail",
                            tint = OnBackgroundColor,
                            modifier = Modifier.size(20.dp)
                        )

                        Text(
                            text = "wallace159santos@hotmail.com",
                            color = OnBackgroundColor,
                            fontSize = 10.sp
                        )

                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Ícone de enviar",
                            tint = OnBackgroundColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .fillMaxWidth()
                    .background(ErrorColor)
                    .clickable { onDismiss() }
                    .padding(vertical = 4.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "FECHAR",
                    color = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
private fun AppInfoViewPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        Column(
            modifier = Modifier.fillMaxSize().background(BackgroundColor)
        ) {
            AppInfoView(
                onEmailClick = {},
                onDismiss = {}
            )
        }
    }
}