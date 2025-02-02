package com.wcsm.wcsmfinanceiro.presentation.util

import org.junit.Test
import org.junit.Assert.*

class HelpersTest {

    @Test
    fun fixMonetaryValueByExistentOwnerTest() {
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
            assertEquals(values.second, fixedValue)
        }
    }
}