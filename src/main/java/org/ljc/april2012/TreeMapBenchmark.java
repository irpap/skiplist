package org.ljc.april2012;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class TreeMapBenchmark implements Benchmark {
	
	public TreeMapBenchmark() {	
	}
	
	@Override
	public Map<String, Integer> loadWords(ArrayList<String> words) {
		TreeMap<String, Integer> map = new TreeMap<String, Integer>();
		for (String w : words) {
            System.err.println(w);

			map.put(w, 0);
		}
		return map;
	}

	@Override
	public void addWordCounts(Map<String, Integer> words, String text) {
		String[] textWords = text.split("\\W+");
		for (String t : textWords) {
			Integer count = words.get(t);
			if (null != count) {
				words.put(t, count.intValue() + 1);
			}
		}
	}

	@Override
	public ArrayList<WordCount> getCounts(Map<String, Integer> words) {
		ArrayList<WordCount> results = new ArrayList<WordCount>();
		for (Map.Entry<String, Integer> m : words.entrySet()) {
			results.add(new WordCount(m.getKey(), m.getValue().intValue()));
		}
		return results;
	}

}
