package org.datanucleus.samples.scala.test

import javax.jdo.annotations.PersistenceCapable
import org.datanucleus.scala.jdo.JDOAnnotations._


@PersistenceCapable
class OptionAddressDependentHolder(
  var addressDefault: Option[Address],
  @persistent(dependentElement = "true")
  var addressDependent: Option[Address],
  @persistent(dependentElement = "false")
  var addressNotDependent: Option[Address])
