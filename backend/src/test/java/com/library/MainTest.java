package com.library;

import com.library.dao.entities.Book;
import com.library.server.http.setup.SetupServerHttp;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import io.github.cdimascio.dotenv.Dotenv;

class MainTest {

    public static void main(String[] args) throws Exception {
        SetupServerHttp setupServer = new SetupServerHttp();
        setupServer.startServer("0.0.0.0", 8000);

        // TODO: Criar classe de config do hibernate
        try {
            // Carregar variáveis de ambiente do arquivo .env
            Dotenv dotenv = Dotenv.load();

            DataSourceConfig dataSourceConfig = new DataSourceConfig();
            dataSourceConfig.setUrl(dotenv.get("PG_JDBC_URL_TEST"));
            dataSourceConfig.setUsername(dotenv.get("PG_USERNAME_TEST"));
            dataSourceConfig.setPassword(dotenv.get("PG_PASSWORD_TEST"));
            dataSourceConfig.setSchema("library_books");
            dataSourceConfig.setPlatform("postgres");

            DatabaseConfig databaseConfig = new DatabaseConfig();
            databaseConfig.setDataSourceConfig(dataSourceConfig);

            databaseConfig.setDdlGenerate(true);
            databaseConfig.setDdlRun(true);

            databaseConfig.addClass(Book.class);

            Database database = DatabaseFactory.create(databaseConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}