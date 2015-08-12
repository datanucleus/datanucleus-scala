package org.datanucleus.scala.test.samples

import javax.jdo.annotations.PersistenceCapable
import javax.jdo.annotations.IdGeneratorStrategy.NATIVE
import org.datanucleus.scala.jdo.JDOAnnotations._

@PersistenceCapable
class Person(
  var name: String,
  var surname: Option[String],
  var address: Address,
  var billingAddress: Option[Address],
  var otherAddress: Option[Address] = None) {
  @persistent(primaryKey = "true", valueStrategy = NATIVE) var id: Long = _
}