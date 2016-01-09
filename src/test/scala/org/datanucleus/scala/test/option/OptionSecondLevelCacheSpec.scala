package org.datanucleus.scala.test.option

import javax.jdo.JDOHelper
import org.datanucleus.samples.scala.test.Person
import org.datanucleus.samples.scala.test.Person
import scala.collection.JavaConverters._
import org.datanucleus.scala.test.BaseSpec

class OptionSecondLevelCacheSpec
  extends BaseSpec
  with UnidirectionalSamples {

  // Enable 2nd level cache usage
  override def useSecondLevelCache = true

  "it should use 2nd level cache" in {

    val person = newSamplePerson(name = timestamped("Sample name"))
    persist(person, cascadeMakeTransient = false)
    val billingAddress_ = person.billingAddress.get
    val addressId = JDOHelper.getObjectId(billingAddress_)
    // Make transient so we can use it to compare later
    pm.makeTransient(billingAddress_)

    transactional {
      // Remove the object directly from the DB to make sure it will be 
      // loaded from the 2nd level cache
      pm.newQuery("javax.jdo.query.SQL",
        s"UPDATE PERSON SET BILLINGADDRESS_ID_OID = null where BILLINGADDRESS_ID_OID = $addressId")
        .execute()

      pm.newQuery("javax.jdo.query.SQL",
        s"DELETE FROM ADDRESS WHERE ID = $addressId")
        .execute()

      val query = pm.newQuery(classOf[Person], s"name == '${person.name}'")
      val persistedPerson = query.execute().asInstanceOf[java.util.List[Person]].asScala.head

      assert(persistedPerson.name == person.name)
      assert(persistedPerson.surname == person.surname)
      assert(persistedPerson.billingAddress ne person.billingAddress, "Has to be a copy")
      assert(persistedPerson.billingAddress.value.street == billingAddress_.street)
    }
  }

}