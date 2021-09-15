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
})
