package com.example.fakedata.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fakedata.FakeData
import java.time.LocalDate

/**
 * @author Du Wenyu
 * 2023/9/25
 */
@FakeData
@Composable
fun MyComposableFun(title: String, number: Int, date: LocalDate) {
    Box(modifier = Modifier.size(200.dp, 100.dp), contentAlignment = Alignment.Center) {
        Text(text = title + number + date)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, widthDp = 360)
@Preview(showBackground = true)
@Composable
fun MyComposableFunPreview() {
    MyComposableFunFake()
}
