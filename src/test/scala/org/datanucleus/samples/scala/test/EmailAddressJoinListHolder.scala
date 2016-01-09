package org.datanucleus.samples.scala.test

import javax.jdo.annotations.PersistenceCapable
import org.datanucleus.scala.jdo.JDOAnnotations._
@PersistenceCapable
class EmailAddressJoinListHolder(
  @join
  var emailAddressList: java.util.List[EmailAddress]) 
