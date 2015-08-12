package org.datanucleus.scala.test.samples.regular

import javax.jdo.annotations.PersistenceCapable

@PersistenceCapable
class MyCompany{
  var department : MyDepartment = _
}