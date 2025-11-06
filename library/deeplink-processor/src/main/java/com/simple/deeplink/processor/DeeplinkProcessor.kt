package com.simple.deeplink.processor

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
class DeeplinkProcessor : AbstractProcessor() {

    private val annotationName = "com.simple.deeplink.annotation.Deeplink"

    private val deeplinkQueueClassName = ClassName.get("com.simple.deeplink.queue", "DeeplinkQueue")

    private val deeplinkHandlerClassName = ClassName.get("com.simple.deeplink", "DeeplinkHandler")

    private val deeplinkProviderClassName = ClassName.get("com.simple.deeplink.provider", "DeeplinkProvider")


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

        if (elements.isEmpty()) {
            return false
        }

        for (element in elements) {

            val className = element.simpleName.toString()

            val packageName = processingEnv.elementUtils.getPackageOf(element).qualifiedName.toString()

            val queueName = element.annotationMirrors.firstOrNull {
                it.annotationType.toString() == annotationName
            }?.let {
                it.elementValues?.toList()?.firstOrNull()?.second?.value
            }?.let {
                "$it".formatAndRemoveWhitespace()
            }

            classInfoList.add(ClassInfo(queueName = queueName ?: "Deeplink", className = className, packageName = packageName))
        }

        if (classInfoList.isEmpty()) {
            return false
        }

        generateScopeAndProvider(classInfoList = classInfoList, processingEnv = processingEnv)

        return true
    }

    private fun generateScopeAndProvider(classInfoList: List<ClassInfo>, processingEnv: ProcessingEnvironment) {

        val packageName = findCommonPackageName(classInfoList)

        generateScopeFiles(classInfoList, packageName, processingEnv)
        generateDeeplinkProvider(classInfoList, packageName, processingEnv)
    }

    private fun findCommonPackageName(classInfoList: List<ClassInfo>): String {

        if (classInfoList.isEmpty()) return "com.tuanha"

        val packages = classInfoList.map { it.packageName }.toSet()

        val splitPackages = packages.map { it.split(".") }

        val first = splitPackages.first()
        val prefixParts = mutableListOf<String>()

        for (i in first.indices) {
            val part = first[i]
            if (splitPackages.all { it.size > i && it[i] == part }) {
                prefixParts.add(part)
            } else {
                break
            }
        }

        return prefixParts.joinToString(".")
    }

    private fun generateScopeFiles(classInfoList: List<ClassInfo>, packageName: String, processingEnv: ProcessingEnvironment) = runCatching {

        val autoServiceAnnotation = createAutoServiceAnnotation(deeplinkQueueClassName)

        val suffix = "Queue"
        val queueNames = classInfoList.map { it.queueName }.distinct()

        queueNames.forEach { queueName ->

            val className = "${queueName}$suffix"

            val classSpec = buildQueueClass(queueName, className, deeplinkQueueClassName, autoServiceAnnotation)

            val javaFile = JavaFile.builder(packageName, classSpec)
                .build()

            // ghi trực tiếp ra Filer (chuẩn kapt/javac/ksp)
            javaFile.writeTo(processingEnv.filer)
        }
    }

    private fun buildQueueClass(queueName: String, className: String, superClass: ClassName, annotation: AnnotationSpec): TypeSpec {

        val getQueueMethod = MethodSpec.methodBuilder("getQueue")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .returns(ClassName.get(String::class.java))
            .addStatement("return \$S", queueName)
            .build()

        return TypeSpec.classBuilder(className)
            .superclass(superClass)
            .addAnnotation(annotation)
            .addModifiers(Modifier.PUBLIC)
            .addMethod(getQueueMethod)
            .build()
    }

    private fun generateDeeplinkProvider(classInfoList: List<ClassInfo>, packageName: String, processingEnv: ProcessingEnvironment) = runCatching {

        // Tạo method provider()
        val providerMethod = buildProviderFunction(classInfoList)

        // Tạo class DeeplinkProvider
        val providerClass = buildProviderClass(providerMethod)

        // Tạo file Java
        val javaFile = JavaFile.builder(packageName, providerClass)
            .build()

        // Xuất file bằng Filer (chuẩn, không cần kaptDir)
        javaFile.writeTo(processingEnv.filer)
    }

    private fun buildProviderFunction(classInfoList: List<ClassInfo>): MethodSpec {

        val stringClass = ClassName.get("java.lang", "String")
        val pairClass = ParameterizedTypeName.get(
            ClassName.get("kotlin", "Pair"),
            stringClass,
            deeplinkHandlerClassName
        )
        val listClass = ParameterizedTypeName.get(
            ClassName.get("java.util", "List"),
            pairClass
        )
        val arrayListClass = ParameterizedTypeName.get(
            ClassName.get("java.util", "ArrayList"),
            pairClass
        )

        val builder = MethodSpec.methodBuilder("provider")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .returns(listClass)
            .addStatement("\$T result = new \$T()", listClass, arrayListClass)

        classInfoList.forEach {
            builder.addStatement("result.add(new \$T(\$S, new \$T()))", pairClass, it.queueName, ClassName.get(it.packageName, it.className))
        }

        builder.addStatement("return result")

        return builder.build()
    }


    private fun buildProviderClass(providerMethod: MethodSpec): TypeSpec {

        // @Keep
        val keepAnnotation = AnnotationSpec.builder(ClassName.get("androidx.annotation", "Keep")).build()

        return TypeSpec.classBuilder(deeplinkProviderClassName.simpleName())
            .superclass(deeplinkProviderClassName)  // deeplinkProviderClassName là ClassName.get(...)
            .addAnnotation(keepAnnotation)
            .addAnnotation(createAutoServiceAnnotation(deeplinkProviderClassName))
            .addModifiers(Modifier.PUBLIC)
            .addMethod(providerMethod) // JavaPoet dùng addMethod thay vì addFunction
            .build()
    }

    private fun createAutoServiceAnnotation(baseInterface: ClassName): AnnotationSpec {

        return AnnotationSpec.builder(ClassName.get("com.simple.autobind.annotation", "AutoBind"))
            .addMember("value", "\$T.class", baseInterface)
            .build()
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
