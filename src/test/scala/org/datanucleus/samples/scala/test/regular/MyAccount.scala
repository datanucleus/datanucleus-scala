package org.datanucleus.samples.scala.test.regular

import org.datanucleus.scala.jdo.JDOAnnotations._
import javax.jdo.annotations.PersistenceCapable

@PersistenceCapable
class MyAccount(
  var login: String,
  @persistent(mappedBy = "account") 
  var user: MyUser = null)
