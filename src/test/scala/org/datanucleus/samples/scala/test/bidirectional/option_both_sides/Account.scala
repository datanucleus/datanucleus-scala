package org.datanucleus.samples.scala.test.bidirectional.option_both_sides

import org.datanucleus.scala.jdo.JDOAnnotations._
import javax.jdo.annotations.PersistenceCapable


@PersistenceCapable
class Account(
  var login: String,
  @persistent(mappedBy = "account") 
  var user: Option[User] = None) {
  def this(name:String,user:User) = this(name,Some(user))
}
