package com.cruftbusters.pairing_ws

import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import java.lang.RuntimeException

fun Application.pingController() {
  routing {
    webSocket("/ping") {
      send(Frame.Text("ping?"))
      for (frame in incoming) {
        when (frame) {
          is Frame.Text -> {
            if (frame.readText() == "ping") send(Frame.Text("pong"))
            else send(Frame.Text("ping?"))
          }
          else -> throw RuntimeException("unknown frame type")
        }
      }
    }
  }
}
