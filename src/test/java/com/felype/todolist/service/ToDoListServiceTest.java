package com.felype.todolist.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;

import com.felype.todolist.exception.BackendServiceException;
import com.felype.todolist.exception.ItemNotFoundException;
import com.felype.todolist.mock.MockObjects;
import com.felype.todolist.model.Item;
import com.felype.todolist.model.List;
import com.felype.todolist.repository.ItemRepository;

import reactor.test.StepVerifier;

@RunWith(MockitoJUnitRunner.class)
public class ToDoListServiceTest {

	@InjectMocks
	private ToDoListService toDoListService;

	@Mock
	private ItemRepository itemRepository;

	@Test
	public void testAddItem() {
		Item item = MockObjects.item();

		when(itemRepository.save(any())).thenReturn(item);

		StepVerifier.create(toDoListService.addItem(item)).expectNext(item).verifyComplete();
	}

	@Test
	public void testAddInvalidItemTitleEmpty() {
		when(itemRepository.save(any())).thenThrow(new ConstraintViolationException(null));

		StepVerifier.create(toDoListService.addItem(MockObjects.invalidItem()))
				.expectError(BackendServiceException.class).verify();
	}

	@Test
	public void testAddItemUnexpectedError() {
		when(itemRepository.save(any())).thenThrow(RuntimeException.class);

		StepVerifier.create(toDoListService.addItem(MockObjects.item())).expectError(BackendServiceException.class)
				.verify();
	}

	@Test
	public void testGetItems() {
		List items = MockObjects.itemList();

		when(itemRepository.findAll()).thenReturn(items.getItems());

		StepVerifier.create(toDoListService.getItems()).expectNext(items).verifyComplete();
	}

	@Test
	public void testGetItemsUnexpectedError() {
		StepVerifier.create(toDoListService.addItem(MockObjects.item())).expectError(BackendServiceException.class)
				.verify();
	}

	@Test
	public void testGetItem() {
		Item item = MockObjects.item();

		when(itemRepository.findById(any())).thenReturn(Optional.of(item));

		StepVerifier.create(toDoListService.getItem(item.getId())).expectNext(item).verifyComplete();
	}

	@Test
	public void testGetItemNotFound() {
		when(itemRepository.findById(any())).thenThrow(ItemNotFoundException.class);

		StepVerifier.create(toDoListService.getItem(5L)).expectError(ItemNotFoundException.class).verify();
	}

	@Test
	public void testGetItemUnexpectedError() {
		when(itemRepository.findById(any())).thenThrow(RuntimeException.class);

		StepVerifier.create(toDoListService.getItem(5L)).expectError(BackendServiceException.class).verify();
	}

	@Test
	public void testDeleteItem() {
		StepVerifier.create(toDoListService.deleteItem(1L)).verifyComplete();

		verify(itemRepository, times(1)).deleteById(eq(1L));
	}

	@Test
	public void testDeleteItemNotFound() {
		Mockito.doThrow(EmptyResultDataAccessException.class).when(itemRepository).deleteById(any());

		StepVerifier.create(toDoListService.deleteItem(1L)).expectError(ItemNotFoundException.class).verify();
	}

	@Test
	public void testDeleteItemUnexpectedError() {
		Mockito.doThrow(RuntimeException.class).when(itemRepository).deleteById(any());

		StepVerifier.create(toDoListService.deleteItem(1L)).expectError(BackendServiceException.class).verify();
	}
	
	@Test
	public void testUpdateItem() {
		Item item = MockObjects.item();

		when(itemRepository.save(any())).thenReturn(item);

		StepVerifier.create(toDoListService.updateItem(item)).expectNext(item).verifyComplete();
	}

	@Test
	public void testUpdateInvalidItemTitleEmpty() {
		when(itemRepository.save(any())).thenThrow(new ConstraintViolationException(null));

		StepVerifier.create(toDoListService.updateItem(MockObjects.invalidItem()))
				.expectError(BackendServiceException.class).verify();
	}

	@Test
	public void testUpdateItemUnexpectedError() {
		when(itemRepository.save(any())).thenThrow(RuntimeException.class);

		StepVerifier.create(toDoListService.updateItem(MockObjects.item())).expectError(BackendServiceException.class)
				.verify();
	}

}
