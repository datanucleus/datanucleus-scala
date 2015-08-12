package org.datanucleus.scala.test.option

import scala.util.Random
import scala.collection.JavaConverters._
import javax.jdo.ObjectState
import javax.jdo.JDOHelper
import javax.jdo.JDOObjectNotFoundException
import org.datanucleus.scala.test.samples._
import org.datanucleus.store.types.SCO
import org.datanucleus.scala.test.BaseSpec

class OptionSpec extends BaseSpec with UnidirectionalSamples {

  "PersistenceManager should persist SCO wrapped inside Option type" in {

    val person = newSamplePerson()
    val id = persist(person)

    clearCaches()

    transactional {
      val persistedPerson = pm.getObjectById(id).asInstanceOf[Person]

      assert(persistedPerson.name == person.name)
      assert(persistedPerson.surname == person.surname)
    }
  }

  "it should persist FCO wrapped inside Option type" in {

    val person = newSamplePerson()
    val id = persist(person)

    clearCaches()

    val billingAddress = person.billingAddress.get

    transactional {
      val persistedPerson = pm.getObjectById(id).asInstanceOf[Person]

      val persistedBillingAddress = persistedPerson.billingAddress.get
      assert(persistedBillingAddress.street == billingAddress.street)
      assert(persistedBillingAddress.country == billingAddress.country)
      assert(persistedPerson.address.street == person.address.street)
    }
  }

  "it should cascade make transient" in {

    val person = newSamplePerson()
    val id = persist(person)

    val billingAddress = person.billingAddress.get
    assert(stateOf(billingAddress) == ObjectState.TRANSIENT, "Make transient should cascade")
  }

  "it should persist None values for FCOs and SCOs" in {

    val person = newSamplePerson(surname = None, billingAddress = None)

    val id = persist(person)

    clearCaches()

    transactional {
      val persistedPerson = pm.getObjectById(id).asInstanceOf[Person]

      assert(persistedPerson.surname.isEmpty)
      assert(persistedPerson.billingAddress.isEmpty)
    }
  }

  "it should persist mutable SCOs wrapped inside Option type" in {

    val task = newTaskSample()
    val dateSample = task.endDate

    val id = persist(task)

    clearCaches()

    transactional {
      val persistedTask = pm.getObjectById(id).asInstanceOf[Task]

      assert(persistedTask.endDate == dateSample)
    }
  }

  // TODO: Check with Andy if this should work. Test Collections
  // See ParametterSetter:216
  "it should update wrapped mutable SCOs" ignore {

    val task = newTaskSample()

    val id = persist(task)

    clearCaches()

    pm.close()

    // Update it
    val newTime1 = Random.nextLong()
    val newTime2 = Random.nextLong()
    transactional {
      val persistedTask = pm.getObjectById(id).asInstanceOf[Task]

      assert(persistedTask.endDate.value.isInstanceOf[SCO[_]])
      persistedTask.startDate.setTime(newTime1)
      persistedTask.endDate.map(_.setTime(newTime2))
    }

    clearCaches()
    pm.close()

    // Verify updated time
    transactional {
      val updatedTask = pm.getObjectById(id).asInstanceOf[Task]

      // Using setTime to persist/retrieve seems to be lossy so compare using a tolerance 
      assert(updatedTask.startDate.getTime === (newTime1 +- 1000))
      assert(updatedTask.endDate.value.getTime === (newTime2 +- 1000))
    }
  }

  "it should update the pc value of Option[FCO] when a field is modified" in {

    val person = newSamplePerson()

    val id = persist(person)

    clearCaches()
    pm.close()

    // Update it
    val newName = "Bob"
    val newSurname = "Marley"
    val newAddress = new Address("Updated Sample street", "Sampleland", Some("Updated Sample Country"))

    val persistedPerson = transactional {
      val persistedPerson = pm.getObjectById(id).asInstanceOf[Person]
      persistedPerson.name = newName
      persistedPerson.surname = Some(newSurname)
      persistedPerson.address = newAddress
    }

    // Make transient so we can use it to check the result later
    pm.makeTransient(newAddress)

    clearCaches()
    pm.close()

    // Verify updated values
    transactional {
      val updatedPerson = pm.getObjectById(id).asInstanceOf[Person]

      assert(updatedPerson.name == newName)
      assert(updatedPerson.surname.value == newSurname)
      assert(updatedPerson.address.street == newAddress.street)
      assert(updatedPerson.address.country.value == newAddress.country.value)
    }
  }

  "it should delete dependent Option[FCO]" in {

    val holder = new OptionAddressDependentHolder(
      Some(newSampleAddress()),
      Some(newSampleAddress()),
      Some(newSampleAddress()))

    persist(holder, makeTransient = false)

    clearCaches()
    var addressDependent: Address = null;
    val Array(idDefault, idDependent, idNotDependent) =
      transactional {

        val ids = JDOHelper.getObjectIds(
          Array[Object](holder.addressDefault.get, holder.addressDependent.get, holder.addressNotDependent.get))

        addressDependent = holder.addressDependent.get
        pm.deletePersistent(holder)
        
        assert(stateOf(addressDependent) == stateOf(holder))
        
        ids
      }
    
    clearCaches()

    transactional {
      assert(pm.getObjectById(idDefault) != null, "nothing specified, by default should keep the related PC")
      assert(pm.getObjectById(idNotDependent) != null, "need to keep, dependent=false")

      withClue("dependent should no longer exist") {
        intercept[JDOObjectNotFoundException] {
          pm.getObjectById(idDependent)
        }
      }

    }
  }

  // TODO Renato: When embedding shouldn't it ignore the of the embedded? Same with datastore id?
  "it should support embedded Option[PC]" in {
    transactional {
      val eph = new EmbeddedPersonHolder("myField", Some(newSamplePerson()))
      //val eph = new EmbeddedSimplePersonHolder("myField", new SimplePerson("Test", newSampleBillingAddress()))
      pm.makePersistent(eph)
    }
  }
}