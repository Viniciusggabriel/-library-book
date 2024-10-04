package com.library.infra.database.repositories.finders;

import io.ebean.Finder;
import io.ebean.PagedList;

import java.util.Arrays;

import static com.library.util.utilitarian.UpdateObjectFields.updateField;

public class EntityFinder<ID, T> extends Finder<ID, T> {
    public EntityFinder(Class<T> type) {
        super(type);
    }

    /**
     * <h3>Método da classe Finder para poder fazer um select paginado </h3>
     * <p>Realiza o select dentro do banco de dados de forma organizada pelo atributo passado, como exemplo o nome do entidade</p>
     *
     * @param attributeBook -> <strong>Atributo da classe de entidade a ser ordenada</strong>
     * @param size          -> <strong>Tamanho de linhas a ser requisitados</strong>
     * @return PagedList<T> -> <strong>Retorna um SQL paginado</strong>
     */
    public PagedList<T> findAll(String attributeBook, Integer size) {
        return query().where().orderBy().asc(attributeBook).setMaxRows(size).findPagedList();
    }

    /**
     * <h3>Método par abuscar um entidade baseado no nome</h3>
     *
     * @param attribute -> <strong>Atributo da classe de entidade a ser ordenada</strong>
     * @param name      -> <strong>Nome do entidade a ser buscado</strong>
     * @return T -> <strong>Entidade encontrado</strong>
     */
    public T findByName(String attribute, String name) {
        return query().where().eq(attribute, name).findOne();
    }

    public void updateEntity(Object entity, Object entityInDatabase, String idAttribute, ID id) throws IllegalAccessException {
        Object target = updateField(entity, entityInDatabase);
        query().asUpdate().set(Arrays.toString(target.getClass().getFields()), entity).where().eq(idAttribute, id).update();
    }
}
