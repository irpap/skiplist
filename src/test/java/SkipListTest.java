import org.junit.Test;
import org.ljc.april2012.SkipList;

import java.util.Comparator;
import java.util.Iterator;

import static org.fest.assertions.Assertions.assertThat;

public class SkipListTest {
    @Test
    public void canInsertAndRetrieveOneElement() {
        SkipList<Integer> skipList = new SkipList<Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        skipList.add(1);
        Iterator<Integer> iterator = skipList.iterator();
        Integer next = iterator.next();
        assertThat(next).isEqualTo(1);
    }

    @Test
    public void canInsertAndRetrieveManyElements() {
        SkipList<Integer> skipList = new SkipList<Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        skipList.add(1);
        skipList.add(5);
        skipList.add(2);
        skipList.add(4);
        skipList.add(6);
        skipList.add(3);
        skipList.uglyPrint();
        Iterator<Integer> iterator = skipList.iterator();
        assertThat(iterator.next()).isEqualTo(1);
        assertThat(iterator.next()).isEqualTo(2);
        assertThat(iterator.next()).isEqualTo(3);
        assertThat(iterator.next()).isEqualTo(4);
        assertThat(iterator.next()).isEqualTo(5);
        assertThat(iterator.next()).isEqualTo(6);
    }

    @Test
    public void searchList() {
        SkipList<Integer> skipList = new SkipList<Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        skipList.add(1);
        skipList.add(5);
        skipList.add(2);
        skipList.add(4);
        skipList.add(6);
        skipList.add(3);
        assertThat(skipList.contains(3)).isEqualTo(true);
        assertThat(skipList.contains(1)).isEqualTo(true);
        assertThat(skipList.contains(2)).isEqualTo(true);
        assertThat(skipList.contains(5)).isEqualTo(true);
        assertThat(skipList.contains(6)).isEqualTo(true);
        assertThat(skipList.contains(4)).isEqualTo(true);
        assertThat(skipList.contains(22)).isEqualTo(false);
        assertThat(skipList.contains(8)).isEqualTo(false);
    }

    @Test
    public void removeElement() {
        SkipList<Integer> skipList = new SkipList<Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        skipList.add(1);
        skipList.add(5);
        skipList.add(2);
        skipList.add(4);
        skipList.add(6);
        skipList.add(3);
        assertThat(skipList.contains(3)).isEqualTo(true);
        assertThat(skipList.remove(3)).isEqualTo(true);
        assertThat(skipList.contains(3)).isEqualTo(false);

        assertThat(skipList.contains(1)).isEqualTo(true);
        assertThat(skipList.remove(1)).isEqualTo(true);
        assertThat(skipList.contains(1)).isEqualTo(false);
    }
}
