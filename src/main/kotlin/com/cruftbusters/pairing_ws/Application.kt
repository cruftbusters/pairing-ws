package com.cruftbusters.pairing_ws

import com.cruftbusters.pairing_ws.version.VersionService
import com.cruftbusters.pairing_ws.version.versionController
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.server.netty.*
import io.ktor.websocket.*

fun Application.globalModules() {
  install(ContentNegotiation) { jackson() }
  install(WebSockets)
}

fun Application.controllers() {
  pingController()
  versionController(
    VersionService(),
  )
}

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
  globalModules()
  controllers()
}
