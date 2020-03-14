package com.felype.todolist.service;

import java.util.ArrayList;
import java.util.function.Function;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.felype.todolist.exception.BackendServiceException;
import com.felype.todolist.exception.ItemNotFoundException;
import com.felype.todolist.model.Item;
import com.felype.todolist.model.List;
import com.felype.todolist.repository.ItemRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ToDoListService {

	private final ItemRepository itemRepository;

	public Mono<Item> addItem(Item item) {
		return Mono.fromCallable(() -> {
			Item savedItem = itemRepository.save(item);

			log.info("Item added. ID: {}", savedItem.getId());

			return savedItem;
		}).onErrorMap(handleErrors());
	}
	
	public Mono<Item> updateItem(Item item) {
		return Mono.fromCallable(() -> {
			Item savedItem = itemRepository.save(item);

			log.info("Item saved. ID: {}", savedItem.getId());

			return savedItem;
		}).onErrorMap(handleErrors());
	}

	public Mono<List> getItems() {
		return Mono.fromCallable(() -> {
			java.util.List<Item> result = new ArrayList<>();

			itemRepository.findAll().forEach(result::add);

			return List.builder().items(result).build();
		}).onErrorMap(handleErrors());
	}

	public Mono<Item> getItem(Long itemId) {
		return Mono
				.fromCallable(() -> itemRepository.findById(itemId)
						.orElseThrow(() -> new ItemNotFoundException(String.format("Item not found. ID: %d", itemId))))
				.onErrorMap(handleErrors());
	}

	public Mono<Void> deleteItem(Long itemId) {
		return Mono.<Void>fromCallable(() -> {
			itemRepository.deleteById(itemId);

			log.info("Item deleted. ID: {}", itemId);

			return null;
		}).onErrorMap(handleErrors(itemId));
	}

	private Function<Throwable, Throwable> handleErrors(Object... args) {
		return throwable -> {
			if (ItemNotFoundException.class.isInstance(throwable)) {
				return throwable;
			} else if (EmptyResultDataAccessException.class.isInstance(throwable)) {
				return new ItemNotFoundException(String.format("Item not found. ID: %d", args[0]));
			} else {
				return new BackendServiceException(throwable);
			}
		};
	}

}
