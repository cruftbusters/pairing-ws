package com.cruftbusters.pairing_ws

import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import java.util.*

data class Session(
  val id: String,
  val start: DefaultWebSocketSession,
  var join: DefaultWebSocketSession? = null,
) {
  constructor(session: DefaultWebSocketSession) : this(UUID.randomUUID().toString(), session)
}

fun Application.startJoinController() {
  routing {
    val hosts = Collections.synchronizedList<Session>(mutableListOf())
    webSocket("/session") {
      val session = Session(this).apply(hosts::add)
      try {
        session.start.send(session.id)
        for (frame in incoming) {
          session.join?.send(frame)
        }
      } finally {
        hosts.remove(session)
      }
    }
    webSocket("/session/{id}") {
      val id = call.parameters["id"]
      val session = hosts.find { it.id == id }
      session?.join = this
      for (frame in incoming) {
        session?.start?.send(frame)
      }
    }
  }
}
