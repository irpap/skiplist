package org.ljc.april2012;

public class SkipListMapEntry<K, V> {
    private K key;
    private V value;

    public SkipListMapEntry(K key) {
        this.key = key;
    }

    public SkipListMapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public V getValue() {
        return value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public K getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SkipListMapEntry<K, V> that = (SkipListMapEntry<K, V>) o;

        if (key != null ? !key.equals(that.key) : that.key != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    public void setValue(V value) {
        this.value = value;
    }
    public String toString() {
        return key + ": " + value;
    }
}
