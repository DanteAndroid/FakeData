package com.example.fakedata

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import java.io.File
import java.io.OutputStream

/**
 * @author Du Wenyu
 * 2023/9/14
 */
fun OutputStream.appendText(string: String) = write(string.toByteArray())

fun KSClassDeclaration.constructorParameters() = this.primaryConstructor?.parameters

fun KSClassDeclaration.fullName() = this.qualifiedName?.asString()

fun KSClassDeclaration.simpleName() = this.simpleName.asString()
fun KSType.simpleName() = this.declaration.simpleName.asString()

inline fun <reified T> KSAnnotation.read(key: String?): T? {
    val value = arguments.find { it.name?.asString() == key }?.value
    if (value is T) {
        return value
    }
    return null
}

fun KSFunctionDeclaration.getImportCode() =
    containingFile?.filePath?.let {
        File(it).let {
            val importSection = StringBuilder()
            // 获取包名
            val packageName = it.readLines().first()
            importSection.appendLine(packageName)
            // 扫描导入
            it.readLines().drop(1).forEach { line ->
                if (line.startsWith("import ")) {
                    importSection.appendLine(line)
                }
            }
            importSection.toString()
        }
    }

fun KSFunctionDeclaration.getFunctionCode(): String? {
    containingFile?.filePath?.let {
        File(it).let { file ->
            val lines = file.readLines()
            var startIndex = -1
            var endIndex = -1

            for ((index, line) in lines.withIndex()) {
                if (line.startsWith("fun ${simpleName.asString()}")) {
                    startIndex = index - 1
                }
                if (line.trim() == "}") {
                    endIndex = index + 1
                    break
                }
            }

            if (startIndex != -1 && endIndex != -1) {
                return lines.slice(startIndex until endIndex + 1)
                    .joinToString("\n")
            }
        }
    }
    return null
}

fun File.importSection(): String {
    val importSection = StringBuilder()
    // 获取包名
    val packageName = readLines().first()
    importSection.appendLine(packageName)
    // 扫描导入
    readLines().drop(1).forEach { line ->
        if (line.startsWith("import ")) {
            importSection.appendLine(line)
        }
    }
    return importSection.toString()
}

fun String.formatCode(): String {
    val lines = this.lines()
    val formatted = StringBuilder()
    var indent = 0
    lines.forEach { line ->
        formatted.append("\t".repeat(indent))
        if (line.contains("{")) indent++
        if (line.contains("}")) indent--
        formatted.appendLine(line)
    }
    return formatted.toString()
}
