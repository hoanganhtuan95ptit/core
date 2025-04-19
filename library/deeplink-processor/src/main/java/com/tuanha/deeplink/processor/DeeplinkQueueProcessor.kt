package com.tuanha.deeplink.processor

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

class DeeplinkQueueProcessor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf("com.tuanha.deeplink.annotation.DeeplinkQueue")
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {

        println("tuanha: process")

        val elements = roundEnv.getElementsAnnotatedWith(
            processingEnv.elementUtils.getTypeElement("com.tuanha.deeplink.annotation.DeeplinkQueue")
        )

        for (element in elements) {
            val packageName = processingEnv.elementUtils.getPackageOf(element).toString()
            val className = element.simpleName.toString()
            val generatedClassName = "${className}QueueHandler"

            val superClass = ClassName("com.simple.adapter", "DeeplinkQueueHandler")

            val classSpec = TypeSpec.classBuilder(generatedClassName)
                .superclass(superClass)
                .addModifiers(KModifier.PUBLIC)
                .build()

            val fileSpec = FileSpec.builder(packageName, generatedClassName)
                .addType(classSpec)
                .build()

            val kaptKotlinGeneratedDir = processingEnv.options["kapt.kotlin.generated"] ?: return false
            fileSpec.writeTo(File(kaptKotlinGeneratedDir))
//            val className = element.simpleName.toString()
//            val packageName = processingEnv.elementUtils.getPackageOf(element).toString()
//            val generatedClassName = "${className}Generated"
//
//            val sayHelloFun = FunSpec.builder("sayHello")
//                .addStatement("println(%S)", "Hello from $generatedClassName!")
//                .build()
//
//            val generatedClass = TypeSpec.classBuilder(generatedClassName)
//                .addFunction(sayHelloFun)
//                .build()
//
//            val fileSpec = FileSpec.builder(packageName, generatedClassName)
//                .addType(generatedClass)
//                .build()
//
//            val kaptKotlinGeneratedDir = processingEnv.options["kapt.kotlin.generated"] ?: return false
//            fileSpec.writeTo(File(kaptKotlinGeneratedDir))
        }

        return true
    }
}