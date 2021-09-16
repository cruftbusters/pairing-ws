package com.cruftbusters.pairing_ws.version

import com.cruftbusters.ktor_baseurl_util.withEmbeddedServer
import com.cruftbusters.pairing_ws.globalModules
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.mockk.every
import io.mockk.mockk

class VersionControllerKtTest : FunSpec({
  val versionService = mockk<VersionService>()
  withEmbeddedServer({
    globalModules()
    versionController(
      versionService,
    )
  }) { httpClient ->
    test("get /version returns version number") {
      every { versionService.get() } returns "the version"

      httpClient.get<HttpResponse>("/version").apply {
        status shouldBe HttpStatusCode.OK
        receive<Map<String, String>>() shouldBe mapOf("version" to "the version")
      }
    }
  }
})