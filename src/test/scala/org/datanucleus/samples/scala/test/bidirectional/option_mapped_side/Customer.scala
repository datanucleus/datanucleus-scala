package org.datanucleus.samples.scala.test.bidirectional.option_mapped_side

import javax.jdo.annotations.PersistenceCapable

@PersistenceCapable
class Customer(
  var bank: Option[Bank])