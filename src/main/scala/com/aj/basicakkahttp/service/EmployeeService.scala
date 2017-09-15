package com.aj.basicakkahttp.service

import com.aj.basicakkahttp.domain.{Employee, EmployeeUpdate}

import scala.concurrent.{ExecutionContext, Future}

class EmployeeService(implicit val executionContext: ExecutionContext) {

  var employees = Vector.empty[Employee]

  def createEmployee(employee: Employee): Future[Option[String]] = Future {
    employees.find(_.id == employee.id) match {
      case Some(q) => None
      case None =>
        employees = employees :+ employee
        Some(employee.id)
    }
  }

  def getEmployee(id: String): Future[Option[Employee]] = Future {
    employees.find(_.id == id)
  }

  def updateEmployee(id: String, update: EmployeeUpdate): Future[Option[Employee]] = {

    def updateEntity(employee: Employee): Employee = {
      val title = update.name.getOrElse(employee.name)
      val text = update.department.getOrElse(employee.department)
      Employee(id, title, text)
    }

    getEmployee(id).flatMap { maybeEmployee =>
      maybeEmployee match {
        case None => Future { None }
        case Some(employee) =>
          val updatedEmployee = updateEntity(employee)
          deleteEmployee(id).flatMap { _ =>
            createEmployee(updatedEmployee).map(_ => Some(updatedEmployee))
          }
      }
    }
  }

  def deleteEmployee(id: String): Future[Unit] = Future {
    employees = employees.filterNot(_.id == id)
  }


}

