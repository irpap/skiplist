package org.ljc.april2012;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import com.google.caliper.Param;
import com.google.caliper.SimpleBenchmark;

public class CaliperBenchmark extends SimpleBenchmark {
	@Param String benchmarkClassName;

	public static final String TEST_WORDS = "wordlist.txt";
	public static final String TEST_TEXT = "the-skull.txt";
	
	public static ArrayList<String> words = null;
	public static String text = null;
	
	private Benchmark benchmark = null;
	
	private Map<String, Integer> mapForAdd = null;
	
	private Map<String, Integer> mapForCount = null;
	
	public CaliperBenchmark() throws Exception {
	}
	
	@Override
	protected void setUp() throws Exception {
		if (null == words) {
			words = loadWords(TEST_WORDS);
			text = loadText(TEST_TEXT);
		}
		
		@SuppressWarnings("rawtypes")
		Class clazz = Class.forName(benchmarkClassName);
		benchmark  = (Benchmark) clazz.newInstance();
		
		mapForAdd =  benchmark.loadWords(words);
		
		mapForCount = benchmark.loadWords(words);
		benchmark.addWordCounts(mapForCount, text);
	}
	
	public int timeLoadWords(int reps) {
		int dummy = 0; // this is just to prevent contents of loop being optimized away
		for (int i=0; i<reps; ++i) {
			Map<String, Integer> map =  benchmark.loadWords(words);
			dummy += map.size();
		}
		return dummy;
	}
	
	public int timeAddWordCounts(int reps) {
		int dummy = 0; // this is just to prevent contents of loop being optimized away
		for (int i=0; i<reps; ++i) {
			benchmark.addWordCounts(mapForAdd, text);
			dummy += mapForAdd.size();
		}
		return dummy;
	}
	
	public int timeGetCounts(int reps) {
		int dummy = 0; // this is just to prevent contents of loop being optimized away
		for (int i=0; i<reps; ++i) {
			ArrayList<WordCount> counts = benchmark.getCounts(mapForCount);
			dummy += counts.size();
		}
		return dummy;
	}
		
	
	public static ArrayList<String> loadWords(String filename) throws Exception {
		String words = loadText(filename);
		String[] textWords = words.split("\\s+");
		
		ArrayList<String> result = new ArrayList<String>();
		for (String t : textWords) {
			result.add(t);
		}
		return result;
	}
	
	public static String loadText(String filename) throws Exception {
		InputStream in = CaliperBenchmark.class.getClassLoader().getResourceAsStream(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		StringBuilder text = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			text.append(line);
			text.append("\n");
		}
		return text.toString();
	}
}
