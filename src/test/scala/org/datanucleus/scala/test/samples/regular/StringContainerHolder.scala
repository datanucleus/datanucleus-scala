package org.datanucleus.scala.test.samples.regular

import javax.jdo.annotations.PersistenceCapable

@PersistenceCapable
class StringContainerHolder(var names:java.util.List[String])