package org.hibernate.engine.query;

import org.hibernate.type.Type;

import java.io.Serializable;

/**
 * @author <a href="mailto:steve@hibernate.org">Steve Ebersole </a>
 */
public class NamedParameterDescriptor implements Serializable {
	private final String name;
	private final Type expectedType;
	private final int[] sourceLocations;

	public NamedParameterDescriptor(String name, Type expectedType, int[] sourceLocations) {
		this.name = name;
		this.expectedType = expectedType;
		this.sourceLocations = sourceLocations;
	}

	public String getName() {
		return name;
	}

	public Type getExpectedType() {
		return expectedType;
	}

	public int[] getSourceLocations() {
		return sourceLocations;
	}
}
