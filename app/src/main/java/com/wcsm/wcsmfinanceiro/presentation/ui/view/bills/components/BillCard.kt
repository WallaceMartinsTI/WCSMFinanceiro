package com.wcsm.wcsmfinanceiro.presentation.ui.view.bills.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Expand
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.wcsmfinanceiro.data.local.entity.Bill
import com.wcsm.wcsmfinanceiro.data.local.model.BillType
import com.wcsm.wcsmfinanceiro.data.local.model.PaymentType
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.MoneyGreenColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.RedColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.TertiaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color
import com.wcsm.wcsmfinanceiro.util.toBrazilianDateString
import com.wcsm.wcsmfinanceiro.util.toBrazilianReal

@Composable
fun BillCard(
    bill: Bill,
    modifier: Modifier = Modifier,
    onExpandBillCard: () -> Unit
) {
    val titleAndPriceColor = if (bill.billType == BillType.INCOME) MoneyGreenColor else RedColor

    ElevatedCard(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .clickable { onExpandBillCard() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = bill.title,
                        color = titleAndPriceColor,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = PoppinsFontFamily,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f).padding(end = 40.dp)
                    )

                    Text(
                        text = bill.date.toBrazilianDateString(extendedYear = false),
                        fontFamily = PoppinsFontFamily
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = bill.value.toBrazilianReal(),
                        color = titleAndPriceColor,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PoppinsFontFamily
                    )

                    Text(
                        text = bill.category,
                        textAlign = TextAlign.End,
                        color = TertiaryColor,
                        fontFamily = PoppinsFontFamily,
                        modifier = Modifier.width(120.dp)
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.OpenInFull,
                contentDescription = "Ícone de abrir em tela cheia",
                tint = White06Color
            )
        }
    }
}

@Preview
@Composable
private fun BillCardPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        val incomeBill = Bill(
            billId = 1,
            walletId = 1,
            walletCardId = 1,
            billType = BillType.INCOME,
            origin = "Trabalho",
            title = "Salário",
            value = 2624.72,
            description = "Salário do mês de Janeiro",
            date = 1736208000000, // Mon Jan 06 2025 21:00:00.000
            category = "Trabalho",
            dueDate = 1739588400000, // Sat Feb 15 2025 00:00:00.000
            expired = false,
            paid = true,
            paymentType = PaymentType.MONEY,
            tags = listOf("Salario", "Trabalho")
        )

        val expenseBill = Bill(
            billId = 1,
            walletId = 1,
            walletCardId = 1,
            billType = BillType.EXPENSE,
            origin = "Mercado",
            title = "Compra do Mês",
            value = 975.35,
            description = "Compra do mês de Janeiro",
            date = 1736208000000, // Mon Jan 06 2025 21:00:00.000
            category = "Mercado",
            dueDate = 1739588400000, // Sat Feb 15 2025 00:00:00.000
            expired = false,
            paid = true,
            paymentType = PaymentType.CARD,
            tags = listOf("Mercado")
        )

        Column(
            modifier = Modifier.fillMaxSize().background(BackgroundColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BillCard(bill = incomeBill.copy(category = "Freelancer/Autônomo")) {}

            Spacer(Modifier.height(16.dp))

            BillCard(bill = expenseBill, modifier = Modifier.padding(horizontal = 16.dp)) {}

            Spacer(Modifier.height(16.dp))

            BillCard(
                bill = incomeBill.copy(value = 9999999.99,category = "Freelancer/Autônomo"),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {}

            Spacer(Modifier.height(16.dp))

            BillCard(bill = expenseBill) {}
        }
    }
}