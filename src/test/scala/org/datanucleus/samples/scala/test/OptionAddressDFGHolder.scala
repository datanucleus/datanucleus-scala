package org.datanucleus.samples.scala.test

import javax.jdo.annotations.PersistenceCapable
import org.datanucleus.scala.jdo.JDOAnnotations._

@PersistenceCapable
class OptionAddressDFGHolder(
  var addressDefault: Option[Address],
  @persistent(defaultFetchGroup = "true") 
  var addressEager: Option[Address],
  @persistent(defaultFetchGroup = "false") 
  var addressLazy: Option[Address]) 