package com.painkiller.recommendations

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.matchesPattern
import org.hamcrest.junit.MatcherAssert.assertThat
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import kotlin.test.Test

@ExperimentalCoroutinesApi
@Extensions(
  ExtendWith(StartApplication::class),
  ExtendWith(TestHttpClientProvider::class),
)
class VersionBSpec {

  @TestHttpClient
  lateinit var httpClient : HttpClient

  data class Version(val version : String)

  @Test
  fun `get _version`() {
    val version = runBlocking { httpClient.get<Version>("/version") }

    assertThat(version.version, matchesPattern("\\d+\\.\\d+\\.\\d+"))
  }
}