package org.datanucleus.scala.test.samples.regular

import javax.jdo.annotations.PersistenceCapable

import org.datanucleus.scala.jdo.JDOAnnotations._

@PersistenceCapable
class MyDepartment(
  @persistent(mappedBy="department")
  var company:MyCompany)