package org.datanucleus.scala.test.option;


import scala.util.Random
import scala.collection.JavaConverters._
import javax.jdo.ObjectState._
import javax.jdo.JDOHelper
import javax.jdo.JDOObjectNotFoundException
import org.datanucleus.samples.scala.test._
import org.datanucleus.store.types.SCO
import org.datanucleus.scala.test.BaseSpec

class OptionAttachDetachSpec extends BaseSpec with UnidirectionalSamples {

  "PersistenceManager should detach copy Option[FCO]" in {
    
    val person = newSamplePerson()
    
    val detachedPerson = transactional {
    	persist(person,false)
      pm.detachCopy(person)
    }
    
    transactional {
      assert(stateOf(detachedPerson) == DETACHED_CLEAN)
      assert(stateOf(detachedPerson.address) == DETACHED_CLEAN)
      assert(stateOf(detachedPerson.billingAddress.get) == DETACHED_CLEAN)
    }
  }
  
  "PersistenceManager should detach Option[FCO] with DetachAllOnCommit enabled" in {
    
    val person = newSamplePerson()
    
    transactional {
      pm.setDetachAllOnCommit(true);
      persist(person,false)
    }
    
    transactional {
      assert(stateOf(person) == DETACHED_CLEAN)
      assert(stateOf(person.address) == DETACHED_CLEAN)
      assert(stateOf(person.billingAddress.get) == DETACHED_CLEAN)
    }
  }
  
  // TODO attach not supported
  "PersistenceManager should attach Option[FCO] with copyOnAttach TRUE" ignore {
    
    val person = newSamplePerson()
    
    // Detach
    transactional {
      pm.setDetachAllOnCommit(true);
      persist(person,false)
    }
    
    // Attach
    transactional {
      pm.makePersistent(person)
      assert(stateOf(person.billingAddress.get) == PERSISTENT_CLEAN)
    }
  }

}