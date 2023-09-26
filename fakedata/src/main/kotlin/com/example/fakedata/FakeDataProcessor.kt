package com.example.fakedata

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.validate

/**
 * @author Du Wenyu
 * 2023/9/1
 */
class FakeDataProcessor(private val codeGenerator: CodeGenerator, private val logger: KSPLogger) :
    SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.warn("FakeDataProcessor start......")
        val symbols = resolver.getSymbolsWithAnnotation(FakeData::class.java.canonicalName)
        val ret = symbols.filter { !it.validate() }.toList()
        symbols.filter { (it is KSClassDeclaration || it is KSFunctionDeclaration) && it.validate() }
            .forEach { it.accept(FakeDataVisitor(codeGenerator), Unit) }
        return ret
    }

}

class FakeDataProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return FakeDataProcessor(environment.codeGenerator, environment.logger)
    }

}
