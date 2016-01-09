package org.datanucleus.scala.test.option

import java.util.Date

import org.datanucleus.samples.scala.test.Address
import org.datanucleus.samples.scala.test.Person
import org.datanucleus.samples.scala.test.Task

import javax.jdo.annotations.PersistenceCapable

trait UnidirectionalSamples {

  def newSampleAddress(
    street: String = "Sample street",
    suburb: String = "Sampleland",
    country: Option[String] = Some("Sample Country")) = new Address(street, suburb, country)

  def newSampleBillingAddress() = new Address("Sample Billing street", "Billing Sampleland", Some("Billing Sample Country"))

  def newSamplePerson(
    name: String = "Jimi",
    surname: Option[String] = Some("Hendrix"),
    address: Address = newSampleAddress(),
    billingAddress: Option[Address] = Some(newSampleBillingAddress())) = new Person(name, surname, address, billingAddress)

  def newDateSample() = new Date()
  def newTaskSample() = new Task("Sample Event", newDateSample(), Some(newDateSample()))
  
}