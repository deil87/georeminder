package georeminder.api.user

import cats.effect.IO
import georeminder.api.Tweet
import org.http4s.dsl.impl.{IntVar, Root}
import org.http4s.{EntityEncoder, HttpRoutes, UrlForm}
import org.http4s.dsl.io._
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._

case class User(fname: String, lname: String)

class UserService {

  val userFinder = null

  implicit val decoder = jsonOf[IO, User]

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req @ POST -> Root =>
//      println("Registered user by JSON body load")
//      for {
//        user <- req.as[User]
//        resp <- Ok(s"Registered user: ${user.fname} ${user.lname}")
//      } yield (resp)
      req
        .decode[UrlForm] { data =>
          val fname = data.values.get("fname").flatMap(_.uncons) match {
            case Some((s, _)) => s
            case None => BadRequest(s"Invalid data: " + data)
          }
          val lname = data.values.get("lname").flatMap(_.uncons) match {
            case Some((s, _)) => s
            case None => BadRequest(s"Invalid data: " + data)
          }
          Ok(fname + " " + lname)
        }

    case GET -> Root / IntVar(userId) =>
      println(s"Retrieved userId: $userId")
      Ok("get userId")
  }
}
