package org.datanucleus.scala.test.option

import scala.util.Random
import scala.collection.JavaConverters._
import javax.jdo.ObjectState
import javax.jdo.JDOHelper
import org.datanucleus.store.types.SCO
import org.datanucleus.api.jdo.NucleusJDOHelper
import org.datanucleus.samples.scala.test._
import org.datanucleus.scala.test.BaseSpec

class OptionQuerySpec extends BaseSpec with UnidirectionalSamples {

  "PersistenceManager should support defaultFetchGroup metadata" in {

    val optionHolder =
      new OptionAddressDFGHolder(
        Some(newSampleAddress("Default Street")),
        Some(newSampleAddress("Eager Street")),
        Some(newSampleAddress("Lazy Street")))

    val id = persist(optionHolder)

    clearCaches()

    val persistedHolder = transactional {
      pm.getObjectById(id).asInstanceOf[OptionAddressDFGHolder]
    }

    // Close pm to prevent lazy fetch
    pm.close()

    assert(persistedHolder.addressDefault == null, "Don't fetch defaultAddress because it is not part of DFG")
    assert(persistedHolder.addressLazy == null, "Dont't fetch, defined dfg=false explicitly")
    assert(persistedHolder.addressEager.value.street == optionHolder.addressEager.value.street, "Fetch the address field content (dfg=true)")
  }

  //TODO: Renato Test Date/String SCO with default fetch group and non-fetch group using default so it obeys the wrapped type

  "it should support lazy loading for Option[FCO]" in {

    val optionHolder =
      new OptionAddressDFGHolder(
        Some(newSampleAddress()),
        Some(newSampleAddress()),
        Some(newSampleAddress()))

    val id = persist(optionHolder)

    clearCaches()

    transactional {
      val persistedHolder = pm.getObjectById(id).asInstanceOf[OptionAddressDFGHolder]

      assert(NucleusJDOHelper.isLoaded(persistedHolder, "addressLazy", pm) == false)

      // Force load
      val addressLazy = persistedHolder.addressLazy.get
      assert(NucleusJDOHelper.isLoaded(persistedHolder, "addressLazy", pm) == true)
      assert(addressLazy.city == optionHolder.addressLazy.get.city)
    }
  }

  "it should query using an Option[SCO] as parameter" in {

    val person = newSamplePerson(surname = Some(timestamped("SampleSurname")))

    persist(person)

    clearCaches()
    pm.close()

    val query = pm.newQuery(classOf[Person], s" surname == :surname")

    transactional {
      val personResult = query.execute(person.surname).asInstanceOf[java.util.List[Person]].asScala

      assert(personResult.head.name == person.name)
      assert(personResult.head.surname.value == person.surname.value)
    }

    transactional {
      val personResult = query.execute(Some(timestamped("SampleSurname"))).asInstanceOf[java.util.List[Person]]

      assert(personResult.isEmpty)
    }
  }

  "it should query using an Option[FCO] as parameter" in {

    val person = newSamplePerson(
      name = timestamped("Sample Name"),
      billingAddress = Some(newSampleAddress(street = timestamped("SampleStreet"))))

    // Keep billing address transactional so we can use it as the parameter
    persist(person, makeTransient = false)

    // Test with transactional parameter
    transactional {
      val query = pm.newQuery(classOf[Person], " billingAddress == :billingAddress")
      val personResult = query.execute(person.billingAddress).asInstanceOf[java.util.List[Person]].asScala

      assert(personResult.head.name == person.name)
    }

    // Test with transient parameter 
    //TODO Is transient param legal? It will generate .._IOD = null instead of _IOS is null SQL. Even if generated the correct SQL,
    // the result is probably not expected. 
    //    transactional {
    //      val query = pm.newQuery(classOf[Person], s" name == :name && billingAddress == :address")
    //      val billingAddress = person.billingAddress
    //      pm.makeTransient(person.billingAddress.get)
    //      val personResult = query.execute(person.name, billingAddress).asInstanceOf[java.util.List[Person]].asScala
    //
    //      assert(personResult.isEmpty, s"There should be no person with name: ${person.name} and no billing address")
    //      assert(stateOf(billingAddress) == ObjectState.TRANSIENT, "The parameter must be kept in the same state")
    //    }
  }

  "it should query using None as parameter" in {

    val person = newSamplePerson(
      name = timestamped("Sample Name"),
      surname = None,
      billingAddress = None)

    persist(person)

    clearCaches()

    //Test with None parameter
    transactional {
      val query = pm.newQuery(classOf[Person], s" name == :name && surname == :surname && :address == billingAddress")
      val personResult = query.execute(person.name, None, None).asInstanceOf[java.util.List[Person]].asScala

      assert(personResult.head.name == person.name)
    }

  }

