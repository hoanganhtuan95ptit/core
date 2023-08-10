package com.one.core

import com.one.core.utils.extentions.toJson
import com.one.core.utils.extentions.toObjectOrNull
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {

        println(hashMapOf("test" to "test22").toJson().toObjectOrNull<TestV2>()?.test)

        println(listOf("123", "^^7", "hoang anh tuan", "hoang anh tuan 23").toJson().toObjectOrNull<List<String>>().toJson())

        assertEquals(4, 2 + 2)
    }

    class TestV2 {
        var test: String = ""
    }
}