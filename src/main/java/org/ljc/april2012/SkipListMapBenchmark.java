package org.ljc.april2012;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SkipListMapBenchmark implements Benchmark {

	public SkipListMapBenchmark() {
	}

	@Override
	public Map<String, Integer> loadWords(ArrayList<String> words) {
		SkipListMap<String, Integer> map = new SkipListMap<String, Integer>(
				String.CASE_INSENSITIVE_ORDER);
		List<String> sublist = words.subList(0, 1000);
		for (String w : sublist) {
			map.put(w, new Integer(0));
		}
		return map;
	}

	@Override
	public void addWordCounts(Map<String, Integer> words, String text) {
		String[] textWords = text.split("\\W+");
		for (String t : textWords) {
			Integer count = words.get(t);
			if (null != count) {
				words.put(t, new Integer(count.intValue() + 1));
			}
		}
	}

	@Override
	public ArrayList<WordCount> getCounts(Map<String, Integer> words) {
		ArrayList<WordCount> results = new ArrayList<WordCount>();
		Iterator<SkipListMapEntry> iterator = ((SkipListMap<String, Integer>) words)
				.iterator();
		while (iterator.hasNext()) {
			SkipListMapEntry<String, Integer> next = iterator.next();
			results.add(new WordCount(next.getKey(), next.getValue().intValue()));

		}
		return results;
	}

}
