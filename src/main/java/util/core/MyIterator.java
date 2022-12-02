package util.core;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class MyIterator<T> implements Iterable<T> {

    private final Iterable<T> current;

    public MyIterator(Iterable<T> iterable) {
        current = iterable;
    }

    public MyIterator(T[] iterable) {
        current = Arrays.asList(iterable);
    }

    public MyIterator<T> where(Predicate<T> iterateFunc) {
        ArrayList<T> toRet = new ArrayList<>();
        for (T element : current) {
            if (iterateFunc.run(element)) toRet.add(element);
        }
        return new MyIterator<>(toRet);
    }

    public ArrayList<T> toArrayList() {
        ArrayList<T> list = new ArrayList<>();
        for (T element : current) list.add(element);
        return list;
    }

    /*
     *  Class
     */

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return current.iterator();
    }

    public interface Predicate<T> {
        Boolean run(T a);
    }
}
