package org.datanucleus.samples.scala.test

import javax.jdo.annotations.PersistenceCapable
import org.datanucleus.scala.jdo.JDOAnnotations._
import javax.jdo.annotations.DatastoreIdentity
import javax.jdo.annotations.IdGeneratorStrategy
import javax.jdo.annotations.Sequence
import javax.jdo.annotations.SequenceStrategy

@PersistenceCapable
class EmailAddressHolder(
  var email: EmailAddress){
  
  @primaryKey
  @persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
  var id: java.lang.Long= _
}

