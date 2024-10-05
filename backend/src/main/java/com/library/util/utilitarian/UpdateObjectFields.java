package com.library.util.utilitarian;

import com.library.util.errors.exceptions.EntityAttributeAccessException;
import org.eclipse.jetty.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.Objects;

public class UpdateObjectFields {

    // TODO: Implementar um tratamento de erros melhor e verificar se os dois valores são nulos, caso seja e possa ser nulo ele ignora, caso não possa ele lança erros
    /**
     * <h3>Método responsável por receber duas entidades e compara valores das duas, no caso é usada para realizar updates parciais na aplicação</h3>
     * <p>Busca os atributos da classe inserida para alteração e pega os valores privados dela</p>
     * <p>Percorre todos os valores da classe e verifica quais valores são nulos, após isso retorna a classe alterada</p>
     *
     * @param target -> <strong>Entidade que vem do banco de dados</strong>
     * @param source -> <strong>Entidade com valores parciais a serem alteradas</strong>
     * @throws IllegalAccessException -> <strong>Exception forçada pela classe Field, sendo tratada tanto aqui quanto no momento de usar por uma exception personalizada</strong>
     */
    public static void updatedObject(Object target, Object source) throws IllegalAccessException {
        System.out.println("target: " + target);
        System.out.println("source: " + source);

        if (target == null || source == null || target.getClass() != source.getClass()) {
            System.out.println("Uma das condições falhou:");
            if (target == null) {
                System.out.println("target é nulo.");
            }
            if (source == null) {
                System.out.println("source é nulo.");
            }
            if (target.getClass() != source.getClass()) {
                System.out.println("target e source têm classes diferentes.");
                System.out.println("Classe do target: " + target.getClass().getName());
                System.out.println("Classe do source: " + source.getClass().getName());
            }
            return;
        }

        System.out.println("!ofjeiopsdjnpfioj" + target);
        System.out.println("dsmpfnm" + source);

        Class<?> clazz = target.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object valueSource = field.get(source);

                if (valueSource != null) {
                    field.set(target, valueSource);
                }
            } catch (IllegalAccessException exception) {
                throw new EntityAttributeAccessException(
                        String.format("Erro ao acessar valor do objeto: %s", exception.getMessage()),
                        HttpStatus.INTERNAL_SERVER_ERROR_500
                );
            }
        }
    }

    public static boolean compareObject(Object target, Object source) {
        if (target == null || source == null || target.getClass() != source.getClass()) {
            return false;
        }

        Class<?> clazz = target.getClass();
        Field[] fields = clazz.getDeclaredFields();
        boolean isEqual = true;

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object valueTarget = field.get(target);
                Object valueSource = field.get(source);

                // Se um valor for nulo no source, não atualizar o target com null
                if (valueSource != null) {
                    if (!Objects.equals(valueTarget, valueSource)) {
                        field.set(target, valueSource); // Atualiza o target com o valor do source
                        isEqual = false;
                    }
                }
            } catch (IllegalAccessException exception) {
                throw new EntityAttributeAccessException(String.format("Erro ao acessar valor do objeto: %s", exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR_500);
            }
        }

        return isEqual;
    }
}