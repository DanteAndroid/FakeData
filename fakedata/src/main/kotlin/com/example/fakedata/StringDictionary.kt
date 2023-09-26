package com.example.fakedata

/**
 * 用来生成字符串
 * @author Du Wenyu
 * 2023/9/26
 */
object StringDictionary {
    private val subjects = listOf("The cat", "A dog", "My friend", "The book")
    private val verbs = listOf("jumps", "runs", "sleeps", "reads")
    private val objects = listOf("over the fence", "in the park", "on the table", "under the tree")

    fun generate(stringMode: StringOption.StringMode, index: Int): String {
        return when (stringMode) {
            StringOption.StringMode.RandomValue -> {
                val subject = subjects.random()
                val verb = verbs.random()
                val obj = objects.random()
                return "$subject $verb $obj."
            }

            StringOption.StringMode.WithIndex -> "This is a fake String $index"
            StringOption.StringMode.Static -> "This is a static String"
        }

    }

}