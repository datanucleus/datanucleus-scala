package org.datanucleus.samples.scala.test

import javax.jdo.annotations.PersistenceCapable
import java.time._
@PersistenceCapable
class BooleanHolder(var booleanValue: Option[Boolean])