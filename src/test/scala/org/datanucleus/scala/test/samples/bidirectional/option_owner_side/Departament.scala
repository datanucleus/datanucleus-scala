package org.datanucleus.scala.test.samples.bidirectional.option_owner_side

import javax.jdo.annotations.PersistenceCapable

import org.datanucleus.scala.jdo.JDOAnnotations._

@PersistenceCapable
class Departament(
  @persistent(mappedBy="department")
  var company:Company)
