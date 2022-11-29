package com.one.core

import com.one.core.utils.extentions.containsChar
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
        listOf("123", "^^7", "hoang anh tuan", "hoang anh tuan 23").forEach {
            println(it + "   " + it.containsChar())
        }
        assertEquals(4, 2 + 2)
    }
}