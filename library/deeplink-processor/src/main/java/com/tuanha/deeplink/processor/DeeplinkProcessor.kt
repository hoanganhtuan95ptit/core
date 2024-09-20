package com.tuanha.deeplink.processor

import com.google.auto.service.AutoService
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import com.tuanha.deeplink.annotation.Deeplink
import java.io.IOException
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class DeeplinkProcessor : AbstractProcessor() {

    private var filer: Filer? = null
    private var messager: Messager? = null
    private var elements: Elements? = null
    private var classList: MutableList<ClassName>? = null

    @Synchronized
    override fun init(processingEnvironment: ProcessingEnvironment) {
        this.filer = processingEnvironment.filer
        this.messager = processingEnvironment.messager
        this.elements = processingEnvironment.elementUtils
        this.classList = ArrayList()
    }

    override fun process(set: Set<TypeElement?>, roundEnvironment: RoundEnvironment): Boolean {

        val elementList = roundEnvironment.getElementsAnnotatedWith(Deeplink::class.java)

        /*
         * 1) Getting annotated classes
         */
        for (element in elementList) {

            if (element.kind != ElementKind.CLASS) {
                messager!!.printMessage(Diagnostic.Kind.ERROR, "@Deeplink should be on top of classes")
                return false
            }

            val className = ClassName.get(elements!!.getPackageOf(element).qualifiedName.toString(), element.simpleName.toString())

            classList!!.add(className)
        }


        /*
         * 2)build function
         */
        val list = ClassName.get("java.util", "List")
        val arrayList = ClassName.get("java.util", "ArrayList")

        val navigationDeepLink = ClassName.get("java.lang", "Object")
        val listOfNavigationDeepLink: TypeName = ParameterizedTypeName.get(list, navigationDeepLink)

        var builder = MethodSpec.methodBuilder("navigation")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
            .returns(listOfNavigationDeepLink)
            .addStatement("\$T result = new \$T<>()", listOfNavigationDeepLink, arrayList)

        for (className in classList!!) {
            builder = builder.addStatement("result.add(new \$T())", className)
        }

        val startMethod = builder.addStatement("return result")
            .build()

        /*
         * 3) Generate a class called Navigator that contains the static methods
         */
        val generatedClass = TypeSpec
            .classBuilder("DeeplinkManager")
            .addModifiers(Modifier.PUBLIC)
            .addMethod(startMethod)
            .build()

        /*
         * 4) Write Navigator class into file
         */
        try {
            JavaFile.builder("com.tuanha.deeplink", generatedClass).build().writeTo(filer)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return true
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(Deeplink::class.java.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }
}