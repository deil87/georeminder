package georeminder.api

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.io._

class HelloWorldService {

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name =>
      Ok(s"Hello, $name.")
  }
}
