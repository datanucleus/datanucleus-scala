package org.datanucleus.scala.jdo

import scala.annotation.meta.field
import javax.jdo.annotations.Persistent

object JDOAnnotations {
  
    type unique = javax.jdo.annotations.Unique @field
    type join = javax.jdo.annotations.Join @field
    type persistent = javax.jdo.annotations.Persistent @field
    type extension = javax.jdo.annotations.Extension @field
    type primaryKey = javax.jdo.annotations.PrimaryKey @field
    type embedded = javax.jdo.annotations.Embedded @field
    type element = javax.jdo.annotations.Element @field
    type column = javax.jdo.annotations.Column @field
}