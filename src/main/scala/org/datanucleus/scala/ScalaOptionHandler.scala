package org.datanucleus.scala

import org.datanucleus.ClassLoaderResolver
import org.datanucleus.metadata.AbstractMemberMetaData
import org.datanucleus.metadata.ColumnMetaData
import org.datanucleus.metadata.ContainerMetaData
import org.datanucleus.metadata.MetaDataManager
import org.datanucleus.store.types.containers.CollectionHandler

class ScalaOptionHandler
    extends CollectionHandler[Option[Object]] {

  override def newContainer(): Option[Object] = Option.empty
  override def newContainer(elements: Object*): Option[Object] = Option(elements.head)

  override def getAdapter(option: Option[Object]): OptionAdapter = new OptionAdapter(option)

  override def populateMetaData(mmgr: MetaDataManager, mmd: AbstractMemberMetaData) = {
    super.populateMetaData(mmgr, mmd)

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

  }

  override def isDefaultFetchGroup(clr: ClassLoaderResolver, mmgr: MetaDataManager, mmd: AbstractMemberMetaData) = {
    val elementTypeName = mmd.getCollection.getElementType
    val elementType = clr.classForName(elementTypeName);

    val typeMgr = mmgr.getNucleusContext.getTypeManager

    typeMgr.isDefaultFetchGroup(elementType)
  }
}