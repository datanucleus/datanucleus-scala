package org.datanucleus.scala.test.samples.regular

import javax.jdo.annotations.PersistenceCapable

@PersistenceCapable
class MyCustomer(
  var bank: MyBank)