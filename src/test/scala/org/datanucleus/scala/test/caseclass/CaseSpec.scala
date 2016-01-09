package org.datanucleus.scala.test.caseclass

import scala.collection.JavaConverters.seqAsJavaListConverter
import org.datanucleus.scala.test.BaseSpec
import org.datanucleus.samples.scala.test.EmailAddressJoinListHolder
import org.datanucleus.samples.scala.test.EmailAddress
import org.datanucleus.samples.scala.test.EmailAddressHolder

class CaseSpec extends BaseSpec {

  "it should persit Case" in {

    val emailAddress = EmailAddress("me", "gmail.com")
    val emailHolder = new EmailAddressHolder(emailAddress)

    val id = persist(emailHolder)
    clearCaches()

    transactional {
      val loadedHolder = pm.getObjectById(id).asInstanceOf[EmailAddressHolder]
      assert { loadedHolder.email === emailAddress }
    }

  }
  
  "it should persit Case case mapped as Collection Element in Join table" ignore {

    val emailAddress = EmailAddress("me", "gmail.com")
    val emailList = List(emailAddress).asJava
    val emailHolder = new EmailAddressJoinListHolder(emailList)

    val id = persist(emailHolder)
    clearCaches()

    transactional {
      val loadedHolder = pm.getObjectById(id).asInstanceOf[EmailAddressJoinListHolder]
      assert { loadedHolder.emailAddressList === emailList }
    }

  }
}

