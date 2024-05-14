package com.tns.hash;

import com.tns.linkedlist.LinkedList;
import com.tns.linkedlist.impl.SinglyLinkedList;

public class Dictionary<K, V> {
	
	private Entry<K, V>[] entries;
	private int size;
	private int capacity;
	
	public Dictionary(int capacity) {
		this.entries = (Entry<K, V>[])new Entry[capacity];
		this.capacity = capacity;
	}

	private class Entry<K, V> {
		
		private K key;
		private V value;
		// if there is similar hash key, the key and value will be store in chain
		private LinkedList<Entry<K, V>> chain; 
		
		private Entry(K key, V value) {
			this.key = key;
			this.value = value;
			this.chain = new SinglyLinkedList<Entry<K,V>>(); 
		}
	}
	
	public void register(K key, V value) {
		if(capacity <= 0) {
			return;
		}
		
		int hash = getHashCode(key);
		var entry = entries[hash];
		// room is empty
		if(entry == null) {
			entries[hash] = new Entry<>(key, value);
			++size;
		}
		// room is not empty
		else {
			// same key but may be different value or same value whatever we override the value
			if(entry.key.equals(key)) {
				entry.value = value;
				return;
			} 
			// different key with same hash code
			else {
				// chain size can be 0 so if 0 we just insert 
				if(entry.chain.size() == 0) {
					entry.chain.insertLast(new Entry<>(key, value));
					++size;
					return;
				}
				// chain size is not 0 so we need to find whether there is key that matches with key passed by parameter or not
				var i = entry.chain.iterator();
				while(i.hasNext()) {
					var oldEntry = i.next();
					// override value
					if(key.equals(oldEntry.key)) {
						oldEntry.value = value;
						return;
					}
					// key is new so just insert
					entry.chain.insertLast(new Entry<>(key, value));
					size++;
				}
			}
		}
	}
			
	
	public V search(K key) {
		int hash = getHashCode(key);
		var entry = entries[hash];
		if(entry == null) {
			return null;
		}
		else {
			if(entry.chain.size() == 0) {
				if(entry.key.equals(key)) return entry.value;
				else return null;
			}
			else {
				if(entry.key.equals(key)) {
					return entry.value;
				}
				var iterator = entry.chain.iterator();
				while(iterator.hasNext()) {
					var chainElement = iterator.next();
					if(chainElement.key.equals(key)) {
						return chainElement.value;
					}
				}
				return null;
			}
		}
	}
	
	public int getSize() {
		return this.size;
	}
	
	private int getHashCode(K key) {
		return Math.abs((key.hashCode()) % capacity);
	}
}
