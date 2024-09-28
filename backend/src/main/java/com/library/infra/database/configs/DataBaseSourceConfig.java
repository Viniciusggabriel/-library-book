package com.library.infra.database.configs;

import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.annotation.Platform;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;

public class DataBaseSourceConfig {
    @Getter
    private static Database database;

    /**
     * <h3>Define os valores padrões para a conexão do banco de dados, os valores são carregados via variáveis de ambiente</h3>
     *
     * @return DataSourceConfig -> <strong>retorna a configuração de um banco de dados</strong>
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
     * <h3>Define o banco de dados da aplicação e cria o sql com o EBEAN</h3>
     *
     * @param entityListClass -> <strong>Recebe as classes de entidade como parâmetro</strong>
     */
    public static void databaseSetup(Class<?>[] entityListClass) {
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

        database = DatabaseFactory.create(databaseConfig);
    }
}
