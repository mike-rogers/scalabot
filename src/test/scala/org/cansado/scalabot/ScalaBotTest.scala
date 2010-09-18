package org.cansado.scalabot

import org.scalatest.junit.AssertionsForJUnit
import scala.collection.mutable.ListBuffer
import org.junit.Assert._
import org.junit.Test
import org.junit.Before

import scala.xml._

class ScalaBotTest extends AssertionsForJUnit {
  var scalabot:ScalaBot = null
  var config:Elem = null

  @Before def initialize() {
    scalabot = new ScalaBot()
    config = <server name="test" url="test.url" nick="testNick"/>
    scalabot.configure(config)
  }

  @Test def verifySimpleConfigurationServer() {
    assert(scalabot.server == "test.url")
  }

  @Test def verifySimpleConfigurationNick() {
    assert(scalabot.getName() == "testNick")
  }

  @Test def verifySimpleConfigurationChannels() {
    assert(scalabot.channels.size == 0)
  }

}
