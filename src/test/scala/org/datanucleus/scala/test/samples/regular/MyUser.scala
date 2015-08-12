package org.datanucleus.scala.test.samples.regular

import javax.jdo.annotations.PersistenceCapable

@PersistenceCapable
class MyUser(
  var name:String, 
  var account: MyAccount = null)