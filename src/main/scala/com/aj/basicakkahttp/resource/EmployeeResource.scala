package com.aj.basicakkahttp.resource

import akka.http.scaladsl.server.Route

import com.aj.basicakkahttp.domain.{Employee, EmployeeUpdate}
import com.aj.basicakkahttp.routing.MyResource
import com.aj.basicakkahttp.service.EmployeeService

trait EmployeeResource extends MyResource {

  val employeeService: EmployeeService

  def employeeRoutes: Route = pathPrefix("employee") {
    pathEnd {
      post {
        entity(as[Employee]) { employee =>
          completeWithLocationHeader(
            resourceId = employeeService.createEmployee(employee),
            ifDefinedStatus = 201, ifEmptyStatus = 409)
          }
        }
    } ~
    path(Segment) { id =>
      get {
        complete(employeeService.getEmployee(id))
      } ~
      put {
        entity(as[EmployeeUpdate]) { update =>
          complete(employeeService.updateEmployee(id, update))
        }
      } ~
      delete {
        complete(employeeService.deleteEmployee(id))
      }
    }

  }
}

