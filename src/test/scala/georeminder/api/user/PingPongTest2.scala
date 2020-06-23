package georeminder.api.user

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import com.typesafe.config.ConfigFactory
import org.junit.runner.RunWith
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatestplus.junit.JUnitRunner

object Echo2 {
  case class Ping(message: String, response: ActorRef[Pong])
  case class Pong(message: String)

  def apply(): Behavior[Ping] = Behaviors.receiveMessage {
    case Ping(m, replyTo) =>
      replyTo ! Pong(m)
      Behaviors.same
  }
}

object PingPongConfig {
  val config = ConfigFactory.parseString(
    """
  akka.loglevel = DEBUG
  akka.log-config-on-start = on
  """).withFallback(ConfigFactory.load())
}


@RunWith(classOf[JUnitRunner])
class PingPongTest2
  extends ScalaTestWithActorTestKit(PingPongConfig.config) with AnyFunSuiteLike {


  test("Ping pong test works") {
    val pinger = testKit.spawn(Echo2(), "ping")
    val probe = testKit.createTestProbe[Echo2.Pong]()
    pinger ! Echo2.Ping("hello", probe.ref)
    probe.expectMessage(Echo2.Pong("hello"))
  }

  override def afterAll(): Unit = testKit.shutdownTestKit()
}