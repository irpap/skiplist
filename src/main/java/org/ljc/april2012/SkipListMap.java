package org.ljc.april2012;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SkipListMap<K, V> implements Map<K, V> {

	private final SkipList<SkipListMapEntry> skipList;

	public SkipListMap(final Comparator<K> comparator) {
		this.skipList = new SkipList<SkipListMapEntry>(
				makeEntryComparator(comparator));
	}

	static <K, V> Comparator<SkipListMapEntry> makeEntryComparator(
			final Comparator<K> keyComparator) {
		return new Comparator<SkipListMapEntry>() {
			@Override
			public int compare(SkipListMapEntry o1, SkipListMapEntry o2) {
				return keyComparator.compare((K) o1.getKey(), (K) o2.getKey());
			}

		};
	}

	@Override
	public final int size() {
		return skipList.size();
	}

	@Override
	public final boolean isEmpty() {
		return skipList.isEmpty();
	}

	@Override
	public final boolean containsKey(final Object key) {
		return skipList.contains(new SkipListMapEntry<K, V>((K) key));
	}

	@Override
	public final boolean containsValue(final Object value) {
		Iterator<SkipListMapEntry> skipListIterator = skipList.iterator();
		while (skipListIterator.hasNext()) {
			SkipListMapEntry next = skipListIterator.next();
			if (next.getValue().equals(value))
				return true;
		}
		return false;
	}

	@Override
	public final V get(final Object key) {
		SkipListMapEntry skipListMapEntry = skipList.find(new SkipListMapEntry<K, V>((K) key));
		return skipListMapEntry == null ? null : (V) skipListMapEntry.getValue();
	}

	/**
	 * Does not really return the previous value
	 */
	@Override
	public final V put(final K key, final V value) {
		skipList.add(new SkipListMapEntry<K, V>(key, value));
		return value;
	}

	@Override
	public final V remove(final Object key) {
		SkipListMapEntry<K, V> skipListMapEntry = skipList.find(new SkipListMapEntry<K, V>((K) key));
		V previousValue = skipListMapEntry == null ? null : skipListMapEntry.getValue();
		skipList.remove(new SkipListMapEntry<K, V>((K) key));
		return previousValue;
	}

	@Override
	public void putAll(Map m) {
	}

	@Override
	public void clear() {
		skipList.clear();
	}

	@Override
	public Set keySet() {
		return null;
	}

	@Override
	public Collection values() {
		return null;
	}

	@Override
	public Set entrySet() {
		return null;
	}

	public final Iterator<SkipListMapEntry> iterator() {
		return skipList.iterator();
	}
    public void uglyPrint() {skipList.uglyPrint();}
}