package org.datanucleus.scala.test

import java.util.Date

import scala.reflect.ClassTag
import scala.reflect.runtime.{ currentMirror => cm }
import scala.reflect.runtime.universe._

import org.datanucleus.api.jdo.JDOPersistenceManager
import org.datanucleus.metadata.AbstractClassMetaData
import org.scalactic.Tolerance
import org.scalatest.BeforeAndAfterAll
import org.scalatest.BeforeAndAfterEach
import org.scalatest.FlatSpec
import org.scalatest.OptionValues

import javax.jdo.JDOHelper
import javax.jdo.PersistenceManager

abstract class BaseSpec extends org.scalatest.FreeSpec
  with BeforeAndAfterEach
  with OptionValues
  with Tolerance {

  var currentPm: PersistenceManager = _

  def useSecondLevelCache = false

  def newPm = {
    val pm = BaseSpec.pmf.getPersistenceManager()
    if (!useSecondLevelCache) pm.setProperty("datanucleus.cache.level2.type", "none")
    pm
  }

  override def beforeEach {
    currentPm = newPm
  }

  override def afterEach {
    if (!currentPm.isClosed) {
      currentPm.close()
    }
  }

  def pm = {
    if (currentPm.isClosed()) {
      currentPm = newPm
    }
    currentPm
  }

  def beginTx() = pm.currentTransaction.begin()
  def commitTx() = pm.currentTransaction.commit()
  def rollbackTx() = pm.currentTransaction.rollback()

  def transactional[T](code: => T): T = {
    val curTx = pm.currentTransaction
    try {
      if (!curTx.isActive()) curTx.begin()
      val result = code
      if (curTx.isActive()) curTx.commit()
      result
    } catch {
      case ex: Exception =>
        if (curTx.isActive()) {
          curTx.rollback()
        }
        throw ex
    }
  }

  def persist(pc: Any, makeTransient: Boolean = true, cascadeMakeTransient: Boolean = true): Any = {

    val id = transactional {
      pm.makePersistent(pc)
      pm.getObjectId(pc)
    }

    if (makeTransient) {
      pm.makeTransient(pc, cascadeMakeTransient)
    }

    id
  }

  def persistAll(pcs: List[Any], makeTransient: Boolean = true, cascadeMakeTransient: Boolean = true): List[Any] = {

    val ids = transactional {
      pcs.map {
        pc =>
          pm.makePersistent(pc)
          pm.getObjectId(pc)
      }

    }

    if (makeTransient) {
      pcs.foreach {
        pc =>
          pm.makeTransient(pc, cascadeMakeTransient)
      }
    }

    ids
  }

  def delete(pc: Any) {

    transactional {
      pm.deletePersistent(pc)
    }
  }

  def timestamped(value: String) = s"$value - ${new Date().getTime()}"

  def clearCaches() = {
    pm.evictAll()
    BaseSpec.pmf.getDataStoreCache().evictAll();
  }

  // TODO: Convert this to a macro
  def assertFieldsEqual[T: TypeTag](actual: T, expected: T, fields: String*) {

    // Comparing the same object will always pass and is probably an error, so it must fail in this case
    assert(actual !== expected, "Comparing same object is an error") // ne operator not working here?

    val tpe = typeOf[T]
    val actualIm = cm.reflect(actual)(ClassTag(actual.getClass))
    val expectedIm = cm.reflect(expected)(ClassTag(expected.getClass))

    val fields = tpe.members.collect { case m: MethodSymbol if m.isGetter => m }

    fields.foreach(
      field => {
        val actualFm = actualIm.reflectField(field)
        val expectedFm = expectedIm.reflectField(field)
        val r = actualFm.get == expectedFm.get

        assert(actualFm.get == expectedFm.get, s"Field ${field.name} is different")
      })
  }

  def stateOf(obj: Any) = JDOHelper.getObjectState(obj)

  def metaDataOf(isntance: Object): AbstractClassMetaData = {
    val mdMngr = ec.getMetaDataManager()
    mdMngr.getMetaDataForClass(isntance.getClass(), clr)
  }
  def ec = pm.asInstanceOf[JDOPersistenceManager].getExecutionContext()
  def clr = ec.getClassLoaderResolver()
}

object BaseSpec {

  val pmf = JDOHelper.getPersistenceManagerFactory("ScalaSamples")

}