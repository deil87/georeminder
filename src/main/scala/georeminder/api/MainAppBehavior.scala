package georeminder.api

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import georeminder.api.user.UserServiceBehavior

object MainAppBehavior {
  final case class NoInputCommands()

  def apply(): Behavior[NoInputCommands] =
    Behaviors.setup { context =>
      val userService = context.spawn(UserServiceBehavior(), "user_service_bhvr")
      Behaviors.same
    }
}
