package org.datanucleus.samples.scala.test

import javax.jdo.annotations.PersistenceCapable
import java.util.Date

@PersistenceCapable
class Task(
  var description: String,
  var startDate: Date,
  var endDate:Option[Date])