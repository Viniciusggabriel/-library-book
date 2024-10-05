package com.library.util.utilitarian;

import com.library.util.errors.exceptions.EntityAttributeAccessException;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Objects;

@RequiredArgsConstructor
public class UpdateObjectFields {
    private static final Logger logger = LoggerFactory.getLogger(UpdateObjectFields.class);

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
        logger.info("target:\n {}", target);
        logger.info("source:\n {}", source);

        if (target == null || source == null || target.getClass() != source.getClass()) {
            logger.error("Uma das condições falhou:");
            if (target == null) {
                logger.error("target é nulo.");
            }
            if (source == null) {
                logger.error("source é nulo.");
            }
            if (target.getClass() != source.getClass()) {
                logger.error("target e source têm classes diferentes.");
                logger.error("Classe do target: {}", target.getClass().getName());
                logger.error("Classe do source: {}", source.getClass().getName());
            }
            return;
        }

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

    /**
     * <h3>Método para compara dois objetos e verificar se são iguais</h3>
     * <p>O método verifica primeiramente os parâmetros são nulos</p>
     * <p>Verifica os Fild dos objetos passados como parâmetro</p>
     *
     * @param target -> <strong>Entidade que vem do banco de dados</strong>
     * @param source -> <strong>Entidade com valores parciais a serem alteradas</strong>
     * @return boolean -> <strong>Retorna o status da compração</strong>
     */
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

                if (valueSource != null) {
                    if (!Objects.equals(valueTarget, valueSource)) {
                        field.set(target, valueSource);
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