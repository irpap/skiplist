package org.ljc.april2012;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class SkipListMapTest {
    @Test
    public void putSomethingInTheMapAndReadIt() {
        SkipListMap<String, Integer> map = new SkipListMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);
        map.put("count", 5);
        assertThat(map.get("count")).isEqualTo(5);

    }

    @Test
    public void putSomethingInTheMapModifyItAndReadIt() {
        SkipListMap<String, Integer> map = new SkipListMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);
        assertThat( map.put("count", 5)).isEqualTo(null);
        assertThat(map.get("count")).isEqualTo(5);

        assertThat( map.put("count", 6)).isEqualTo(5);
        assertThat(map.get("count")).isEqualTo(6);

        assertThat( map.put("count", 7)).isEqualTo(6);
        assertThat(map.get("count")).isEqualTo(7);

        assertThat( map.put("count", 8)).isEqualTo(7);
        assertThat(map.get("count")).isEqualTo(8);
    }
}
