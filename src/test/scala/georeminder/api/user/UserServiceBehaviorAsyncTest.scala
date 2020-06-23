package georeminder.api.user

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class UserServiceBehaviorAsyncTest
  extends AnyFunSuite
    with BeforeAndAfterAll
    with Matchers {

  val testKit = ActorTestKit()

  test("Registration of the user returns confirmation") {
    val userService = testKit.spawn(UserServiceBehavior(), "usb")
    val probe = testKit.createTestProbe[UserServiceBehavior.RegisteredUser]()
    userService ! UserServiceBehavior.RegisterUser("hello", probe.ref)
    probe.expectMessage(UserServiceBehavior.RegisteredUser())
  }

  override def afterAll(): Unit = testKit.shutdownTestKit()
}