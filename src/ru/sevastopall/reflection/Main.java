package ru.sevastopall.reflection;

import ru.sevastopall.reflection.cars.Car;
import ru.sevastopall.reflection.cars.Column;
import ru.sevastopall.reflection.cars.Table;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Car car = new Car("Toyota", "Corolla");
        System.out.println(car);
        System.out.println(generateInsert(car));
    }

    private static String generateInsert(Car car) {
        String template = "INSERT INTO %s, %s (%s) VALUES (%s);";
        Table table = car.getClass().getAnnotation(Table.class);
        Field[] declaredFields = car.getClass().getDeclaredFields();

        String fieldsName = Arrays.stream(declaredFields)
                .filter(field -> field.isAnnotationPresent(Column.class))
                .map(field -> field.getAnnotation(Column.class))
                .map(Column::value)
                .collect(Collectors.joining(", "));

        String fieldValues = Arrays.stream(declaredFields)
                .filter(field -> field.isAnnotationPresent(Column.class))
                .peek(field -> field.setAccessible(true))
                .map(field -> {
                    try {
                        return String.valueOf(field.get(car));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return "";
                    }
                })
                .map(value -> "'" + value + "'")
                .collect(Collectors.joining(", "));

        return String.format(template, table.schema(), table
                .value(), fieldsName, fieldValues);
    }
}
