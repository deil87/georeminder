package georeminder.api.user

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.EventSourcedBehavior.CommandHandler
import akka.persistence.typed.scaladsl.{Effect, EventSourcedBehavior}
import georeminder.api.CborSerializable

object UserServiceBehavior {
  sealed trait Command
  final case class RegisterUser(name: String, replyTo: ActorRef[RegisteredUser]) extends Command

  sealed trait CommandReply extends CborSerializable
  sealed trait OperationResult extends CommandReply
  final case class RegisteredUser() extends OperationResult
  final case class Rejected(reason: String) extends OperationResult
  sealed trait Event
  final case class State()

  def apply(): Behavior[Command] =
    Behaviors.setup { context =>
      EventSourcedBehavior[Command, Event, State](
        persistenceId = PersistenceId.ofUniqueId("user_service"),
        emptyState = State(),
        commandHandler = (state, cmd) => cmd match {
          case RegisterUser(name, replyTo) =>
            context.log.info(s"User $name has been registered")
            Effect.none.thenReply(replyTo)(_ => RegisteredUser())
//            Effect.reply(replyTo)(RegisteredUser())
        },/*commandHandler = CommandHandler.command { cmd =>
          context.log.info("Got command {}", cmd)
          Effect.none
        },*/
        eventHandler = (state, evt) => state
      )
    }

}
