package org.datanucleus.scala.test.samples

import javax.jdo.annotations.PersistenceCapable
import javax.jdo.annotations.IdGeneratorStrategy.NATIVE
import org.datanucleus.scala.jdo.JDOAnnotations._

@PersistenceCapable
class Address(
  var street: String,
  var city: String,
  var country: Option[String]) {
  @persistent(primaryKey = "true", valueStrategy = NATIVE) var id: Long = _
}
