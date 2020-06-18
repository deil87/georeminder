package georeminder.db

import cats.effect.IO
import georeminder.api.Tweet

class DatabaseAccess()
class SecurityFilter()

class TweetFinder(databaseAccess: DatabaseAccess, securityFilter: SecurityFilter) {

  def getTweet(tweetId: Int): IO[Tweet] = ???

  def getPopularTweets(): IO[Seq[Tweet]] = ???
}

trait TweetModule {
  import com.softwaremill.macwire._

  lazy val theDatabaseAccess   = wire[DatabaseAccess]
  lazy val theSecurityFilter   = wire[SecurityFilter]
  lazy val tweetFinder       = wire[TweetFinder]
}
