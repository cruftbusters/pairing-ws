package com.cruftbusters.pairing_ws

import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import java.lang.RuntimeException
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

data class ChatSession(val index: Int, val session: DefaultWebSocketSession) {
  companion object {
    val counter = AtomicInteger(0)
  }

  constructor(session: DefaultWebSocketSession) : this(counter.getAndIncrement(), session)
}

fun Application.chatController() {
  routing {
    val webSockets = Collections.synchronizedList<ChatSession>(mutableListOf())
    webSocket("/chat") {
      val (index) = ChatSession(this).apply(webSockets::add)
      try {
        for (frame in incoming) {
          when (frame) {
            is Frame.Text -> webSockets
              .filter { it.index != index }
              .forEach { it.session.send(frame.copy()) }
            else -> throw RuntimeException("unknown frame type")
          }
        }
      } finally {
        webSockets.removeIf { it.index == index }
      }
    }
  }
}
