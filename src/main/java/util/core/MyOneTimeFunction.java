package util.core;

public class MyOneTimeFunction<R, P> {

    private final MyInterfaces.Function1<P, R> firstTime;
    private final MyInterfaces.Function1<P, R> after;
    private MyInterfaces.Function1<P, R> current;

    public MyOneTimeFunction(MyInterfaces.Function1<P, R> firstTime, MyInterfaces.Function1<P, R> after){
        this.firstTime = firstTime;
        this.after = after;
        this.current = this::firstTime;
    }

    private R firstTime(P parameter){
        current = this::after;
        return firstTime.run(parameter);
    }

    private R after(P parameter) {
        return after.run(parameter);
    }

    public R run(P parameter){
        return current.run(parameter);
    }
}
