import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class LRUCache<K, V> {

    private class Node {
        K key;
        V value;
        long expiresAt; // -1 = no expiry
        Node prev, next;

        Node(K key, V value, long ttlMillis) {
            this.key = key;
            this.value = value;
            this.expiresAt = ttlMillis > 0
                    ? System.currentTimeMillis() + ttlMillis
                    : -1;
        }

        boolean expired() {
            return expiresAt != -1 && System.currentTimeMillis() > expiresAt;
        }
    }

    private final int capacity;
    private final Map<K, Node> map;
    private final Node head, tail;
    private final boolean threadSafe;
    private final ReentrantLock lock;

    public LRUCache(int capacity) {
        this(capacity, false);
    }

    public LRUCache(int capacity, boolean threadSafe) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.threadSafe = threadSafe;
        this.lock = threadSafe ? new ReentrantLock() : null;

        head = new Node(null, null, -1);
        tail = new Node(null, null, -1);
        head.next = tail;
        tail.prev = head;
    }

    public V get(K key) {
        if (threadSafe) lock.lock();
        try {
            Node node = map.get(key);
            if (node == null) return null;

            if (node.expired()) {
                remove(node);
                map.remove(key);
                return null;
            }

            moveToFront(node);
            return node.value;
        } finally {
            if (threadSafe) lock.unlock();
        }
    }

    public void put(K key, V value) {
        put(key, value, -1);
    }

    public void put(K key, V value, long ttlMillis) {
        if (threadSafe) lock.lock();
        try {
            Node node = map.get(key);

            if (node != null) {
                node.value = value;
                node.expiresAt = ttlMillis > 0
                        ? System.currentTimeMillis() + ttlMillis
                        : -1;
                moveToFront(node);
                return;
            }

            if (map.size() >= capacity) {
                Node lru = tail.prev;
                remove(lru);
                map.remove(lru.key);
            }

            Node newNode = new Node(key, value, ttlMillis);
            addToFront(newNode);
            map.put(key, newNode);

        } finally {
            if (threadSafe) lock.unlock();
        }
    }

    public int size() {
        return map.size();
    }

    // --------------------------
    // Doubly Linked List Ops
    // --------------------------

    private void addToFront(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    private void remove(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void moveToFront(Node node) {
        remove(node);
        addToFront(node);
    }
}
