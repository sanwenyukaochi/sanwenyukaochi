package creational_design_patterns.singleton.example.non_thread_safe;

public class DemoSingleThread {
    public static void main(String[] args) {
        IO.println(
                """
                If you see the same value, then singleton was reused (woohoo!)
                If you see different values, then 2 singletons were created (ugh!!)

                RESULT:
                """);
        Singleton singleton = Singleton.getInstance("FOO");
        Singleton anotherSingleton = Singleton.getInstance("BAR");
        IO.println(singleton.value);
        IO.println(anotherSingleton.value);
    }
}
