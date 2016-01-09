package org.datanucleus.samples.scala.test

import java.time.LocalDate
import javax.jdo.annotations.PersistenceCapable

@PersistenceCapable
class LocalDateHolder(var localDate: LocalDate, var optionLocalDate: Option[LocalDate])