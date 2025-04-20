package com.simple.deeplink.processor

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.StandardLocation

@Suppress("unused")
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class DeeplinkProviderProcessor : AbstractProcessor() {

    private val annotationName = "com.simple.deeplink.annotation.Deeplink"

    private val deeplinkQueueClassName = ClassName("com.simple.deeplink.queue", "DeeplinkQueue")

    private val deeplinkHandlerClassName = ClassName("com.simple.deeplink", "DeeplinkHandler")

    private val deeplinkProviderClassName = ClassName("com.simple.deeplink.provider", "DeeplinkProvider")


    private val classInfoList: MutableList<ClassInfo> = arrayListOf()


    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(annotationName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(set: Set<TypeElement?>, roundEnvironment: RoundEnvironment): Boolean {

        if (processingEnv == null) {
            return false
        }

        val elements = roundEnvironment.getElementsAnnotatedWith(
            processingEnv.elementUtils.getTypeElement(annotationName)
        )

        for (element in elements) {

            val className = element.simpleName.toString()

            val packageName = processingEnv.elementUtils.getPackageOf(element).qualifiedName.toString()

            val queueName = element.annotationMirrors.firstOrNull {
                it.annotationType.toString() == annotationName
            }?.let {
                it.elementValues?.toList()?.firstOrNull()?.second?.value
            }?.let {
                println(it)
                "$it".formatAndRemoveWhitespace()
            }

            println("queueName:$queueName")
            classInfoList.add(ClassInfo(queueName = queueName ?: "Deeplink", className = className, packageName = packageName))
        }


        if (elements.isNotEmpty()) {
            return true
        }

        if (classInfoList.isEmpty()) {
            return true
        }


        val kaptKotlinGeneratedDir = processingEnv.options["kapt.kotlin.generated"] ?: return false

        val packageName = findCommonPackageName(classInfoList)

        generateScopeAndProvider(classInfoList = classInfoList, packageName = packageName, kaptKotlinGeneratedDir = kaptKotlinGeneratedDir, processingEnv = processingEnv)

        return true
    }

    private fun generateScopeAndProvider(classInfoList: List<ClassInfo>, packageName: String, kaptKotlinGeneratedDir: String, processingEnv: ProcessingEnvironment) {

        generateScopeFiles(classInfoList, packageName, kaptKotlinGeneratedDir, processingEnv)
        generateDeeplinkProvider(classInfoList, packageName, kaptKotlinGeneratedDir, processingEnv)
    }

    private fun findCommonPackageName(classInfoList: List<ClassInfo>): String {

        val packages = classInfoList.map { it.packageName }.toSet()
        return packages.reduce { acc, pkg ->
            acc.commonPrefixWith(pkg).substringBeforeLast('.')
        }
    }

    private fun generateScopeFiles(classInfoList: List<ClassInfo>, packageName: String, kaptDir: String, processingEnv: ProcessingEnvironment) {

        val autoServiceAnnotation = createAutoServiceAnnotation(deeplinkQueueClassName)

        val suffix = "Queue"
        val queueNames = classInfoList.map { it.queueName }.distinct()

        queueNames.forEach { queueName ->
            val className = "${queueName}$suffix"
            val classSpec = buildQueueClass(queueName, className, deeplinkQueueClassName, autoServiceAnnotation)
            FileSpec.builder(packageName, className)
                .addType(classSpec)
                .build()
                .writeTo(File(kaptDir))
        }

        writeMetaInf(
            classNames = queueNames.map { "${it}$suffix" },
            packageName = packageName,
            interfaceClass = deeplinkQueueClassName,
            processingEnv = processingEnv
        )
    }

    private fun buildQueueClass(queueName: String, className: String, superClass: ClassName, annotation: AnnotationSpec): TypeSpec {

        val getQueueMethod = FunSpec.builder("getQueue")
            .addModifiers(KModifier.OVERRIDE)
            .returns(String::class)
            .addStatement("return %S", queueName)
            .build()

        return TypeSpec.classBuilder(className)
            .superclass(superClass)
            .addAnnotation(annotation)
            .addModifiers(KModifier.PUBLIC)
            .addFunction(getQueueMethod)
            .build()
    }

    private fun generateDeeplinkProvider(classInfoList: List<ClassInfo>, packageName: String, kaptDir: String, processingEnv: ProcessingEnvironment) {

        val providerFunction = buildProviderFunction(classInfoList)
        val providerClass = buildProviderClass(providerFunction)

        FileSpec.builder(packageName, deeplinkProviderClassName.simpleName)
            .addType(providerClass)
            .build()
            .writeTo(File(kaptDir))

        writeMetaInf(
            classNames = listOf(deeplinkProviderClassName.simpleName),
            packageName = packageName,
            interfaceClass = deeplinkProviderClassName,
            processingEnv = processingEnv
        )
    }

    private fun buildProviderFunction(classInfoList: List<ClassInfo>): FunSpec {

        val stringClass = ClassName("kotlin", "String")
        val pairClass = ClassName("kotlin", "Pair").parameterizedBy(stringClass, deeplinkHandlerClassName)
        val listClass = ClassName("kotlin.collections", "List").parameterizedBy(pairClass)

        val builder = FunSpec.builder("provider")
            .addModifiers(KModifier.OVERRIDE, KModifier.PUBLIC)
            .returns(listClass)
            .addStatement("val result = mutableListOf<%T>()", pairClass)

        classInfoList.forEach {
            builder.addStatement("result.add(%T(%S, %T()))", pairClass, it.queueName, ClassName(it.packageName, it.className))
        }

        builder.addStatement("return result")

        return builder.build()
    }

    private fun buildProviderClass(providerFunction: FunSpec): TypeSpec {

        val keepAnnotation = AnnotationSpec.builder(ClassName("androidx.annotation", "Keep")).build()

        val autoServiceAnnotation = createAutoServiceAnnotation(deeplinkProviderClassName)

        return TypeSpec.classBuilder(deeplinkProviderClassName.simpleName)
            .superclass(deeplinkProviderClassName)
            .addAnnotation(keepAnnotation)
            .addAnnotation(autoServiceAnnotation)
            .addModifiers(KModifier.PUBLIC)
            .addFunction(providerFunction)
            .build()
    }

    private fun createAutoServiceAnnotation(baseInterface: ClassName): AnnotationSpec {

        return AnnotationSpec.builder(ClassName("com.google.auto.service", "AutoService"))
            .addMember("%T::class", baseInterface)
            .build()
    }

    private fun writeMetaInf(classNames: List<String>, packageName: String, interfaceClass: ClassName, processingEnv: ProcessingEnvironment) = kotlin.runCatching {

        val resource = processingEnv.filer.createResource(
            StandardLocation.CLASS_OUTPUT,
            "",
            "META-INF/services/${interfaceClass.canonicalName}"
        )

        resource.openWriter().use { writer ->
            classNames.forEach {
                writer.write("$packageName.$it\n")
            }
        }
    }

    private fun String.formatAndRemoveWhitespace(): String {

        val noSpaces = this.replace("\\s+".toRegex(), "")

        return if (noSpaces.isNotEmpty()) {
            noSpaces.substring(0, 1).uppercase() + noSpaces.substring(1).lowercase()
        } else {
            noSpaces
        }
    }

    data class ClassInfo(
        val queueName: String,
        val className: String,
        val packageName: String,
    )
}
