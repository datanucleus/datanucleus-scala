package org.datanucleus.scala.test.collection

import javax.jdo.annotations.PersistenceCapable

@PersistenceCapable
class Item(var description:String){
  
  override def toString() ={
    description
  }
}