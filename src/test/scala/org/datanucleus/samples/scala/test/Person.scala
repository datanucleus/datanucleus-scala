package org.datanucleus.samples.scala.test

import javax.jdo.annotations.PersistenceCapable
import javax.jdo.annotations.IdGeneratorStrategy.NATIVE
import org.datanucleus.scala.jdo.JDOAnnotations._

@PersistenceCapable
class Person(
  var name: String,
  var surname: Option[String],
  var address: Address,
  @persistent
  var billingAddress: Option[Address],
  @persistent
  var otherAddress: Option[Address] = None) {
  @persistent(primaryKey = "true", valueStrategy = NATIVE) var id: Long = _
}