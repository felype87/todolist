package com.felype.todolist.mock;

import java.util.Arrays;

import com.felype.todolist.model.Item;
import com.felype.todolist.model.List;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MockObjects {

	public Item item(Long id) {
		return Item.builder().id(id).title("title").build();
	}

	public Item item() {
		return item(1L);
	}

	public Item invalidItem() {
		return Item.builder().title("").build();
	}

	public List itemList() {
		return List.builder().items(Arrays.asList(item(1L), item(2L), item(3L))).build();
	}

}
