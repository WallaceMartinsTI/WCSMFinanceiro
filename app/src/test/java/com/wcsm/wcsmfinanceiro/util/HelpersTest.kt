package com.wcsm.wcsmfinanceiro.util

import com.google.common.truth.Truth.assertThat
import com.wcsm.wcsmfinanceiro.data.local.model.BillType
import com.wcsm.wcsmfinanceiro.data.local.model.PaymentType
import org.junit.Test

class HelpersTest {

    @Test
    fun getPaymentTypeFromString_getPaymentTypeFromStringWithValidPaymentType_shouldReturnCorrectPaymentType() {
        val moneyPaymentTypeString = "Dinheiro"
        val moneyPaymentType = getPaymentTypeFromString(moneyPaymentTypeString)

        val cardPaymentTypeString = "Cartão"
        val cardPaymentType = getPaymentTypeFromString(cardPaymentTypeString)

        assertThat(moneyPaymentType).isEqualTo(PaymentType.MONEY)
        assertThat(cardPaymentType).isEqualTo(PaymentType.CARD)
    }

    @Test
    fun getPaymentTypeFromString_getPaymentTypeFromStringWithInvalidPaymentType_shouldReturnDefaultPaymentType() {
        val moneyPaymentTypeString = "DinheiroErrado"
        val moneyPaymentType = getPaymentTypeFromString(moneyPaymentTypeString)

        val cardPaymentTypeString = "CartãoErrado"
        val cardPaymentType = getPaymentTypeFromString(cardPaymentTypeString)

        assertThat(moneyPaymentType).isEqualTo(PaymentType.MONEY)
        assertThat(cardPaymentType).isEqualTo(PaymentType.MONEY)
    }

    @Test
    fun getBillTypeFromString_getBillTypeFromStringWithValidBillType_shouldReturnCorrectBillType() {
        val incomeBillTypeString = "Receita"
        val incomeBillType = getBillTypeFromString(incomeBillTypeString)

        val expenseBillTypeString = "Despesa"
        val expenseBillType = getBillTypeFromString(expenseBillTypeString)

        assertThat(incomeBillType).isEqualTo(BillType.INCOME)
        assertThat(expenseBillType).isEqualTo(BillType.EXPENSE)
    }

    @Test
    fun getBillTypeFromString_getBillTypeFromStringWithInvalidBillType_shouldReturnDefaultBillType() {
        val incomeBillTypeString = "ReceitaErrado"
        val incomeBillType = getBillTypeFromString(incomeBillTypeString)

        val expenseBillTypeString = "DespesaErrado"
        val expenseBillType = getBillTypeFromString(expenseBillTypeString)

        assertThat(incomeBillType).isEqualTo(BillType.INCOME)
        assertThat(expenseBillType).isEqualTo(BillType.INCOME)
    }

    @Test
    fun getFormattedTags_getFormattedTags_shouldFormatTagsCorrectly() {
        val tags = "tag1, tag2, tag3, tag4"

        val formattedTags = getFormattedTags(tags)

        formattedTags.forEachIndexed { index, tag ->
            assertThat(tag).isEqualTo("tag${index + 1}")
        }
    }

    @Test
    fun formatMonetaryValue_formatMonetaryValueWithDifferentValues_shouldFormatCorrectly() {
        val valuesToTest = listOf(
            Pair("0.01", "0.01"),
            Pair("0.10", "0.10"),
            Pair("1.00", "1.00"),
            Pair("1.00", "1.00"),
            Pair("1.01", "1.01"),
            Pair("1.10", "1.10"),
            Pair("10.01", "10.01"),
            Pair("10.10", "10.10"),
            Pair("100.01", "100.01"),
            Pair("100.10", "100.10"),
            Pair("101.01", "101.01"),
            Pair("101.10", "101.10"),
            Pair("1000.01", "1000.01"),
            Pair("1000.10", "1000.10"),
            Pair("1500.0", "1500.00"),
            Pair("0.0", "0.00"),
            Pair("0.5", "0.50"),
            Pair("999999.9", "999999.90"),
            Pair("999999.99", "999999.99"),
            Pair("1.1", "1.10"),
            Pair("1234.5", "1234.50"),
            Pair("9876.7", "9876.70"),
            Pair("1000.0", "1000.00"),
            Pair("5000000.99", "5000000.99"),
            Pair("9.9", "9.90"),
            Pair("5.05", "5.05"),
            Pair("3.0", "3.00"),
            Pair("9999999.99", "9999999.99")
        )

        valuesToTest.forEach { values ->
            val fixedValue = formatMonetaryValue(values.first)
            assertThat(values.second).isEqualTo(fixedValue)
        }
    }

    @Test
    fun getDoubleForStringPrice_getDoubleForStringPrice_shouldReturnCorrectlyDoubleValue() {
        val stringPrice1 = "3"
        val stringPrice2 = "30"
        val stringPrice3 = "300"
        val stringPrice4 = "3000"
        val stringPrice5 = "30000"
        val stringPrice6 = "300000"

        assertThat(getDoubleForStringPrice(stringPrice1)).isEqualTo(0.03)
        assertThat(getDoubleForStringPrice(stringPrice2)).isEqualTo(0.30)
        assertThat(getDoubleForStringPrice(stringPrice3)).isEqualTo(3.00)
        assertThat(getDoubleForStringPrice(stringPrice4)).isEqualTo(30.00)
        assertThat(getDoubleForStringPrice(stringPrice5)).isEqualTo(300.00)
        assertThat(getDoubleForStringPrice(stringPrice6)).isEqualTo(3000.00)
    }

    @Test
    fun getDoubleForStringPrice_getDoubleForStringPrice_shouldReturnDefaultDoubleValue() {
        val wrongStringPrice = "30,30,30"

        assertThat(getDoubleForStringPrice(wrongStringPrice)).isEqualTo(0.0)
    }
}