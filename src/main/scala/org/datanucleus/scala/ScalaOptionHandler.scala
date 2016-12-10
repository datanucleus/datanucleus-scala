package org.datanucleus.scala

import org.datanucleus.ClassLoaderResolver
import org.datanucleus.metadata.AbstractMemberMetaData
import org.datanucleus.metadata.ColumnMetaData
import org.datanucleus.metadata.MetaDataManager
import org.datanucleus.store.types.TypeManager
import org.datanucleus.store.types.containers.CollectionHandler
import org.datanucleus.enhancement.Persistable
import org.datanucleus.enhancer.EnhancementHelper
import javax.jdo.JDOHelper

class ScalaOptionHandler
    extends CollectionHandler[Option[Object]] {

  var _clr: ClassLoaderResolver = _
  var _prm: ClassLoader = _

  override def newContainer(mmd: AbstractMemberMetaData): Option[Object] = Option.empty
  override def newContainer(mmd: AbstractMemberMetaData, elements: Object*): Option[Object] = Option(elements.head)

  override def getAdapter(option: Option[Object]): OptionAdapter = new OptionAdapter(option)
  override def populateMetaData(clr: ClassLoaderResolver, primary: ClassLoader, mmd: AbstractMemberMetaData) = {

    _prm = primary
    _clr = clr
    mmd.getCollection.setSingleElement(true)

    // Get columns defined metadata - not visible
    val columns = new AbstractMemberMetaData(mmd.getParent(), mmd) {
      def getColumns = columns;
    }.getColumns;

    if (columns == null || columns.size() == 0) {
      // Option should allow nullable by default
      val colmd = new ColumnMetaData()
      colmd.setAllowsNull(true)
      mmd.addColumn(colmd)
    }

    super.populateMetaData(clr, primary, mmd)
  }

  /** Java reflection doesn't work in some scenarios such as using scala.Boolean
    * as a type parameter because they are boxed/unboxed by the compiler so the
    * type for Java reflection will be Object, hence we have to use Scala reflection
    * to determine the type. However Scala reflection API from scala.reflect will
    * cause the related classes to be loaded when building the reflection symbols,
    * which will cause the initialization of their metadata as well and that can
    * cause issues, specially when datanucleus.metadata.autoregistration is enabled.
    * To workaround this problem we read the type name straight from
    * the ScalaSignature using scalap API.
    */
  override protected def getElementType(mmd: AbstractMemberMetaData): String = {

    val clazz = mmd.getMemberRepresented.getDeclaringClass
    ScalaSigReader.readFieldTypeName(mmd.getName, clazz, 0)

  }

  override def isDefaultFetchGroup(clr: ClassLoaderResolver, typeMgr: TypeManager, mmd: AbstractMemberMetaData) = {
    val elementTypeName = mmd.getCollection.getElementType
    val elementType = clr.classForName(elementTypeName);

    typeMgr.isDefaultFetchGroup(elementType)
  }

}