package ch3;

import org.junit.Assert;
import org.junit.Test;

import java.nio.file.DirectoryStream;
import java.util.*;
import java.util.function.BiFunction;

import static org.junit.Assert.assertEquals;

public class CollectionsTest {

    @Test
    public void binarySearch() {
        Integer[] numberArr = {2, 10, 4, -22, 20};
        List<Integer> list = Arrays.asList(numberArr);
        assertEquals(Collections.binarySearch(list, -22), -1);
        assertEquals(Arrays.binarySearch(new Integer[]{100, 45, 12, 4, -22}, -22, (n1, n2) -> n2 - n1), 4);
        Collections.sort(list);
        assertEquals(Collections.binarySearch(list, -22), 0);
    }

    @Test
    public void merge() {
        HashMap<Integer, String> map = new HashMap<>();
        map.put(1, "Unp");
        map.put(13, "trece");
        map.put(10, "Ten");
        BiFunction<String, String, String> merger = (v, vn) -> {
            return v.compareTo(vn) > -1 ? v : vn;
        };
        map.merge(1, "Zeta", merger);
        map.merge(13, "100", merger);
        System.out.println(map);
    }

    public static class MyComparator implements Comparator<String> {
        public int compare(String a, String b) {
            return a.toLowerCase().compareTo(b.toLowerCase());
        }
    }

    public static void main(String[] args) {
        String[] values = {"123", "Abb", "aab"};
        Arrays.sort(values, new MyComparator());
        for (String s : values)
            System.out.print(s + " ");

        Sorted s1 = new Sorted(88, "a");

        TreeSet<Sorted> t2 = new TreeSet<>(s1);
        DirectoryStream.Filter<Collection<?>> collectionFilter = new ArrayList<>()::removeAll;
        new ArrayList<>().removeIf((o) -> o.getClass() == null);
    }

    public static class Sorted implements Comparable<Sorted>, Comparator<Sorted> {
        private int num;
        private String text;

        Sorted(int n, String t) {
            this.num = n;
            this.text = t;
        }

        public String toString() {
            return "" + num;
        }

        public int compareTo(Sorted s) {
            return text.compareTo(s.text);
        }

        public int compare(Sorted s1, Sorted s2) {
            return s1.num - s2.num;
        }

    }


}