package com.ewolff.microservice.order.item;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class ItemTestDataGenerator {

	private final ItemRepository itemRepository;

	public ItemTestDataGenerator(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@PostConstruct
	public void generateTestData() {
		itemRepository.save(new Item("iPod", 42.0));
		itemRepository.save(new Item("iPod touch", 21.0));
		itemRepository.save(new Item("iPod nano", 1.0));
		itemRepository.save(new Item("Apple TV", 100.0));
	}

}
