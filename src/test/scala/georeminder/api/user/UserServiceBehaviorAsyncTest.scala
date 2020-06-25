package georeminder.api.user

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import com.typesafe.config.ConfigFactory
import georeminder.api.user.UserServiceBehavior.OperationResult
import org.junit.runner.RunWith
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatestplus.junit.JUnitRunner

object TestUserBehaviorConfig {
  val config = ConfigFactory.parseString(
    """
  akka.loglevel = DEBUG

  """).withFallback(ConfigFactory.load())
}

@RunWith(classOf[JUnitRunner])
class UserServiceBehaviorAsyncTest
  extends ScalaTestWithActorTestKit(TestUserBehaviorConfig.config) with AnyFunSuiteLike  {

  test("Registration of the user returns confirmation") {
    val userService = testKit.spawn(UserServiceBehavior(), "usb")
    val probe = testKit.createTestProbe[OperationResult]()
    userService ! UserServiceBehavior.RegisterUser("hello", probe.ref)
    probe.expectMessage(UserServiceBehavior.RegisteredUser())
  }

  override def afterAll(): Unit = testKit.shutdownTestKit()
}