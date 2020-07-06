package georeminder

import akka.actor
import akka.actor.ActorSystem
import cats.data.Kleisli
import cats.effect.ExitCase.Canceled
import cats.effect.{Blocker, ExitCode, IO, IOApp, Resource}
import georeminder.api.user.UserService
import georeminder.api.{HelloWorldService, TweetService}
import org.http4s.dsl.impl.Root
import org.http4s.{HttpApp, HttpRoutes, Request, Response}
import org.http4s.server.{Router, Server}
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.staticcontent._
import org.http4s.dsl.io._
import org.http4s.implicits._ // this is being used for '.orNotFound'

import scala.concurrent.ExecutionContext.global


object MainApp extends IOApp {

  val helloWorldService = new HelloWorldService()
  val tweetService = new TweetService()
  val userService = new UserService()

//  val system: ActorSystem[HelloWorldMain.SayHello] =
//    ActorSystem(HelloWorldMain(), "hello")

  def httpApp(blocker: Blocker):HttpApp[IO] = {
    val value: HttpRoutes[IO] = fileService(FileService.Config("./assets/static-web", blocker)) // assets is a resource directory and its content being copied to the classpath
    val tweets: HttpRoutes[IO] = tweetService.routes
    val router: HttpRoutes[IO] = Router(
      "/" -> value,
      "/hello/" -> helloWorldService.routes,
      "/api" -> tweets,
      "/user" -> userService.routes
    )
    router.orNotFound
  }

  def run(args: List[String]): IO[ExitCode] = {

    val program = for {
      blocker <- Blocker[IO]
//      server <- BlazeServerBuilder[IO](global)
//        .bindHttp(8080, "0.0.0.0")
//        .withHttpApp(httpApp(blocker))
//        .resource

    } yield {
      val res = BlazeServerBuilder[IO](global)
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(httpApp(blocker))
        .serve
        .compile
        .drain
        .as(ExitCode.Success)

      res
    }

    val value: IO[ExitCode] = program.use(io => io)
    value

//    program.guaranteeCase{
//      case Canceled =>
//        IO(println("Interrupted: releasing and exiting!"))
//      case _ =>
//        IO(println("Normal exit!"))
//    }

  }
}