package com.example.fakedata

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration

/**
 * @author Du Wenyu
 * 2023/9/1
 */
class FakeDataProcessor(private val codeGenerator: CodeGenerator) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(FakeData::javaClass.name)
        println("Processssss ${FakeData::javaClass.name}")
        symbols.forEach { symbol ->
            if (symbol is KSClassDeclaration) {
                val packageName = symbol.packageName
                val className = symbol.simpleName
                val generatedClassName = "${className}Fake"
                val generatedPackageName = "$packageName.generated"
                val initializer = createInitializer(symbol)
                val code = """
                    package $generatedPackageName
                    
                    import ${symbol.qualifiedName}
                    
                    object $generatedClassName {
                        val data: ${symbol.qualifiedName} = ${symbol.qualifiedName}($initializer)
                    }
                """.trimIndent()

                val file = codeGenerator.createNewFile(
                    dependencies = Dependencies(false, symbol.containingFile!!),
                    packageName = generatedPackageName,
                    fileName = generatedClassName
                )
                file.bufferedWriter().use {
                    it.write(code)
                }
            }
        }
        return emptyList()
    }

    private fun createInitializer(symbol: KSClassDeclaration): String {
        val properties = symbol.primaryConstructor?.parameters?.joinToString(",") { parameter ->
            val propertyName = parameter.name?.asString()
            val propertyType = parameter.type.resolve().declaration.qualifiedName!!.asString()
            val fakeValue = getFakeValue(propertyType)
            "$propertyName = $fakeValue"
        }
        return properties ?: ""
    }

    private fun getFakeValue(propertyType: String): String {
        return when (propertyType) {
            "kotlin.String" -> "\"fakeString\""
            "kotlin.Int", "kotlin.Long", "kotlin.Double", "kotlin.Float" -> "0"
            else -> "null"
        }
    }

}

class FakeDataProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return FakeDataProcessor(environment.codeGenerator)
    }
}
