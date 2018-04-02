package org.datanucleus.scala.test

import java.time.LocalDate
import java.util
import java.util.UUID
import java.util.concurrent.ThreadLocalRandom

import scala.util.Random

trait RandomUtils {
  def randomString(): String = UUID.randomUUID().toString

  def randomLocalDate(): LocalDate = {
    val minDay = LocalDate.of(1970, 1, 1).toEpochDay
    val maxDay = LocalDate.of(2095, 12, 31).toEpochDay
    val randomDay = ThreadLocalRandom.current.nextLong(minDay, maxDay)

    LocalDate.ofEpochDay(randomDay)
  }
}
