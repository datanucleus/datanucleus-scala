package org.datanucleus.samples.scala.test.regular

import javax.jdo.annotations.PersistenceCapable

@PersistenceCapable
class StringContainerHolder(var names:java.util.List[String])