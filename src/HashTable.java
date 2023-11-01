import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class HashTable {

    private static final int INITIAL_CAPACITY = 100;
    private List<String>[] buckets;
    private int size;

    @SuppressWarnings("unchecked")
    public HashTable() {
        buckets = (List<String>[]) new List[INITIAL_CAPACITY];
    }

    public void add(String value) {
        if (contains(value)) return;
        int idx = hash(value);
        if (buckets[idx] == null) buckets[idx] = new ArrayList<>();
        buckets[idx].add(value);
        size++;

        if (size > buckets.length * 0.75) resize();
    }

    private int hash(String value) {
        return (value.hashCode() & 0x7fffffff) % buckets.length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newSize = buckets.length * 2;
        List<String>[] oldBuckets = buckets;
        buckets = (List<String>[]) new List[newSize];
        size = 0;

        for (List<String> bucket : oldBuckets) {
            if (bucket != null) {
                for (String value : bucket) {
                    add(value);
                }
            }
        }
    }

    public boolean contains(String value) {
        return getValue(value) != null;
    }

    public String getValue(String value) {
        int idx = hash(value);
        if (buckets[idx] != null) {
            for (String existingValue : buckets[idx]) {
                if (existingValue.equals(value)) {
                    return existingValue;
                }
            }
        }
        return null;
    }

    public int getPosition(String value) {
        int idx = hash(value);
        if (buckets[idx] != null)
            return idx;
        return -1;
    }

    public void print(PrintWriter out) {
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] != null) {
                out.print("Bucket " + i + ": ");
                for (String value : buckets[i]) {
                    out.print(value + " ");
                }
                out.println();
            }
        }
    }

}
