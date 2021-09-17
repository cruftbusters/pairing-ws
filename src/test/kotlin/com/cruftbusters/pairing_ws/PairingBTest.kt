package com.cruftbusters.pairing_ws

import io.kotest.matchers.shouldBe
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*

class PairingBTest : BFunSpec({
  test("can we websocket") {
    wsClient.webSocket("/ping") {
      (incoming.receive() as Frame.Text).readText() shouldBe "ping?"
      send(Frame.Text("ping"))
      (incoming.receive() as Frame.Text).readText() shouldBe "pong"
    }
  }
  test("have conversations") {
    wsClient.webSocket("/chat") {
      val first = this
      wsClient.webSocket("/chat") {
        val second = this
        first.send(Frame.Text("hello second from first"))
        (second.incoming.receive() as Frame.Text).readText() shouldBe "hello second from first"
        wsClient.webSocket("/chat") {
          val third = this
          first.send(Frame.Text("hello second,third from first"))
          (second.incoming.receive() as Frame.Text).readText() shouldBe "hello second,third from first"
          (third.incoming.receive() as Frame.Text).readText() shouldBe "hello second,third from first"
          third.send(Frame.Text("hello first,second from third"))
          (first.incoming.receive() as Frame.Text).readText() shouldBe "hello first,second from third"
          (second.incoming.receive() as Frame.Text).readText() shouldBe "hello first,second from third"
        }
        second.send(Frame.Text("hello first from second"))
        (first.incoming.receive() as Frame.Text).readText() shouldBe "hello first from second"
      }
    }
  }
})
