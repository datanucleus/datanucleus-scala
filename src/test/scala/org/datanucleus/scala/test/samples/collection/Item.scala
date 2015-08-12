package org.datanucleus.scala.test.samples.collection

import javax.jdo.annotations.PersistenceCapable

@PersistenceCapable
class Item(code: String,
  description: String,
  price: BigDecimal)
