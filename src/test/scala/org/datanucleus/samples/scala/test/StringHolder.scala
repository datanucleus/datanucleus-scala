package org.datanucleus.samples.scala.test

import javax.jdo.annotations.PersistenceCapable

@PersistenceCapable
class StringHolder(var str: String, var optionStr: Option[String])
