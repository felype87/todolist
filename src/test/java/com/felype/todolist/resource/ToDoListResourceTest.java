package com.felype.todolist.resource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.felype.todolist.exception.ItemNotFoundException;
import com.felype.todolist.mock.MockObjects;
import com.felype.todolist.model.Item;
import com.felype.todolist.model.List;
import com.felype.todolist.service.ToDoListService;

import reactor.core.publisher.Mono;

@RunWith(MockitoJUnitRunner.class)
public class ToDoListResourceTest {

	@Mock
	private ToDoListService toDoListService;

	@InjectMocks
	private ToDoListResource resource;

	private WebTestClient webTestClient;

	@Before
	public void setup() {
		webTestClient = WebTestClient.bindToController(resource)
				.controllerAdvice(new ResourceErrorHandler())
				.configureClient()
				.build();
	}

	@Test
	public void testPostItem() {
		Item addedItem = MockObjects.item(1L);
		when(toDoListService.addItem(any())).thenReturn(Mono.just(addedItem));

		webTestClient.post().uri("/items")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(MockObjects.item(null))
				.exchange()
				.expectStatus().isCreated()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody(Item.class).isEqualTo(addedItem);
	}

	@Test
	public void testPostInvalidItem() {
		webTestClient.post().uri("/items")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(MockObjects.invalidItem())
				.exchange()
				.expectStatus().isBadRequest();
	}

	@Test
	public void testPostItemUnexpectedException() {
		when(toDoListService.addItem(any())).thenThrow(RuntimeException.class);

		webTestClient.post().uri("/items")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(MockObjects.item(null))
				.exchange()
				.expectStatus().is5xxServerError();
	}

	@Test
	public void testGetItems() {
		List itemList = MockObjects.itemList();

		when(toDoListService.getItems()).thenReturn(Mono.just(itemList));

		webTestClient.get().uri("/items")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody(List.class).isEqualTo(itemList);
	}

	@Test
	public void testGetItemsUnexpectedException() {
		when(toDoListService.getItems()).thenThrow(RuntimeException.class);

		webTestClient.get().uri("/items")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().is5xxServerError();
	}

	@Test
	public void testGetItem() {
		Item item = MockObjects.item();

		when(toDoListService.getItem(any())).thenReturn(Mono.just(item));

		webTestClient.get().uri("/items/{item_id}", 1L)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody(Item.class).isEqualTo(item);
	}

	@Test
	public void testGetItemUnexpectedException() {
		when(toDoListService.getItem(any())).thenThrow(RuntimeException.class);

		webTestClient.get().uri("/items/{item_id}", 1L)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().is5xxServerError();
	}

	@Test
	public void testGetItemNotFound() {
		when(toDoListService.getItem(any())).thenReturn(Mono.error(new ItemNotFoundException("Item not found")));

		webTestClient.get().uri("/items/{item_id}", 1L)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	public void testDeleteItem() {
		when(toDoListService.deleteItem(any())).thenReturn(Mono.empty());

		webTestClient.delete().uri("/items/{item_id}", 1L)
				.exchange()
				.expectStatus().isNoContent();
	}

	@Test
	public void testDeleteItemNotFound() {
		when(toDoListService.deleteItem(any())).thenReturn(Mono.error(new ItemNotFoundException("Item not found")));

		webTestClient.delete().uri("/items/{item_id}", 1L)
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	public void testDeteleItemUnexpectedException() {
		when(toDoListService.deleteItem(any())).thenThrow(RuntimeException.class);

		webTestClient.delete().uri("/items/{item_id}", 1L)
				.exchange()
				.expectStatus().is5xxServerError();
	}

	@Test
	public void testPutItem() {
		Item item = MockObjects.item();

		when(toDoListService.updateItem(any())).thenReturn(Mono.just(item));

		webTestClient.put().uri("/items/{item_id}", item.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(item)
				.exchange()
				.expectStatus().isAccepted()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody(Item.class).isEqualTo(item);
	}

	@Test
	public void testPutItemInvalidRequest() {
		Item item = MockObjects.item();

		webTestClient.put().uri("/items/{item_id}", 2L)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(item)
				.exchange()
				.expectStatus().isBadRequest();
	}

	@Test
	public void testPutItemUnexpectedException() {
		when(toDoListService.updateItem(any())).thenThrow(RuntimeException.class);
		
		Item item = MockObjects.item();

		webTestClient.put().uri("/items/{item_id}", item.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(item)
				.exchange()
				.expectStatus().is5xxServerError();
	}

}
