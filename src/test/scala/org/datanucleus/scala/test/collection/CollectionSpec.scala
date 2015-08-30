package org.datanucleus.scala.test.collection

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

import org.datanucleus.scala.test.BaseSpec

class CollectionSpec extends BaseSpec {

  "it should persit Collection" ignore {

    val a = new Item("a")
    val b = new Item("b")
    val c = new Item("c")

    val inv = new Invoice(false, List(a, b, c))

    val id = transactional {
      persist(inv)
    }

    //    transactional {
    //      val invoice = pm.getObjectById(id).asInstanceOf[Invoice]
    //      
    //      //invoice.items = List(new Item("e"))
    //      val a1 = invoice.items.get(0)
    //      val b1 = invoice.items.get(1)
    //
    //      val filtered = invoice.items.filter { _.description == "c" }
    //      invoice.items.retainAll(filtered);
    //      
    ////      val items = invoice.items
    ////      invoice.items = null
    //      //inv.items.add(new Item("e"))
    //      //val x = inv.items.toList
    //    }

    clearCaches()

    transactional {

      val invoice = pm.getObjectById(id).asInstanceOf[Invoice]
      val items = invoice.items
      
      
      val filtered = items.filter { item => item.description == "a" }

      filtered.add(new Item("XXXX"));
      filtered.add(new Item("YYYY"));
      println("Filtered:")
      //filtered.foreach { println(_) }
      invoice.items = filtered
      
     // println("Values:")
      //invoice.items.foreach { println(_) }

    }

    
    clearCaches()

    transactional {
      val invoice = pm.getObjectById(id).asInstanceOf[Invoice]
      invoice.items.get(0);
     // invoice.items.foreach { println(_) }
    }

  }

}