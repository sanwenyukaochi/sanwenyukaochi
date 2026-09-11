package creationalDesignPatterns.singleton;

public class DemoMultiThread {
    public static void main(String[] args) {
        IO.println(
                """
                If you see the same value, then singleton was reused (woohoo!)
                If you see different values, then 2 singletons were created (ugh!!)

                RESULT:
                """);
        Thread threadFoo = new Thread(new ThreadFoo());
        Thread threadBar = new Thread(new ThreadBar());
        threadFoo.start();
        threadBar.start();
    }

    static class ThreadFoo implements Runnable {
        @Override
        public void run() {
            Singleton singleton = Singleton.getInstance("FOO");
            IO.println(singleton.value);
        }
    }

    static class ThreadBar implements Runnable {
        @Override
        public void run() {
            Singleton singleton = Singleton.getInstance("BAR");
            IO.println(singleton.value);
        }
    }
}
