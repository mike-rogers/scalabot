package org.cansado.scalabot

import org.scalatest.junit.AssertionsForJUnit
import scala.collection.mutable.ListBuffer
import org.junit.Assert._
import org.junit.Test
import org.junit.Before

import scala.xml._

class ChannelsTest extends AssertionsForJUnit {
  var scalabot:ScalaBot = null
  var config:Elem = null

  @Before def initialize() {
    scalabot = new ScalaBot()
    config = <server name="test" url="test.url" nick="testNick"><channel name="#test1"/><channel name="#test2"/></server>
    scalabot.configure(config)
  }

  @Test def verifyChannelConfigurationCount() {
    assert(scalabot.channels.size == 2)
  }

  @Test def verifyChannelConfigurationOne() {
    assert(scalabot.channels(0) == "#test1")
  }

  @Test def verifyChannelConfigurationTwo() {
    assert(scalabot.channels(1) == "#test2")
  }

}
