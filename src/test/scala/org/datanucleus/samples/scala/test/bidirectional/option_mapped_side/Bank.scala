package org.datanucleus.samples.scala.test.bidirectional.option_mapped_side

import javax.jdo.annotations.PersistenceCapable
import org.datanucleus.scala.jdo.JDOAnnotations._

@PersistenceCapable
class Bank(
  @persistent(mappedBy = "bank")
  @join
  var customers:java.util.List[Customer]) 