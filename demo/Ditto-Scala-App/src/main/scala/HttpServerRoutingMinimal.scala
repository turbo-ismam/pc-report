
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.util.Timeout

import java.net.InetAddress
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.io.StdIn
import scala.language.postfixOps
import scala.util.Failure
import scala.util.Success


object HttpServerRoutingMinimal extends App {

  /*implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext: ExecutionContextExecutor = system.executionContext
  implicit val timeout: Timeout = Timeout(2 seconds)

  private val route =path("hello") {
      get {
        println("Received request on port 8011")
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,
          Await.result(AkkaHttp.doRequest(),timeout.duration)))
      }
    }

  private val bindingFuture = Http().newServerAt("0.0.0.0", 8011).bind(route)

  private val ipAddress = InetAddress.getLocalHost.getHostAddress
  println(s"Host IP Address: $ipAddress")
  println(s"Server now online. Please navigate to http://localhost:8011/hello\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return



  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done*/

  implicit val system: ActorSystem[Unit] = ActorSystem(Behaviors.empty, "my-system")

  import scala.concurrent.ExecutionContext.Implicits.global

  private val route = path("hello"){
    get {
      println("Received request on port 8011")

      val result = AkkaHttp.doRequest()

      complete(HttpEntity(ContentTypes.`application/json`, result))
    }
  }

  Http().newServerAt("0.0.0.0",8011)
    .bind(route)
    .onComplete {
      case Failure(exception) => exception.printStackTrace()
      case Success(value) =>
        val ipAddress = InetAddress.getLocalHost.getHostAddress
        println(s"Host IP Address: $ipAddress")
        println(s"Server now online. Please navigate to http://localhost:8011/hello\nPress RETURN to stop...")
        StdIn.readLine() // let it run until user presses return
        println("Server now offline, terminating")
        value.unbind()
        system.terminate()
    }

}
