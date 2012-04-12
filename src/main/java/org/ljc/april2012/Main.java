package org.ljc.april2012;

import java.util.Iterator;

public class Main {
	public static void main(String[] args) {
		SkipListMap<String, Integer> map = new SkipListMap<String, Integer>(
				String.CASE_INSENSITIVE_ORDER);
		map.put("b", 1);
        map.put("d", 1);
        map.put("f", 1);
        map.put("a", 1);
        map.put("c", 1);
        map.put("e", 1);

		map.put("b", 2);
        map.put("d", 2);
        map.put("e", 2);
        map.put("a", 2);
        map.put("c", 2);
        map.put("f", 2);

        map.put("d", 200);
        map.put("e", 200);
        map.put("a", 200);
        map.put("f", 200);
        map.put("b", 200);
//        map.put("c", 200);
        printAll(map);
        map.uglyPrint();


    }

    private static void printAll(SkipListMap<String, Integer> map) {
        Iterator<SkipListMapEntry> iterator = map.iterator();
        System.out.println("ITERATING");
        while (iterator.hasNext()) {
            SkipListMapEntry next = iterator.next();
            System.out.println(next.getKey() + ": " + next.getValue());
        }
    }
}
