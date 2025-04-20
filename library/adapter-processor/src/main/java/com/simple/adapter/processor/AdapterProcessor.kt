package com.simple.adapter.processor

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
class AdapterProcessor : AbstractProcessor() {

    private val annotationName = "com.simple.adapter.annotation.ItemAdapter"

    private val adapterProviderClassName = ClassName("com.simple.adapter.provider", "AdapterProvider")


    private var classInfoList: MutableList<ClassInfo> = mutableListOf()


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

            classInfoList.add(ClassInfo(className = className, packageName = packageName))
        }

        if (elements.isNotEmpty()) {
            return true
        }

        if (classInfoList.isEmpty()) {
            return true
        }

        return generateAdapterProvider(classInfoList = classInfoList, processingEnv = processingEnv)
    }

    private fun generateAdapterProvider(classInfoList: List<ClassInfo>, processingEnv: ProcessingEnvironment): Boolean {

        val kaptDir = processingEnv.options["kapt.kotlin.generated"] ?: return false
        val packageName = findCommonPackageName(classInfoList)
        val adapterProviderFile = createAdapterProviderFile(classInfoList, packageName)

        adapterProviderFile.writeTo(File(kaptDir))
        writeMetaInfServiceFile(processingEnv, packageName)

        return true
    }

    private fun findCommonPackageName(classInfoList: List<ClassInfo>): String {

        val packages = classInfoList.map { it.packageName }.toSet()
        return packages.reduce { acc, pkg ->
            acc.commonPrefixWith(pkg).substringBeforeLast('.')
        }
    }

    private fun createAdapterProviderFile(classInfoList: List<ClassInfo>, packageName: String): FileSpec {

        val providerFunction = buildProviderFunction(classInfoList)

        val adapterProviderClass = buildAdapterProviderClass(providerFunction)

        return FileSpec.builder(packageName, "AdapterProvider")
            .addType(adapterProviderClass)
            .build()
    }

    private fun buildProviderFunction(classInfoList: List<ClassInfo>): FunSpec {

        val viewItemClass = ClassName("kotlin", "Any")
        val listOfViewItemClass = ClassName("kotlin.collections", "List").parameterizedBy(viewItemClass)

        val functionBuilder = FunSpec.builder("provider")
            .addModifiers(KModifier.OVERRIDE, KModifier.PUBLIC)
            .returns(listOfViewItemClass)
            .addStatement("val result = mutableListOf<%T>()", viewItemClass)

        classInfoList.forEach {
            val className = ClassName(it.packageName, it.className)
            functionBuilder.addStatement("result.add(%T())", className)
        }

        functionBuilder.addStatement("return result")

        return functionBuilder.build()
    }

    private fun buildAdapterProviderClass(providerFunction: FunSpec): TypeSpec {

        val keepAnnotation = AnnotationSpec.builder(ClassName("androidx.annotation", "Keep")).build()
        val autoServiceAnnotation = AnnotationSpec.builder(ClassName("com.google.auto.service", "AutoService"))
            .addMember("%T::class", adapterProviderClassName)
            .build()

        return TypeSpec.classBuilder(adapterProviderClassName.simpleName)
            .superclass(adapterProviderClassName)
            .addAnnotation(keepAnnotation)
            .addAnnotation(autoServiceAnnotation)
            .addModifiers(KModifier.PUBLIC)
            .addFunction(providerFunction)
            .build()
    }

    private fun writeMetaInfServiceFile(processingEnv: ProcessingEnvironment, packageName: String) = kotlin.runCatching {

        val providerClassName = adapterProviderClassName.canonicalName
        val resource = processingEnv.filer.createResource(
            StandardLocation.CLASS_OUTPUT,
            "",
            "META-INF/services/$providerClassName"
        )
        resource.openWriter().use { writer ->
            writer.write("$packageName.AdapterProvider\n")
        }
    }

    data class ClassInfo(
        val className: String,
        val packageName: String,
    )
}
