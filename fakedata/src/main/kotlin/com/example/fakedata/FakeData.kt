package com.example.fakedata

/**
 * @author Du Wenyu
 * 2023/9/1
 */
annotation class FakeData(
    val stringOption: StringOption = StringOption(),
    val booleanOption: BooleanOption = BooleanOption()
)

annotation class StringOption(
    val prefix: String = "",
    val suffix: String = "",
    val mode: StringMode = StringMode.WithIndex
) {
    enum class StringMode {
        WithIndex, RandomValue, Static;

        fun generate(index: Int): String = StringDictionary.generate(this, index = index)
    }
}

annotation class BooleanOption(
    val staticValue: Boolean = false,
    val randomValue: Boolean = false
)