package com.cruftbusters.pairing_ws.version

import com.cruftbusters.pairing_ws.BFunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class VersionBTest : BFunSpec({
  test("get /version returns version") {
    httpClient.get<HttpResponse>("/version").apply {
      status shouldBe HttpStatusCode.OK
      receive<Map<String, String>>()["version"] shouldMatch Regex("\\d+")
    }
  }
})