  "it should query referring to Option[FCO] field" in {

    val person = newSamplePerson(
      name = timestamped("Sample Name"),
      billingAddress = Some(newSampleAddress(
        street = timestamped("Sample Billing Address"),
        country = Some(timestamped("Sample Country")))))

    persist(person)

    clearCaches()

    transactional {
      val query = pm.newQuery(classOf[Person],
        s"address.street == :street && billingAddress.street == :street2 && billingAddress.country == :country")
      val personResult = query
        .execute(
          person.address.street,
          person.billingAddress.get.street,
          person.billingAddress.get.country)
        .asInstanceOf[java.util.List[Person]].asScala

      assert(personResult.head.name == person.name)
    }
  }

  "it should query using dynamic fetch group" in {

    val person = newSamplePerson(name = timestamped("Sample name"))

    persist(person)

    clearCaches()

    val query = pm.newQuery(classOf[Person], s"name == '${person.name}'")
    val fgName = "Addresses"
    val fg = pm.getFetchGroup(classOf[Person], fgName)
    fg.addMember("billingAddress")
    query.getFetchPlan().addGroup(fgName)

    transactional {
      val persistedPerson = query.execute().asInstanceOf[java.util.List[Person]].asScala.head

      assert(NucleusJDOHelper.isLoaded(persistedPerson, "billingAddress", pm) == true)
      assert(persistedPerson.name == person.name)
    }
  }

  "it should support querying individual field on Option[SCO]" in {

    val person = newSamplePerson(name = timestamped("SampleName"), surname = Some(timestamped("SampleSurname")))

    persist(person)

    clearCaches()

    transactional {
      val query = pm.newQuery(s"SELECT surname FROM org.datanucleus.samples.scala.test.Person where name == '${person.name}'")
      val result = query.execute().asInstanceOf[java.util.List[Object]]

      val surnameResult = result.asScala.head.asInstanceOf[Option[String]]
      assert(surnameResult.value === person.surname.value)

    }
  }

  "it should support querying individual field on Option[FCO]" in {

    val person = newSamplePerson(name = timestamped("SampleName"))

    persist(person)

    clearCaches()

    transactional {
      val query = pm.newQuery(s"SELECT billingAddress FROM org.datanucleus.samples.scala.test.Person where name == '${person.name}'")
      val result = query.execute().asInstanceOf[java.util.List[Object]]

      val billingAddressResult = result.asScala.head.asInstanceOf[Option[Address]]
      assert(billingAddressResult.value.street === person.billingAddress.value.street)

    }
  }

  "it should support query with aggregation for Option[SCO]" in {

    val countryToGroup = timestamped("CountryToGroup")

    val addresses = List(
      newSampleAddress(country = Some(countryToGroup)),
      newSampleAddress(country = Some(countryToGroup)),
      newSampleAddress())

    persistAll(addresses)

    clearCaches()

    transactional {
      val query = pm.newQuery(s"SELECT count(country), country FROM org.datanucleus.samples.scala.test.Address group by country")
      val result = query.execute().asInstanceOf[java.util.List[Array[Object]]]

      val Array(count, resultCountry) = result.asScala.find {
        case Array(_, Some(country)) if country == countryToGroup => true
        case _ => false;
      }.get

      assert(count === 2)
      assert(resultCountry.asInstanceOf[Option[String]].value === countryToGroup)
    }
  }

  "it should support query with aggregation for Option[FCO]" in {

    val billingAddressToGroup = newSampleAddress(street = timestamped("StreetGroup"))

    val people = List(
      newSamplePerson(billingAddress = Some(billingAddressToGroup)),
      newSamplePerson(billingAddress = Some(billingAddressToGroup)),
      newSamplePerson())

    persistAll(people)

    clearCaches()

    transactional {
      val query = pm.newQuery(s"SELECT count(billingAddress), billingAddress FROM org.datanucleus.samples.scala.test.Person group by billingAddress")
      val result = query.execute().asInstanceOf[java.util.List[Array[Object]]]

      val Array(count, resultAddress) = result.asScala.find {
        case Array(_, Some(billingAddressGroup: Address)) if billingAddressGroup.street == billingAddressToGroup.street => true
        case _ => false;
      }.get
      
      
      result.asScala.contains(Array(2,Some(billingAddressToGroup)));

      assert(count === 2)
      assert(resultAddress.asInstanceOf[Option[Address]].value.street === billingAddressToGroup.street)
    }
  }

}