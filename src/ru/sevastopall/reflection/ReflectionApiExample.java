package ru.sevastopall.reflection;

public class ReflectionApiExample {

    public static void main(String[] args) {
        User ivan = new User(25L, "Ivan");
        Class<? extends User> userClass = ivan.getClass();
        Class<User> userClass1 = User.class;
        System.out.println(userClass1 == userClass);

    }
}
