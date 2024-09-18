package com.library;

import com.library.infra.database.configs.DataBaseSourceConfig;
import com.library.infra.server.configs.ServerHttpConfig;
import com.library.infra.server.handlers.BookHandler;
import com.library.util.errors.handlers.ServerErrorHttpHandler;
import com.library.application.models.Book;
import com.library.application.models.UserInLibrary;

import java.util.List;

public class Main {

    /**
     * Método main para execução do projeto, carrega duas outras classes de configurações
     * Carrega a classe de configuração do banco de dados e passa uma lista com as entidades
     * Carrega a classe do servidor http e passa os handler de rotas para o cliente acessar e os handler de erros em formato de lista
     *
     * @param args -> Argumentos ao executar a classe via terminal
     * @throws Exception -> Exception exigida por classes de configuração
     */
    public static void main(String[] args) throws Exception {
        // Define o banco de dados da aplicação
        DataBaseSourceConfig.databaseSetup(
                List.of(
                        Book.class,
                        UserInLibrary.class
                )
        );

        // Define o servidor http da aplicação
        ServerHttpConfig.startServer(
                List.of(
                        BookHandler.setupBookHandler()
                ),
                List.of(
                        new ServerErrorHttpHandler()
                )
        );
    }
}