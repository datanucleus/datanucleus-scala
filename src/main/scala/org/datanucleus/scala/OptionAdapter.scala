package org.datanucleus.scala

import scala.collection.JavaConverters._
import org.datanucleus.store.types.ContainerAdapter
import org.datanucleus.store.types.ElementContainerAdapter

class OptionAdapter(option: Option[Object])
  extends ElementContainerAdapter[Option[Object]](option) {

  def iterator = (option.iterator).asJava

  def add(element: Object): Unit = setContainer(Option(element))
  
  def remove(x$1: Any): Unit = setContainer(None)
  
  def clear = setContainer(None)
}