package com.wcsm.wcsmfinanceiro.presentation.ui.view.bills

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.wcsmfinanceiro.presentation.ui.component.XIcon
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.GrayColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnSurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme

@Composable
fun BillTag(
    tag: String,
    modifier: Modifier = Modifier,
    onDeleteTag: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(25.dp))
            .width(170.dp)
            .background(GrayColor)
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = tag,
            color = OnSurfaceColor,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            overflow = TextOverflow.Ellipsis,
            softWrap = false,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )

        XIcon {
            onDeleteTag()
        }
    }
}

@Preview
@Composable
private fun BillTagPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        Column(
            modifier = Modifier
                .size(350.dp, 300.dp)
                .background(BackgroundColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BillTag("Bonificação") {}
            Spacer(Modifier.height(8.dp))
            BillTag("Bonificaçãopopo") {}
            Spacer(Modifier.height(8.dp))
            BillTag("Bonificaçãotestemuitograndemesmo") {}

        }
    }
}