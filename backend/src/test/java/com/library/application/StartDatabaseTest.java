package com.library.application;

import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import io.github.cdimascio.dotenv.Dotenv;

public class StartDatabaseTest {

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
        dataSourceConfig.setUsername(dotenv.get("PG_USERNAME_TEST"));
        dataSourceConfig.setPassword(dotenv.get("PG_PASSWORD_TEST"));
        dataSourceConfig.setUrl(dotenv.get("PG_JDBC_URL_TEST"));
        dataSourceConfig.setSchema("library_books");
        dataSourceConfig.setPlatform("postgres");

        return dataSourceConfig;
    }

    /**
     * Define o banco de dados da aplicação e cria o sql com o EBEAN
     */
    public static Database databaseTestSetup() {
        // Define a configuração que será usada para gerar o sql com o EBEAN
        DatabaseConfig databaseConfig = new DatabaseConfig();
        databaseConfig.setDataSourceConfig(setupDataSourceConfig());

        databaseConfig.setDdlGenerate(true);
        databaseConfig.setDdlRun(true);

        return DatabaseFactory.create(databaseConfig);
    }
}
