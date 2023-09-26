package com.example.fakedata

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.Nullability
import kotlin.random.Random

/**
 * @author Du Wenyu
 * 2023/9/14
 */
class FakeValueGenerator(
    private val stringOption: StringOption,
    private val booleanOption: BooleanOption
) {
    val packages = mutableListOf<String>()

    private fun getFakeValue(propertyType: String?, index: Int): String? {
        return when (propertyType) {
            "kotlin.Int", "kotlin.Long", "kotlin.Double", "kotlin.Float" -> "0"

            "kotlin.String" -> "\"" +
                    stringOption.prefix +
                    stringOption.mode.generate(index) +
                    stringOption.suffix +
                    "\""

            "java.time.LocalDate" -> {
                packages.add(propertyType)
                "LocalDate.now()"
            }

            "androidx.lifecycle.ViewModel" -> {
                packages.add("androidx.lifecycle.viewmodel.compose.viewModel")
                "viewModel()"
            }

            "kotlin.Boolean" -> {
                if (booleanOption.randomValue) Random.nextBoolean()
                    .toString() else booleanOption.staticValue.toString()
            }

            "java.util.Date" -> {
                packages.add(propertyType)
                "Date()"
            }

            else -> null
        }
    }

    private var index = 0
    fun createConstructor(symbol: KSClassDeclaration): String {
        val constructor = symbol.constructorParameters()?.joinToString(", ") { parameter ->
            val propertyName = parameter.name?.asString()
            parameter.type.resolve().let {
                val fakeValue = getFakeValue(it.declaration.qualifiedName?.asString(), index)
                if (fakeValue == null) {
                    // 因为是构造假数据，所以传null就行
                    if (it.nullability == Nullability.NULLABLE) "$propertyName = null" else "FakeParameterFailed"
                } else {
                    "$propertyName = $fakeValue"
                }
            }
        }
        index++
        return constructor?.replace(", FakeParameterFailed", "").orEmpty()
    }

    fun createConstructor(declaration: KSFunctionDeclaration, index: Int = 0): String {
        var hasViewModel = false
        val parameters = declaration.parameters.joinToString { parameter ->
            val name = parameter.name?.asString() ?: ""
            val type = parameter.type.resolve()
            if (type.declaration.qualifiedName?.asString() == "androidx.lifecycle.ViewModel") hasViewModel =
                true

            val fakeValue = getFakeValue(type.declaration.qualifiedName?.asString(), index)
            if (fakeValue == null) {
                // 因为是构造假方法，所以这个属性保持不变（用户自己传参数）
                "$name:$type"
            } else "$name:$type = $fakeValue"
        }
        val returnType =
            declaration.returnType?.resolve()?.declaration?.simpleName?.asString() ?: ""
        return declaration.getImportCode() +
                (if (hasViewModel) "import androidx.lifecycle.viewmodel.compose.viewModel\n\n" else "") +
                declaration.getFunctionCode()?.let {
                    it.replace(
                        it.lines()[1],
                        "fun ${declaration.simpleName.asString()}Fake($parameters): $returnType{"
                    )
                }.orEmpty()
    }

}