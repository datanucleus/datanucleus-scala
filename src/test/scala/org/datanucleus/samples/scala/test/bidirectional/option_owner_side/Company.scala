package org.datanucleus.samples.scala.test.bidirectional.option_owner_side

import javax.jdo.annotations.PersistenceCapable
import scala.util.Either
import org.datanucleus.scala.jdo.JDOAnnotations._

@PersistenceCapable
class Company(var department:Option[Departament])

//class Company2(var allocation:Either[Departament,Room])
//class Room(
//  @persistent(mappedBy="allocation.right")
//  var company:Company2)
//  
//class Item(
//  var location:Either[Warehouse,Factory])
//
//class Warehouse(
//  @persistent(mappedBy="location,allocated")
//  var content:Either[Item,Person])
//
//class Person(
//  var allocated: Either[Warehouse,Factory]
//  )
//
//
//class Factory(
//  @persistent(mappedBy="location")
//  var item:Item)
  