package org.datanucleus.scala.test.samples

import javax.jdo.annotations.PersistenceCapable
import java.util.Date

@PersistenceCapable
class Task(
  var description: String,
  var startDate: Date,
  var endDate:Option[Date])