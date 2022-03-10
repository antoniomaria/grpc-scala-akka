package org.example.grpc

import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.util.ByteString
import fi.polar.services.tcxprocessor.api._

import java.io.{File, FileOutputStream}
import java.nio.file.Paths
import scala.concurrent.Future
import scala.util.{Failure, Success}

object HelloGrpc extends App {

  //val x: TrainingSessionAsTcxFileRequest = null
  //TCXProcessor

  // Boot akka
  implicit val sys = ActorSystem("HelloWorldClient")
  implicit val ec = sys.dispatcher

  // Configure the client by code:
 // val clientSettings = GrpcClientSettings.connectToServiceAt("192.168.118.78", 8094).withTls(false)
  val clientSettings = GrpcClientSettings.connectToServiceAt("localhost", 8094).withTls(false)

  // Or via application.conf:
  // val clientSettings = GrpcClientSettings.fromConfig(GreeterService.name)

  // Create a client-side stub for the service
  val client: TCXProcessorClient = TCXProcessorClient(clientSettings)

  var request = TrainingSessionAsGpxFileRequest(
    trainingSessionId = 6940732L,
    includePauseTimes = true,
    compressionType = TcxCompressionType.NONE)

  runSingleRequestReplyExample()



// https://www.gpsvisualizer.com/map?output_home
  def runSingleRequestReplyExample(): Unit = {
    println ("Performing request")
    val response = client.trainingSessionAsGpxFile(request)
    response.onComplete {
      case Success(msg) =>
        val fos = new FileOutputStream(new File("my-gpx-training.xml"))
        fos.write(msg.toByteArray)
        fos.close()

        println(s"got single reply: $msg")
      case Failure(e) =>
        e.printStackTrace()
    }
  }
}
