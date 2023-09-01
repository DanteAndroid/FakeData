package com.example.fakedata.data

import com.example.fakedata.FakeData
import java.time.LocalDate

/**
 * @author Du Wenyu
 * 2023/9/1
 */
@FakeData
data class MyDataClass(
    val name: String,
    val age: Int,
    val height: Long,
    val job: String,
    val time: LocalDate
){
    companion object{
        fun test(){
//            MyDataClass

        }
    }
}
