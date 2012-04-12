package org.ljc.april2012;

import java.util.ArrayList;
import java.util.Map;

public interface Benchmark {

	Map<String,Integer> loadWords(ArrayList<String> words);
	
	void addWordCounts(Map<String, Integer> words, String text);
	
	ArrayList<WordCount> getCounts(Map<String, Integer> words);
}
