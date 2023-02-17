import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.BasicHttpCredentials
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, headers}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

private object AkkaHttp {

  implicit val system: ActorSystem = ActorSystem()
  import system.dispatcher

  private val request = HttpRequest(
    method = HttpMethods.GET,
    uri = "http://localhost:8001/api/2/things/org.eclipse.ditto:fancy-car",
  ).withHeaders(headers.Authorization(BasicHttpCredentials("ditto","ditto")))

  def doRequest(): Future[String] = {
    println("Sending request to Ditto Cluster ...")
    val responseFuture = Http().singleRequest(request)
    responseFuture.flatMap(_.entity.toStrict(2 seconds)).map(_.data.utf8String)
  }

}
