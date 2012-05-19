import org.fest.assertions.Assertions;
import org.junit.Test;
import org.ljc.april2012.SkipList;

import java.util.Comparator;
import java.util.Iterator;

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
        Assertions.assertThat(next).isEqualTo(1);
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
        Assertions.assertThat(iterator.next()).isEqualTo(1);
        Assertions.assertThat(iterator.next()).isEqualTo(2);
        Assertions.assertThat(iterator.next()).isEqualTo(3);
        Assertions.assertThat(iterator.next()).isEqualTo(4);
        Assertions.assertThat(iterator.next()).isEqualTo(5);
        Assertions.assertThat(iterator.next()).isEqualTo(6);
    }
}
