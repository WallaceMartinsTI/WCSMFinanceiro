package com.wcsm.wcsmfinanceiro.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnSecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CurrentDateTimeContainer(modifier: Modifier = Modifier) {
    val currentDate = remember { Date() }

    val dayOfTheWeekFormat = SimpleDateFormat("EEEE", Locale("pt", "BR"))
    var dayOfTheWeek = dayOfTheWeekFormat.format(currentDate).replaceFirstChar {
        if(it.isLowerCase()) it.titlecase() else it.toString()
    }

    if(dayOfTheWeek.endsWith("-feira")) {
        dayOfTheWeek = dayOfTheWeek.replace("-feira", "-Feira")
    }

    val dateFormat = SimpleDateFormat(
        "dd 'de' MMMM 'de' yyyy", Locale("pt", "BR")
    )
    val rawDate = dateFormat.format(currentDate)
    val formattedDate = capitalizeMonth(rawDate)

    var time by remember { mutableStateOf(getCurrentTime()) }

    LaunchedEffect(Unit) {
        while(true) {
            withContext(Dispatchers.Default) {
                val newTime = getCurrentTime()
                withContext(Dispatchers.Main) {
                    if(time != newTime) {
                        time = newTime
                    }
                }
            }
            delay(1000L)
        }
    }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .background(SecondaryColor)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DateTimeContainerText(text = dayOfTheWeek)
        DateTimeContainerText(text = formattedDate)
        DateTimeContainerText(text = time)
    }
}

@Composable
fun DateTimeContainerText(text: String) {
    Text(
        text = text,
        color = OnSecondaryColor,
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Bold
    )
}

@Preview
@Composable
private fun DateTimeContainerPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        Column(
            modifier = Modifier.size(300.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CurrentDateTimeContainer()
        }
    }
}

private fun getCurrentTime(): String {
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return timeFormat.format(Date())
}

private fun capitalizeMonth(date: String): String {
    val monthRegex = Regex("\\d{2} de ([a-záéíóúãõ]+) de \\d{4}")
    return date.replace(monthRegex) { match ->
        val month = match.groupValues[1]
        val capitalizedMonth = month.replaceFirstChar { it.titlecase(Locale.getDefault()) }
        match.groupValues[0].replace(month, capitalizedMonth)
    }
}