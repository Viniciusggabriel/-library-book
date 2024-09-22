package com.library.infra.database.configs;

import io.ebean.DatabaseFactory;
import io.ebean.annotation.Platform;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;

public class DataBaseSourceConfig {

    /**
     * Define os valores padrões para a conexão do banco de dados, os valores são carregados via variáveis de ambiente
     *
     * @return -> DataSourceConfig retorna a configuração de um banco de dados
     */
    private static DataSourceConfig setupDataSourceConfig() {
        // Carregar variáveis de ambiente do arquivo .env
        Dotenv dotenv = Dotenv.load();

        // Define o datasource
        DataSourceConfig dataSourceConfig = new DataSourceConfig();

        // Configurações do banco de dados para EBEAN
        dataSourceConfig.setUsername(dotenv.get("PG_USERNAME_PROD"));
        dataSourceConfig.setPassword(dotenv.get("PG_PASSWORD_PROD"));
        dataSourceConfig.setUrl(dotenv.get("PG_JDBC_URL_PROD"));

        // DDL
        dataSourceConfig.setSchema("library_books");

        // Config JDBC
        dataSourceConfig.setPlatform(Platform.POSTGRES.name());

        return dataSourceConfig;
    }

    /**
     * Define o banco de dados da aplicação e cria o sql com o EBEAN
     *
     * @param entityListClass -> Recebe as classes de entidade como parâmetro
     */
    public static void databaseSetup(List<Class<?>> entityListClass) {
        // Define a configuração que será usada para gerar o sql com o EBEAN
        DatabaseConfig databaseConfig = new DatabaseConfig();
        databaseConfig.setDataSourceConfig(setupDataSourceConfig());

        // DDL
        databaseConfig.setDdlGenerate(true);
        databaseConfig.setDdlRun(true);
        databaseConfig.setDbSchema("library_books");

        // Adiciona as entidades passadas por parâmetro
        for (Class<?> entities : entityListClass) {
            databaseConfig.addClass(entities);
        }

        DatabaseFactory.create(databaseConfig);
    }
}
