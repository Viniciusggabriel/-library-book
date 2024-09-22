package com.library.util.utilitarian;

import com.library.util.errors.exceptions.EntityReferenceIllegal;

import java.lang.reflect.Field;

// TODO: Implementar um tratamento de erros melhor e verificar se os dois valores são nulos, caso seja e possa ser nulo ele ignora, caso não possa ele lança erros
public class UpdateObjectFields {
    public static void updateField(Object source, Object target) throws IllegalAccessException {
        Field[] fields = source.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            try {
                Object sourceValue = field.get(source);
                Object targetValue = field.get(target);

                if (sourceValue != null)
                    field.set(target, sourceValue);
            } catch (IllegalAccessException exception) {
                throw new EntityReferenceIllegal("O objeto inserido para alteração contem valores errados: " + exception.getMessage());
            }
        }
    }
}