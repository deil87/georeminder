package georeminder

import cats.effect.ExitCase.Canceled
import cats.effect.{ExitCode, IO, IOApp}
import georeminder.api.{HelloWorldService, TweetService}
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder


object MainApp extends IOApp {

  val helloWorldService = new HelloWorldService()
  val tweetService = new TweetService()


  val httpApp = Router("/" -> helloWorldService.routes, "/api" -> tweetService.routes).orNotFound

  def run(args: List[String]): IO[ExitCode] = {

    val program: IO[ExitCode] = BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)

    program.guaranteeCase{
      case Canceled =>
        IO(println("Interrupted: releasing and exiting!"))
      case _ =>
        IO(println("Normal exit!"))
    }
  }
}