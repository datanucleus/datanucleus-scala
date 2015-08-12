package org.datanucleus.scala.test.samples.bidirectional.option_mapped_side

import javax.jdo.annotations.PersistenceCapable

@PersistenceCapable
class Customer(
  var bank: Option[Bank])