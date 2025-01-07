package com.library;

import com.library.application.models.Book;
import com.library.application.models.BorrowedBooks;
import com.library.application.models.ClientInLibrary;
import com.library.application.models.UserInLibrary;
import com.library.infra.database.configs.DataBaseSourceConfig;
import com.library.infra.server.configs.ServerHttpConfig;
import com.library.infra.server.handlers.BookHandlers;
import com.library.infra.server.handlers.UserInLibraryHandlers;
import com.library.util.errors.handlers.JettyServerHttpErrorHandler;
import org.eclipse.jetty.ee10.servlet.ErrorHandler;
import org.eclipse.jetty.server.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());


    /**
     * <h3>Método principal para execução do projeto, carrega duas outras classes de configurações</h3>
     * <p>Carrega a classe de configuração do banco de dados e passa uma lista com as entidades</p>
     * <p>Carrega a classe do servidor http e passa os handler de rotas para o cliente acessar e os handler de erros em formato de lista</p>
     *
     * @param args -> <strong>Argumentos ao executar a classe via terminal</strong>
     * @throws Exception -> <strong>Exception exigida por classes de configuração</strong>
     */
    public static void main(String[] args) throws Exception {
        // Cria e inicia a thread principal para executar o setup do Docker
        Thread setupDockerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Executa o setup do Docker (simulação do processo de setup)
                SetupDockerCompose setupDockerCompose = new SetupDockerCompose();
                setupDockerCompose.run();
            }
        });

        setupDockerThread.start();

        // Cria e inicia a thread que espera a setupDocker terminar e depois aguarda 60 segundos
        Thread nextTaskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Aguarda a thread setupDocker terminar
                    setupDockerThread.join();  // Espera a thread 'setupDockerThread' terminar
                    logger.info("Thread de setup Docker terminou.");

                    // Após terminar, dorme por 60 segundos
                    Thread.sleep(60000);  // 60 segundos de espera
                    logger.info("Atraso de 60 segundos concluído.");

                    ExecuteDataBaseConnection executeDataBaseConnection = new ExecuteDataBaseConnection();
                    executeDataBaseConnection.run();

                    ExecuteServerConnection executeServerConnection = new ExecuteServerConnection();
                    executeServerConnection.run();
                } catch (InterruptedException e) {
                    logger.warning("Erro ao aguardar ou dormir: " + e.getMessage());
                }
            }
        });

        nextTaskThread.start();
    }
}

class SetupDockerCompose implements Runnable {
    private static final Logger logger = Logger.getLogger(SetupDockerCompose.class.getName());
    private static final String[] COMMAND = {"docker-compose", "up", "-d"};

    @Override
    public void run() {
        ProcessBuilder processBuilder = new ProcessBuilder(COMMAND);
        processBuilder.redirectErrorStream(true);

        try {
            // Inicia o processo
            Process process = processBuilder.start();

            // Captura e exibe a saída do comando
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info(line);
                }
            }

            // Espera o processo terminar e verifica se foi bem-sucedido
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                logger.info("docker-compose up -d executado com sucesso.");
            } else {
                logger.warning("docker-compose up -d falhou com código de saída: " + exitCode);
            }
        } catch (IOException | InterruptedException exception) {
            logger.severe("Erro ao executar docker-compose up -d: " + exception.getMessage());
        }
    }
}

class ExecuteDataBaseConnection implements Runnable {
    private static final Logger logger = Logger.getLogger(ExecuteDataBaseConnection.class.getName());

    // Define o banco de dados da aplicação
    @Override
    public void run() {
        DataBaseSourceConfig.databaseSetup(
                new Class[]{
                        Book.class,
                        BorrowedBooks.class,
                        ClientInLibrary.class,
                        UserInLibrary.class

                }
        );
    }
}

class ExecuteServerConnection implements Runnable {
    private static final Logger logger = Logger.getLogger(ExecuteServerConnection.class.getName());

    // Define o servidor http da aplicação
    @Override
    public void run() {
        try {
            ServerHttpConfig.startServer(
                    new Handler[]{
                            BookHandlers.setupBookHandler(),
                            UserInLibraryHandlers.setupUserInLibraryHandler()
                    },
                    new ErrorHandler[]{
                            new JettyServerHttpErrorHandler()
                    }
            );
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}