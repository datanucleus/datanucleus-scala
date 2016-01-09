package org.datanucleus.scala.test

import java.time.LocalDate
import org.datanucleus.samples.scala.test.BooleanHolder

class TypesSpec extends BaseSpec {
  "it should support scala.Boolean" in {

    val booleanHolder = new BooleanHolder(Some(true))
    
    val id = persist(booleanHolder)

    clearCaches()

    transactional {
      val loadedHolder = pm.getObjectById(id).asInstanceOf[BooleanHolder]
      assert { loadedHolder.booleanValue.value === true }
    }

  }
}