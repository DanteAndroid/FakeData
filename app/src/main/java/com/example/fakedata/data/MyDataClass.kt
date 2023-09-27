package com.example.fakedata.data

import com.example.fakedata.BooleanOption
import com.example.fakedata.FakeData
import com.example.fakedata.StringOption
import java.time.LocalDate

/**
 * @author Du Wenyu
 * 2023/9/1
 */
@FakeData(
    stringOption = StringOption(prefix = "pre-", mode = StringOption.StringMode.Static),
    booleanOption = BooleanOption(staticValue = true)
)
data class MyDataClass(
    val name: String,
    val age: Int,
    val test: Boolean,
    val height: Long,
    val job: String,
    val time: LocalDate
){
    companion object{
        fun test(){
            // After successful build
            // You can call MyDataClassFake.xxx to get fake data now!
        }
    }
}
