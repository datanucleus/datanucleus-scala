package org.datanucleus.scala.test.samples.regular

import javax.jdo.annotations.PersistenceCapable
import org.datanucleus.scala.jdo.JDOAnnotations._

@PersistenceCapable
class MyPet(var person:MyPerson) 

