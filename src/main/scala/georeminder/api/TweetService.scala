package georeminder.api

import cats.effect.IO
import georeminder.db.{TweetFinder, TweetModule}
import org.http4s.{EntityEncoder, HttpRoutes}
import org.http4s.dsl.io._

case class Tweet(id: Int, message: String)

class TweetService {

  implicit def tweetEncoder: EntityEncoder[IO, Tweet] = ???

  implicit def tweetsEncoder: EntityEncoder[IO, Seq[Tweet]] = ???

  import com.softwaremill.macwire._
  val wired = wiredInModule(new TweetModule {})

  val tweetFinder = wired.lookup(classOf[TweetFinder]).head

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "tweets" / "popular" =>
      val tweets = tweetFinder.getPopularTweets()
      tweets.flatMap(Ok(_))
    case GET -> Root / "tweets" / IntVar(tweetId) =>
      tweetFinder.getTweet(tweetId).flatMap(Ok(_))
  }
}
