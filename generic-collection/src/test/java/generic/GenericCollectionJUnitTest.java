package generic;

import com.ryuukun.colllection.ArrayGenericCollection;
import com.ryuukun.colllection.GenericCollection;
import com.ryuukun.colllection.LinkedGenericCollection;
import org.junit.Before;
import org.junit.Test;

public class GenericCollectionJUnitTest {
    private final int LENGTH = 10000;

    private GenericCollection<Integer> coll;

    @Before
    public void init() {
        coll = new ArrayGenericCollection<>();
    }

    @Test
    public void addAndRemoveTest() {
        for (int i = 0; i < LENGTH; ++i) {
            coll.add((int) (Math.random() * 1000));
        }

        for (int i = 0; i < LENGTH; ++i) {
            coll.remove((int) (Math.random() * (coll.size())));
        }
    }

    @Test
    public void printAllTest() {
        for (int i = 0; i < LENGTH; ++i) {
            coll.add((int) (Math.random() * 1000));
        }

        coll.printAll();
    }
}
