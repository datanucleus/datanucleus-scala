package org.datanucleus.samples.scala.test

import javax.jdo.annotations.PersistenceCapable
import org.datanucleus.scala.jdo.JDOAnnotations._

@PersistenceCapable
class EmbeddedPersonHolder(
  var ownField:String,
  @persistent(embeddedElement="true")
  var person:Option[Person])
  
@PersistenceCapable
class EmbeddedSimplePersonHolder(
  var ownField:String,
  @persistent(embedded="true")
  var person:SimplePerson)  

@PersistenceCapable
class SimplePerson(
  var name: String,
  var address: Address)
