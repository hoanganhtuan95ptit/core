package com.simple.crashlytics.sentry

import com.simple.crashlytics.Crashlytics
import io.sentry.Sentry
import retrofit2.HttpException
import java.util.UUID

internal val sentrySession by lazy {
    UUID.randomUUID().toString()
}

class SentryCrashlytics : Crashlytics {

    override suspend fun execute(throwable: Throwable) {

        Sentry.withScope { scope ->

            val extras = hashMapOf<String, String>()


            throwable.getHttpException()?.let {

                extras.putAll(it.toExtras(extras))
            }


            extras.map {

                scope.setExtra(it.key, it.value)
            }


            scope.setTransaction(sentrySession)

            scope.fingerprint = listOf(throwable.message)

            Sentry.captureException(RuntimeException(throwable.message + extras.getOrEmpty(CODE) + extras.getOrEmpty(URL)))
        }
    }
}

internal fun Map<String, String>.getOrEmpty(key: String) = get(key)?.let { " $it" } ?: ""

internal fun Throwable.getHttpException(): HttpException? {

    var throwable: Throwable? = this

    while (throwable !is HttpException && throwable != null) {

        throwable = throwable.cause
    }

    return throwable as? HttpException
}

internal fun HttpException.toExtras(mExtras: HashMap<String, String> = hashMapOf()): HashMap<String, String> {

    val response = response()?.raw() ?: return hashMapOf()

    val request = response.request

    val url = request.url


    val extras = hashMapOf(
        CODE to code().toString(),
    )

    if (!mExtras.contains(URL)) {

        extras[URL] = url.toString()
    }

    return extras
}

private const val URL = "url"
private const val CODE = "code"