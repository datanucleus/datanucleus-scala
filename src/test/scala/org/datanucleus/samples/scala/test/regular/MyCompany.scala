package org.datanucleus.samples.scala.test.regular

import javax.jdo.annotations.PersistenceCapable

@PersistenceCapable
class MyCompany{
  var department : MyDepartment = _
}