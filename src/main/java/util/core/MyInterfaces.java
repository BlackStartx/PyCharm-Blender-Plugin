package util.core;

public class MyInterfaces {

    public interface Action {
        void run();
    }

    public interface Action1<T> {
        void run(T parameter);
    }

    public interface Action2<T1, T2> {
        void run(T1 parameter1, T2 parameter2);
    }

    public interface Function<R> {
        R run();
    }

    public interface Function1<A1, R> {
        R run(A1 a);
    }

    public interface Function2<A1, A2, R> {
        R run(A1 a, A2 b);
    }
}
