package com.painkillergis.ktor_starter

import io.kotest.core.listeners.ProjectListener
import io.kotest.core.spec.AutoScan
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCase
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import com.cruftbusters.ktor_baseurl_util.setBaseUrl

abstract class BFunSpec(val body: FunSpec.(HttpClient) -> Unit) : FunSpec({
  register(ServerStart)
  body(this, httpClient)
}) {
  companion object {
    private const val embeddedServerBaseUrl = "http://localhost:8080"

    private val baseUrl = System.getProperty("baseUrl").ifBlank { embeddedServerBaseUrl }

    private var started = false

    private val server = embeddedServer(Netty, port = 8080) { module() }

    val httpClient = HttpClient {
      defaultRequest {
        setBaseUrl(baseUrl)
      }
      install(JsonFeature)
    }

    object ServerStart : io.kotest.core.listeners.TestListener {
      override suspend fun beforeTest(testCase: TestCase) {
        if (started || baseUrl != embeddedServerBaseUrl) return
        server.start()
        started = true
        withTimeout(4000) {
          while (!isRunning()) {
            delay(250)
          }
        }
      }

      private suspend fun isRunning() =
        try {
          httpClient.get<HttpResponse>("/version").status == HttpStatusCode.OK
        } catch (exception: Exception) {
          false
        }
    }

    @AutoScan
    object ServerStop : ProjectListener {
      override suspend fun afterProject() {
        server.stop(1000, 1000)
      }
    }
  }
}
