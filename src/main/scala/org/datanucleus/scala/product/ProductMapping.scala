package org.datanucleus.scala.product

import org.datanucleus.store.rdbms.mapping.java.SingleFieldMultiMapping
import org.datanucleus.ClassLoaderResolver
import org.datanucleus.metadata.AbstractMemberMetaData
import org.datanucleus.store.rdbms.table.Table
import org.datanucleus.store.rdbms.RDBMSStoreManager
import scala.reflect.runtime.universe._
import java.sql.PreparedStatement
import org.datanucleus.ExecutionContext
import java.sql.ResultSet
import org.datanucleus.store.schema.table.Column
import org.datanucleus.metadata.ColumnMetaData
import org.datanucleus.metadata.FieldRole
import java.lang.reflect.Method
import org.datanucleus.metadata.ColumnMetaDataContainer
import org.datanucleus.metadata.ElementMetaData

class ProductMapping extends SingleFieldMultiMapping {

  var applyMethod: Method = _;
  var productArity: Int = _;

  override def initialize(fmd: AbstractMemberMetaData, table: Table, clr: ClassLoaderResolver) = {
    super.initialize(fmd, table, clr)
    val m = runtimeMirror(fmd.getType.getClassLoader)

    val tpe = if (roleForMember == FieldRole.ROLE_COLLECTION_ELEMENT) {
      clr.classForName(fmd.getCollection.getElementType)
    } else fmd.getType

    val classSymbol = m.classSymbol(tpe)
    val members = classSymbol.toType.members.filter(!_.isMethod)
    productArity = members.size

    applyMethod = m.runtimeClass(classSymbol).getDeclaredMethods.find(m => "apply".equals(m.getName)).get

    val types = members.map(_.typeSignature.typeSymbol.asClass.fullName)
    val names = members.map(_.name)

    val cmmdc: ColumnMetaDataContainer =
      if (roleForMember == FieldRole.ROLE_COLLECTION_ELEMENT) {
        Option(mmd.getElementMetaData).getOrElse({
          mmd.setElementMetaData(new ElementMetaData)
          mmd.getElementMetaData
        })
      } else {
        mmd
      }

    // Remove the default column
    // TODO reuse column definitions, just setting the name if not provided
    mmd.clearColumns()

    names.toList.reverse.foreach { name =>
      val clmd = new ColumnMetaData
      clmd.setName(tpe.getSimpleName + "_" + name.toString().trim())
      cmmdc.addColumn(clmd)
    }

    types.toList.reverse.foreach {
      tpe =>
        addColumns(tpe)
    }
  }

  override def initialize(storeMgr: RDBMSStoreManager, tpe: String) = ???

  override def getJavaTypeForDatastoreMapping(index: Int) = {
    super.getJavaTypeForDatastoreMapping(index)
  }

  override def setObject(ec: ExecutionContext, ps: PreparedStatement, exprIndex: Array[Int], value: Object) {
    val product = value.asInstanceOf[Product]
    val abs = mmd.getAbsoluteFieldNumber
    for(i <- 0 until productArity){
      getDatastoreMapping(i).setObject(ps, exprIndex(i), product.productElement(i))
    }
  }

  override def getObject(ec: ExecutionContext, resultSet: ResultSet, exprIndex: Array[Int]) = {
    ProductMapping.invoker(productArity).apply(applyMethod, ec, resultSet, exprIndex)
  }

  def getJavaType(): Class[_] = {
    mmd.getType
  }
}

object ProductMapping {

  val invoker = Map(
    1 -> getObject1 _,
    2 -> getObject2 _,
    3 -> getObject3 _,
    4 -> getObject4 _,
    5 -> getObject5 _,
    6 -> getObject6 _)

  def getObject1(applyMethod: Method, ec: ExecutionContext, resultSet: ResultSet, exprIndex: Array[Int]) = {
    applyMethod.invoke(null,
      resultSet.getObject(exprIndex(0)))
  }

  def getObject2(applyMethod: Method, ec: ExecutionContext, resultSet: ResultSet, exprIndex: Array[Int]) = {
    applyMethod.invoke(null,
      resultSet.getObject(exprIndex(0)),
      resultSet.getObject(exprIndex(1)))
  }

  def getObject3(applyMethod: Method, ec: ExecutionContext, resultSet: ResultSet, exprIndex: Array[Int]) = {
    applyMethod.invoke(null,
      resultSet.getObject(exprIndex(0)),
      resultSet.getObject(exprIndex(1)),
      resultSet.getObject(exprIndex(2)))
  }

  def getObject4(applyMethod: Method, ec: ExecutionContext, resultSet: ResultSet, exprIndex: Array[Int]) = {
    applyMethod.invoke(null,
      resultSet.getObject(exprIndex(0)),
      resultSet.getObject(exprIndex(1)),
      resultSet.getObject(exprIndex(2)),
      resultSet.getObject(exprIndex(3)))
  }

  def getObject5(applyMethod: Method, ec: ExecutionContext, resultSet: ResultSet, exprIndex: Array[Int]) = {
    applyMethod.invoke(null,
      resultSet.getObject(exprIndex(0)),
      resultSet.getObject(exprIndex(1)),
      resultSet.getObject(exprIndex(2)),
      resultSet.getObject(exprIndex(3)),
      resultSet.getObject(exprIndex(4)))
  }

  def getObject6(applyMethod: Method, ec: ExecutionContext, resultSet: ResultSet, exprIndex: Array[Int]) = {
    applyMethod.invoke(null,
      resultSet.getObject(exprIndex(0)),
      resultSet.getObject(exprIndex(1)),
      resultSet.getObject(exprIndex(2)),
      resultSet.getObject(exprIndex(3)),
      resultSet.getObject(exprIndex(4)),
      resultSet.getObject(exprIndex(5)))
  }
}