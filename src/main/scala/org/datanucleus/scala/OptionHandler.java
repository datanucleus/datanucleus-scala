package org.datanucleus.scala;

import scala.Option;

/**
 * This class is a workaround for https://issues.scala-lang.org/browse/SI-1459
 * Once this issue is solved the ScalaOptionHandler class can be used.
 */
public class OptionHandler extends ScalaOptionHandler {
	@Override
	public Option<Object> newContainer(Object... elements) {
		return Option.apply(elements[0]);
	}
}
