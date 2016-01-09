package org.datanucleus.samples.scala.test.bidirectional.option_both_sides

import javax.jdo.annotations.PersistenceCapable

@PersistenceCapable
class User(
  var name: String,
  var account: Option[Account] = None) {
  def this(name:String,account:Account) = this(name,Some(account))
}