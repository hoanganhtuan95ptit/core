package com.hoanganhtuan95ptit.autobind

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.StandardLocation

@Suppress("unused")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class AutoBindProcessor : AbstractProcessor() {

    // Tên annotation mà processor sẽ xử lý
    private val annotationName = "com.hoanganhtuan95ptit.autobind.annotation.AutoBind"

    // Trả về tập annotation mà processor hỗ trợ
    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(annotationName)
    }

    // Trả về phiên bản source code mà processor hỗ trợ
    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    // Hàm chính xử lý annotation
    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {

        // Lấy tất cả class được annotate với @AutoService
        val elements = roundEnv.getElementsAnnotatedWith(
            processingEnv.elementUtils.getTypeElement(annotationName)
        )

        // Nếu không có class nào, dừng xử lý
        if (elements.isEmpty()){
            return false
        }

        // JsonArray để chứa tất cả bindings
        val arr = JsonArray()

        for (element in elements) {

            // Lấy annotation AutoService trên class này
            val ann = element.annotationMirrors.first {
                it.annotationType.toString() == annotationName
            }

            // Lấy danh sách các interface/class trong value (AutoService có thể có nhiều interface)
            val forTypeList = ann.elementValues.entries
                .first { it.key.simpleName.toString() == "value" }
                .value.value as List<*>

            // Lấy tên class implement (fully-qualified)
            val implName = (element as TypeElement).qualifiedName.toString()

            // Duyệt qua từng interface/class trong value để tạo entry JSON
            for (forType in forTypeList) if (forType != null) {

                val forTypeName = forType.toString().removeSuffix(".class")

                val obj = JsonObject()
                obj.addProperty("type", forTypeName)
                obj.addProperty("impl", implName)
                arr.add(obj)
            }
        }


        // Nếu không có binding nào được tạo, dừng xử lý
        if (arr.isEmpty) {
            return false
        }

        // Tạo object JSON cuối cùng
        val json = JsonObject()
        json.add("bindings", arr)

        // Ghi file JSON vào CLASS_OUTPUT/assets/autobind/
        writeJsonFile(json.toString())

        return true
    }

    // Hàm ghi file JSON
    private fun writeJsonFile(content: String) = kotlin.runCatching {

        val fileObject = processingEnv.filer.createResource(
            StandardLocation.CLASS_OUTPUT,
            "",
            "dummy.txt"
        )

        val path = File(fileObject.toUri())

        // Tìm moduleDir (cha của build)
        val moduleDir = generateSequence(path) { it.parentFile }
            .firstOrNull { it.name == "build" }
            ?.parentFile ?: error("Cannot find moduleDir")

        // Lấy variantName (thư mục ngay sau "classes")
        val parts = path.invariantSeparatorsPath.split("/")
        val classesIndex = parts.indexOf("classes")
        val variantName = if (classesIndex != -1 && classesIndex + 1 < parts.size) {
            parts[classesIndex + 1]
        } else {
            "main" // fallback
        }

        val moduleName = moduleDir.name

        // Ghi vào src/<variantName>/assets/autobind/<module>.json
        val assetsDir = File(moduleDir, "src/$variantName/assets/autobind").apply { mkdirs() }
        val outFile = File(assetsDir, "$moduleName.json")

        outFile.writeText(content, Charsets.UTF_8)
    }
}