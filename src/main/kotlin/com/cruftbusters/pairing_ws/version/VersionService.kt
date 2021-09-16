package com.cruftbusters.pairing_ws.version

class VersionService {
  fun get() = javaClass.classLoader.getResource("version")!!.readText()
}
