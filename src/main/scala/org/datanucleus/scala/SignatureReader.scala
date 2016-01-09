package org.datanucleus.scala

import scala.tools.scalap.scalax.rules.scalasig._
import org.datanucleus.exceptions.NucleusException

/** Based on Lift Json code */
//https://raw.githubusercontent.com/lift/framework/2.4-release/core/json/src/main/scala/net/liftweb/json/ScalaSig.scala
object ScalaSigReader {

  def readFieldTypeName(name: String, clazz: Class[_], typeArgIndex: Int): String = {
    def read(current: Class[_]): MethodSymbol = {
      if (current == null)
        fail("Can't find field " + name + " from " + clazz)
      else
        findField(findClass(current), name).getOrElse(read(current.getSuperclass))
    }
    findArgTypeName(read(clazz), typeArgIndex)
  }

  private def findClass(clazz: Class[_]): ClassSymbol = {
    val sig = findScalaSig(clazz).getOrElse(fail("Can't find ScalaSig for " + clazz))
    findClass(sig, clazz).getOrElse(fail("Can't find " + clazz + " from parsed ScalaSig"))
  }

  private def findClass(sig: ScalaSig, clazz: Class[_]): Option[ClassSymbol] = {
    sig.symbols.collect { case c: ClassSymbol if !c.isModule => c }.find(_.name == clazz.getSimpleName).orElse {
      sig.topLevelClasses.find(_.symbolInfo.name == clazz.getSimpleName).orElse {
        sig.topLevelObjects.map { obj =>
          val t = obj.infoType.asInstanceOf[TypeRefType]
          t.symbol.children collect { case c: ClassSymbol => c } find (_.symbolInfo.name == clazz.getSimpleName)
        }.head
      }
    }
  }

  private def findField(c: ClassSymbol, name: String): Option[MethodSymbol] =
    (c.children collect { case m: MethodSymbol if m.name == name => m }).headOption

  private def findArgTypeName(s: MethodSymbol, typeArgIdx: Int): String = {
    def resultType = try {
      s.infoType.asInstanceOf[{ def resultType: Type }].resultType
    } catch {
      case e: java.lang.NoSuchMethodException => s.infoType.asInstanceOf[{ def typeRef: Type }].typeRef
    }

    val t = resultType match {
      case TypeRefType(_, _, args) => args(typeArgIdx)
    }

    def findPrimitive(t: Type): Symbol = t match {
      case TypeRefType(ThisType(_), symbol, _)      => symbol
      case TypeRefType(SingleType(_, _), symbol, _) => symbol
      case ref @ TypeRefType(_, _, _)               => findPrimitive(ref)
      case x                                        => fail("Unexpected type info " + x)
    }

    findPrimitive(t).path
  }

  private def findScalaSig(clazz: Class[_]): Option[ScalaSig] =
    ScalaSigParser.parse(clazz).orElse(findScalaSig(clazz.getDeclaringClass))

  private def fail(msg: String) = throw new NucleusException(msg)
}