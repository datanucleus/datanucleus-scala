package org.datanucleus.scala.test.collection

import javax.jdo.annotations.PersistenceCapable

import org.datanucleus.scala.jdo.JDOAnnotations._
import java.util._

@PersistenceCapable
class Invoice(
  var paid:Boolean,
  //var paymentDate:Option[Date],
  @persistent(dependentElement="true")
  var items:List[Item])