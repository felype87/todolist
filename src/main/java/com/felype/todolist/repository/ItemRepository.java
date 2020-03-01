package com.felype.todolist.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.felype.todolist.model.Item;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {

}
