package com.felype.todolist.model;

import java.util.ArrayList;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class List {

	@NotNull
	private java.util.List<Item> items;

	public synchronized java.util.List<Item> getItems() {
		if (items == null) {
			items = new ArrayList<>();
		}

		return items;
	}

}
