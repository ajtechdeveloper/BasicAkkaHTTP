package com.aj.basicakkahttp

import akka.http.scaladsl.server.Route
import com.aj.basicakkahttp.resource.EmployeeResource
import com.aj.basicakkahttp.service.EmployeeService

import scala.concurrent.ExecutionContext

trait RestInterface extends Resources {

  implicit def executionContext: ExecutionContext

  lazy val employeeService = new EmployeeService

  val routes: Route = employeeRoutes

}

trait Resources extends EmployeeResource

