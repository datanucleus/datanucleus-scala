package org.datanucleus.samples.scala.test.regular

import javax.jdo.annotations.PersistenceCapable
import org.datanucleus.scala.jdo.JDOAnnotations._

@PersistenceCapable
class MyPet(var person:MyPerson) 

