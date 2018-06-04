package org.datanucleus.scala

import org.datanucleus.store.rdbms.mapping.java.SingleFieldMultiMapping
import java.sql.ResultSet
import org.datanucleus.metadata.AbstractMemberMetaData
import org.datanucleus.ClassLoaderResolver
import org.datanucleus.ExecutionContext
import org.datanucleus.ClassConstants
import org.datanucleus.store.rdbms.table.Table
import java.sql.PreparedStatement

class BooleanMapping extends SingleFieldMultiMapping 
{
  def getJavaType(): Class[_] = classOf[Boolean]

  override def initialize(mmd: AbstractMemberMetaData, table: Table, clr: ClassLoaderResolver) = 
  {
    super.initialize(mmd, table, clr)
    addColumns(ClassConstants.BOOLEAN.getName)
  }

  override def setObject(ex: ExecutionContext, ps: PreparedStatement, exprIndex: Array[Int], value: Object) 
  {
    ps.setBoolean(exprIndex(0), value.asInstanceOf[Boolean])
  }

  override def getObject(ex: ExecutionContext, rs: ResultSet, exprIndex: Array[Int]): Object = 
  {
    Boolean.box(rs.getBoolean(exprIndex(0)))
  }
}