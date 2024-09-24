package com.library.util.utilitarian;

import java.lang.reflect.Field;

// TODO: Implementar um tratamento de erros melhor e verificar se os dois valores são nulos, caso seja e possa ser nulo ele ignora, caso não possa ele lança erros
public class UpdateObjectFields {

    public static <T> T updateField(T source, T target) throws IllegalAccessException {
        Field[] fields = source.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            try {
                Object sourceValue = field.get(source);
                if (sourceValue != null) {
                    field.set(target, sourceValue);
                }
            } catch (IllegalAccessException exception) {
                throw new IllegalArgumentException("Erro ao acessar campo: " + exception.getMessage());
            }
        }

        return target;
    }
}