package org.springframework.github;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

/**
 * @author Marcin Grzejszczak
 */
public class Pojos {
	private final List<Pojo> data;

	@JsonCreator
	public Pojos(List<Pojo> data) {
		this.data = data;
	}

	public List<Pojo> getData() {
		return data;
	}
}
