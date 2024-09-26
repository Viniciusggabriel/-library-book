package com.library;

import com.library.application.models.Book;
import com.library.application.models.BorrowedBooks;
import com.library.application.models.UserInLibrary;
import com.library.infra.database.configs.DataBaseSourceConfig;
import com.library.infra.server.configs.ServerHttpConfig;
import com.library.infra.server.handlers.BookHandler;
import com.library.util.errors.handlers.ServerErrorHttpHandler;
import org.eclipse.jetty.ee10.servlet.ErrorHandler;
import org.eclipse.jetty.server.Handler;

public class Main {

    /**
     * <h3>Método principal para execução do projeto, carrega duas outras classes de configurações</h3>
     * <p>Carrega a classe de configuração do banco de dados e passa uma lista com as entidades</p>
     * <p>Carrega a classe do servidor http e passa os handler de rotas para o cliente acessar e os handler de erros em formato de lista</p>
     *
     * @param args -> <strong>Argumentos ao executar a classe via terminal</strong>
     * @throws Exception -> <strong>Exception exigida por classes de configuração</strong>
     */
    public static void main(String[] args) throws Exception {
        // Define o banco de dados da aplicação
        DataBaseSourceConfig.databaseSetup(
                new Class[]{
                        Book.class,
                        BorrowedBooks.class,
                        UserInLibrary.class

                }
        );

        // Define o servidor http da aplicação
        ServerHttpConfig.startServer(
                new Handler[]{
                        BookHandler.setupBookHandler()
                },
                new ErrorHandler[]{
                        new ServerErrorHttpHandler()
                }
        );
    }
}