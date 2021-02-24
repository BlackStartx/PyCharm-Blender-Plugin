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

    private void indexIterate(MyInterfaces.Action2<Integer, T> iterator) {
        int i = 0;
        for (T element : current) {
            iterator.run(i, element);
            i++;
        }
    }

    public boolean any(MyInterfaces.Function1<T, Boolean> iterateFunc) {
        for (T element : current) if (iterateFunc.run(element)) return true;
        return false;
    }

    public MyIterator<T> where(MyInterfaces.Function1<T, Boolean> iterateFunc) {
        ArrayList<T> toRet = new ArrayList<>();
        indexIterate((index, element) -> {
            if (iterateFunc.run(element)) toRet.add(element);
        });
        return new MyIterator<>(toRet);
    }

    public MyIterator<T> where(MyInterfaces.Function2<T, Integer, Boolean> iterateFunc) {
        ArrayList<T> toRet = new ArrayList<>();
        indexIterate((index, element) -> {
            if (iterateFunc.run(element, index)) toRet.add(element);
        });
        return new MyIterator<>(toRet);
    }

    public <R> MyIterator<R> select(MyInterfaces.Function1<T, R> forEachInterface) {
        ArrayList<R> toRet = new ArrayList<>();
        for (T t : current) toRet.add(forEachInterface.run(t));
        return new MyIterator<>(toRet);
    }

    public <R> MyIterator<R> select(MyInterfaces.Function2<Integer, T, R> iterateFunc) {
        ArrayList<R> toRet = new ArrayList<>();
        indexIterate((index, element) -> toRet.add(iterateFunc.run(index, element)));
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
}
