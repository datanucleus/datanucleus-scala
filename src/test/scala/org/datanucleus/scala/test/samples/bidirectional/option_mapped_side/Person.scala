package org.datanucleus.scala.test.samples.bidirectional.option_mapped_side

import javax.jdo.annotations.PersistenceCapable
import org.datanucleus.scala.jdo.JDOAnnotations._

@PersistenceCapable
class Person(
  @persistent(mappedBy="person")
  var pet:Option[Pet])
  
