package com.one.coreapp

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest : KoinComponent {

    @Before
    fun before() {
        val apiModule = module {
        }

        startKoin {
            modules(apiModule)
        }
    }

    @After
    fun after() {

    }

    @Test
    fun getLookUpLeagueList() {

    }
}