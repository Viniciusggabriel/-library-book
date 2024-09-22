package com.library.application;

import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.annotation.Platform;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class DataBaseSourceConfigTest {
    private static final Logger logger = LoggerFactory.getLogger(DataBaseSourceConfigTest.class);

    /**
     * Define os valores padrões para a conexão do banco de dados, os valores são carregados via variáveis de ambiente
     * Define qual banco de dados será usado no caso é usado o banco de dados H2 para testes
     *
     * @return -> DataSourceConfig retorna a configuração de um banco de dados
     */
    private static DataSourceConfig setupDataSourceConfig() {
        // Carregar variáveis de ambiente do arquivo .env
        Dotenv dotenv = Dotenv.load();
        String username = dotenv.get("PG_USERNAME_TEST");
        String password = dotenv.get("PG_PASSWORD_TEST");
        String jdbcUrl = dotenv.get("PG_JDBC_URL_TEST");

        // Define o datasource
        DataSourceConfig dataSourceConfig = new DataSourceConfig();

        // Configurações do banco de dados para EBEAN
        dataSourceConfig.setUsername(username);
        dataSourceConfig.setPassword(password);
        dataSourceConfig.setUrl(jdbcUrl);

        // Config JDBC
        dataSourceConfig.setPlatform(Platform.H2.name());
        dataSourceConfig.setDriver("org.h2.Driver");

        // Conexão para executar o ddl
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            logger.info("A conexão foi estabelecida com o banco de dados H2: {}", connection);
        } catch (SQLException exception) {
            logger.error("Erro ao realizar conexão de testes no banco de dados em memoria H2: {}", exception.getMessage());
        }

        return dataSourceConfig;
    }

    /**
     * Define o banco de dados da aplicação e cria o sql com o EBEAN
     * Define as entidades que serão usadas nos testes
     */
    public static Database databaseTestSetup(List<Class<?>> entityListClass) {
        // Define a configuração que será usada para gerar o sql com o EBEAN
        DatabaseConfig databaseConfig = new DatabaseConfig();
        databaseConfig.setDataSourceConfig(setupDataSourceConfig());

        // DDL
        databaseConfig.setDdlGenerate(true);
        databaseConfig.setDdlRun(true);

        // Adiciona as entidades passadas por parâmetro
        for (Class<?> entities : entityListClass) {
            databaseConfig.addClass(entities);
        }

        return DatabaseFactory.create(databaseConfig);
    }
}
