package com.example.fakedata

/**
 * @author Du Wenyu
 * 2023/9/1
 */
@Target(AnnotationTarget.CLASS)
annotation class FakeData(val withIndex: Boolean = false, val randomSuffix: Int = 0) {
    companion object {
        const val flexible = "withIndex"
        const val randomSuffix = "randomSuffix"
    }
}
