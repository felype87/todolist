package com.felype.todolist.resource;

import java.util.Objects;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.felype.todolist.exception.InvalidRequestException;
import com.felype.todolist.model.Item;
import com.felype.todolist.model.List;
import com.felype.todolist.service.ToDoListService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class ToDoListResource {

	private final ToDoListService toDoListService;

	
	@GetMapping(value = "/items")
	public Mono<List> getItems() {
		return toDoListService.getItems();
	}

	@GetMapping(value = "/items/{itemId}")
	public Mono<Item> getItem(@PathVariable("itemId") Long itemId) {
		return toDoListService.getItem(itemId);
	}

	@DeleteMapping(value = "/items/{itemId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteItem(@PathVariable("itemId") Long itemId) {
		return toDoListService.deleteItem(itemId);
	}

	@PostMapping(value = "/items")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Item> postItem(@RequestBody @Valid Item item) {
		return toDoListService.addItem(item);
	}
	
	@PutMapping(value = "/items/{itemId}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public Mono<Item> putItem(@RequestBody @Valid Item item, @PathVariable("itemId") Long itemId) {
		if(!Objects.equals(item.getId(), itemId)) {
			throw new InvalidRequestException("Item ID is invalid. Call post to create new item.");
		}
		
		return toDoListService.updateItem(item);
	}

}
