package com.simple.adapter.processor

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeSpec
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

@Suppress("unused")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class AdapterProcessor : AbstractProcessor() {

    private val annotationName = "com.simple.adapter.annotation.ItemAdapter"

    private val adapterProviderClassName = ClassName.get("com.simple.adapter.provider", "AdapterProvider")


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

        if (elements.isEmpty()) {
            return false
        }

        for (element in elements) {

            val className = element.simpleName.toString()
            val packageName = processingEnv.elementUtils.getPackageOf(element).qualifiedName.toString()

            classInfoList.add(ClassInfo(className = className, packageName = packageName))
        }

        if (classInfoList.isEmpty()) {
            return false
        }

        println("tuanha AdapterProcessor start")
        return generateAdapterProvider(classInfoList = classInfoList, processingEnv = processingEnv)
    }

    private fun generateAdapterProvider(classInfoList: List<ClassInfo>, processingEnv: ProcessingEnvironment): Boolean {

        val packageName = findCommonPackageName(classInfoList)
        val adapterProviderFile = createAdapterProviderFile(classInfoList, packageName)

        adapterProviderFile.writeTo(processingEnv.filer)

        return true
    }

    private fun findCommonPackageName(classInfoList: List<ClassInfo>): String {

        val packages = classInfoList.map { it.packageName }.toSet()
        return packages.reduce { acc, pkg ->
            acc.commonPrefixWith(pkg).substringBeforeLast('.')
        }
    }

    private fun createAdapterProviderFile(classInfoList: List<ClassInfo>, packageName: String): JavaFile {

        val providerFunction = buildProviderFunction(classInfoList)

        val adapterProviderClass = buildAdapterProviderClass(providerFunction)

        return JavaFile.builder(packageName, adapterProviderClass)
            .build()
    }

    private fun buildProviderFunction(classInfoList: List<ClassInfo>): MethodSpec {

        val listOfAny = ParameterizedTypeName.get(
            ClassName.get(List::class.java),
            ClassName.get(Any::class.java)
        )

        return MethodSpec.methodBuilder("provider")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .returns(listOfAny)
            .addStatement(
                "\$T result = new \$T<>()",
                ParameterizedTypeName.get(ClassName.get(List::class.java), ClassName.get(Any::class.java)),
                ClassName.get("java.util", "ArrayList")
            )
            .apply {
                classInfoList.forEach {
                    addStatement("result.add(new \$T())", ClassName.get(it.packageName, it.className))
                }
            }
            .addStatement("return result")
            .build()
    }

    private fun buildAdapterProviderClass(providerFunction: MethodSpec): TypeSpec {

        val annotationSpec = AnnotationSpec.builder(ClassName.get("com.hoanganhtuan95ptit.autobind.annotation", "AutoBind"))
            .addMember("value", "\$T.class", adapterProviderClassName)
            .build()

        return TypeSpec.classBuilder("AdapterProvider")
            .addModifiers(Modifier.PUBLIC)
            .superclass(adapterProviderClassName)
            .addAnnotation(ClassName.get("androidx.annotation", "Keep"))
            .addAnnotation(annotationSpec)
            .addMethod(providerFunction)
            .build()
    }

    data class ClassInfo(
        val className: String,
        val packageName: String,
    )
}
