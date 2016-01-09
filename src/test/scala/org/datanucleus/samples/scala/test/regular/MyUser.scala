package org.datanucleus.samples.scala.test.regular

import javax.jdo.annotations.PersistenceCapable

@PersistenceCapable
class MyUser(
  var name:String, 
  var account: MyAccount = null)