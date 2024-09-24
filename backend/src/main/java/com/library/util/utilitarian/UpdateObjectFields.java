package com.library.util.utilitarian;

import java.lang.reflect.Field;

public class UpdateObjectFields {

    // TODO: Implementar um tratamento de erros melhor e verificar se os dois valores são nulos, caso seja e possa ser nulo ele ignora, caso não possa ele lança erros

    /**
     * <h3>Método responsável por receber duas entidades e compara valores das duas, no caso é usada para realizar updates parciais na aplicação</h3>
     * <p>Busca os atributos da classe inserida para alteração e pega os valores privados dela</p>
     * <p>Percorre todos os valores da classe e verifica quais valores são nulos, após isso retorna a classe alterada</p>
     *
     * @param source -> <strong>Entidade que vem do banco de dados</strong>
     * @param target -> <strong>Entidade com valores parciais a serem alteradas</strong>
     * @param <T>    -> <strong>Valor genérico para poder usar qualquer entidade</strong>
     * @return T -><strong>Retorna a entidade com os valores alterados</strong>
     * @throws IllegalAccessException -> <strong>Exception forçada pela classe Field, sendo tratada tanto aqui quanto no momento de usar por uma exception personalizada</strong>
     */
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