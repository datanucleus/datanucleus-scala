package org.datanucleus.samples.scala.test.regular

import javax.jdo.annotations.PersistenceCapable

import org.datanucleus.scala.jdo.JDOAnnotations._

@PersistenceCapable
class MyBank(
  @persistent(mappedBy = "bank")
  @join
  var customers:java.util.List[MyCustomer]) 