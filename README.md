# LRU Cache (Java)

A compact and production-style LRU cache implementation built
with a HashMap + Doubly Linked List for true O(1) get and put.

Includes optional thread safety and TTL support.

---

## Highlights

- O(1) get
- O(1) put
- Automatic eviction (Least Recently Used)
- Optional TTL expiration
- Optional thread-safe mode (ReentrantLock)
- Simple benchmark included

---

## Design

Core components:

- HashMap<K, Node>
- Custom doubly linked list
- Sentinel head & tail nodes
- Node-level expiration timestamp

Eviction occurs when capacity is exceeded.
Expired entries are removed lazily on access.

---

## Compile & Run

Compile:

    javac LRUCache.java LRUBenchmark.java

Run benchmark:

    java LRUBenchmark

---

## Usage Example

```java
LRUCache<String, String> cache = new LRUCache<>(3, true);

cache.put("a", "1");
cache.put("b", "2");
cache.put("c", "3");

cache.get("a");
cache.put("d", "4"); // evicts least recently used
