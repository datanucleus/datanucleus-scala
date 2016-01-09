package org.datanucleus.scala.test

import org.datanucleus.api.jdo.JDOPersistenceManagerFactory
import javax.jdo.JDOHelper

object SimpleApp extends App {

  println("CL:" + classOf[JDOPersistenceManagerFactory].getClassLoader())
  JDOHelper.getPersistenceManagerFactory("ScalaSamples")
  //        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("MyTest");
  //        PersistenceManager pm = pmf.getPersistenceManager();
  //        
  //        pm.currentTransaction().begin();
  //        Address address = new Address("TEST");
  //        pm.makePersistent(address);

}