package io.grpc.examples.helloworld

import akka.stream.Materializer

import scala.concurrent.Future


import com.google.protobuf.timestamp.Timestamp

class GreeterImpl(implicit mat: Materializer) extends Greeter {

  /**
   * Sends a greeting
   */
  override def sayHello(in: HelloRequest): Future[HelloReply] = {
    println(s"sayHello to ${in.name}")
    Future.successful(HelloReply("Hello " + in.name))
  }
}
