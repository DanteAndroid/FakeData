package com.example.fakedata

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import java.io.OutputStream

/**
 * @author Du Wenyu
 * 2023/9/14
 */

class FakeDataVisitor(private val codeGenerator: CodeGenerator) : KSVisitorVoid() {

    private lateinit var log: OutputStream

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
        log =
            codeGenerator.createNewFile(Dependencies(false), "", function.toString(), "log")
        val params = parseParameters(function.annotations)
        log.appendText("${params.first}\n${params.second}\n")
        log.appendText(function.simpleName.asString() + ":\n" + function.parameters.joinToString {
            it.name?.asString().orEmpty()
        })
        val fakeValueGenerator =
            FakeValueGenerator(params.first, params.second)
        val packageName = function.packageName.asString()
        val className = function.simpleName.asString()
        val generatedClassName = "${className}Fake"
        val initializer = fakeValueGenerator.createConstructor(function)

        log.appendText("\n$initializer\n")

        val file = codeGenerator.createNewFile(
            dependencies = Dependencies(true),
            packageName = packageName,
            fileName = generatedClassName
        )
        file.appendText(initializer.formatCode())
    }

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        log =
            codeGenerator.createNewFile(Dependencies(false), "", classDeclaration.toString(), "log")
        val params = parseParameters(classDeclaration.annotations)
        log.appendText("${params.first}\n\n${params.second}")
        val generator =
            FakeValueGenerator(params.first, params.second)
        val packageName = classDeclaration.packageName.asString()
        val className = classDeclaration.simpleName.asString()
        val generatedClassName = "${className}Fake"
        val initializer = generator.createConstructor(classDeclaration)
        val sb = StringBuilder()
        sb.appendLine("package $packageName")
        sb.appendLine("import ${classDeclaration.fullName()}")
        sb.appendLine(generator.packages.joinToString("\n") { "import $it" })
        sb.appendLine()
        sb.appendLine(
            "object $generatedClassName {\n" +
                    "val item: ${classDeclaration.simpleName()} = ${classDeclaration.simpleName()}($initializer)\n\n" +
                    "fun items(size:Int):List<${classDeclaration.simpleName()}>{" +
                    "return (1..size).map { ${classDeclaration.simpleName()}($initializer) }" +
                    "}\n" +
                    "}"
        )

        val file = codeGenerator.createNewFile(
            dependencies = Dependencies(true),
            packageName = packageName,
            fileName = generatedClassName
        )
        file.appendText(sb.toString())
    }

    private fun parseParameters(classDeclaration: Sequence<KSAnnotation>): Pair<StringOption, BooleanOption> {
        var stringOption = StringOption()
        var booleanOption = BooleanOption()
        classDeclaration.forEach { ksAnnotation ->
            ksAnnotation.arguments.joinToString(",\n\n") { argument ->
                val key = argument.name?.asString()
                val value = parse(
                    key,
                    argument.value
                )
                if (value is StringOption) stringOption = value
                if (value is BooleanOption) booleanOption = value
                "${key}:\n[${value}]"
            }
        }
        return stringOption to booleanOption
    }

    private fun parse(key: String?, value: Any?): Any? {
        return if (value is KSAnnotation) {
            when (key) {
                "stringOption" -> {
                    val prefix = value.read<String>("prefix").orEmpty()
                    val suffix = value.read<String>("suffix").orEmpty()
                    val modeName =
                        value.arguments.find { it.name?.asString() == "mode" }?.value as KSType
                    val mode = StringOption.StringMode.valueOf(modeName.simpleName())
                    return StringOption(prefix = prefix, suffix = suffix, mode = mode)
                }

                "booleanOption" -> {
                    val staticValue = value.read<Boolean>("staticValue") ?: false
                    val randomValue = value.read<Boolean>("randomValue") ?: false
                    return BooleanOption(staticValue = staticValue, randomValue = randomValue)
                }

                else -> null
            }
        } else null
    }
}