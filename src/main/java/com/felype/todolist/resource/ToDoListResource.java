package com.felype.todolist.resource;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.felype.todolist.model.Item;
import com.felype.todolist.model.List;
import com.felype.todolist.service.ToDoListService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ToDoListResource {

	private final ToDoListService toDoListService;

	@GetMapping(value = "/items")
	public Mono<List> getItems() {
		return toDoListService.getItems();
	}

	@GetMapping(value = "/items/{item_id}")
	public Mono<Item> getItem(@PathVariable("item_id") Long itemId) {
		return toDoListService.getItem(itemId);
	}

	@DeleteMapping(value = "/items/{item_id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteItem(@PathVariable("item_id") Long itemId) {
		return toDoListService.deleteItem(itemId);
	}

	@PostMapping(value = "/items")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Item> postItem(@RequestBody @Valid Item item) {
		return toDoListService.addItem(item);
	}

}
