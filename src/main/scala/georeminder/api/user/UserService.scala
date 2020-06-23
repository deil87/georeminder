package georeminder.api.user

import cats.effect.IO
import georeminder.api.Tweet
import org.http4s.{EntityEncoder, HttpRoutes}

case class User(id: Int, message: String)

class UserService {

  implicit def tweetEncoder: EntityEncoder[IO, Tweet] = ???

  implicit def tweetsEncoder: EntityEncoder[IO, Seq[Tweet]] = ???

  val userFinder = null

  val routes: HttpRoutes[IO] = null /*HttpRoutes.of[IO] {
    case GET -> Root / "tweets" / "popular" =>
      val tweets = tweetFinder.getPopularTweets()
      tweets.flatMap(Ok(_))
    case GET -> Root / "tweets" / IntVar(tweetId) =>
      tweetFinder.getTweet(tweetId).flatMap(Ok(_))
  }*/
}
