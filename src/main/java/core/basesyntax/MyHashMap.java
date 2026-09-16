package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    @SuppressWarnings("unchecked")
    private Node<K,V>[] table = (Node<K, V>[])new Node[DEFAULT_INITIAL_CAPACITY];
    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private int size = 0;

    @Override
    public void put(K key, V value) {
        if (capacityNeedsToBeChanged()) {
            resize();
        }
        putToTable(key, value);
    }

    @Override
    public V getValue(K key) {
        int position = hash(key);

        if (table[position] == null) {
            return null;
        }

        Node<K,V> node = table[position];

        while (node != null) {
            if (key == node.key || (node.key != null && node.key.equals(key))) {
                return node.value;
            }
            node = node.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putToTable(K key, V value) {
        int position = hash(key);

        if (table[position] == null) {
            table[position] = new Node<>(key, value);
            size++;
            return;
        }
        Node<K,V> node = table[position];

        while (true) {
            if (key == node.key || (node.key != null && node.key.equals(key))) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value);
                size++;
                return;
            }
            node = node.next;
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldTable = table;
        capacity *= 2;
        table = (Node<K, V>[])new Node[capacity];

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                Node<K, V> next = node.next;
                int position = hash(node.key);

                node.next = table[position];
                table[position] = node;
                node = next;
            }
        }
    }

    private boolean capacityNeedsToBeChanged() {
        return capacity * DEFAULT_LOAD_FACTOR == size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.floorMod(key.hashCode(), capacity);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
            hash = key == null ? 0 : key.hashCode();
        }
    }
}
