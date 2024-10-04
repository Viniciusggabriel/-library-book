package com.library.infra.server.configs;

import com.library.infra.server.connectors.ConectorServerHttp;
import org.eclipse.jetty.ee10.servlet.ErrorHandler;
import org.eclipse.jetty.server.CustomRequestLog;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.RequestLogWriter;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimeZone;

public class ServerHttpConfig {
    private static final Logger logger = LoggerFactory.getLogger(ServerHttpConfig.class);

    /**
     * <h3>Define a configuração dos servidores no momento usa somente o servidor HTTP</h3>
     * <p>Recebe as classes de handlers e errorHandlers</p>
     *
     * @param routsHandlers      -> <strong>Rotas da api que serão usadas pelo cliente ao acessar a aplicação</strong>
     * @param exceptionsHandlers -> <strong>Handlers der erros que serão usados para retornar</strong>
     * @throws Exception -> <strong>Exception padrão para tratamento de erros dentro do servidor</strong>
     */
    public static void startServer(Handler[] routsHandlers, ErrorHandler[] exceptionsHandlers) throws Exception {
        Server server = null;

        try {
            // Configuração do servidor Jetty
            server = new Server();
            server.addConnector(ConectorServerHttp.setupHttpServerConnector(server));

            // Define o handlers para acesso do usuário
            for (Handler handler : routsHandlers) {
                server.setHandler(handler);
            }

            // Define os handlers de erros
            for (ErrorHandler handler : exceptionsHandlers) {
                server.setErrorHandler(handler);
            }

            server.start();
            logger.info("Servidor iniciado com sucesso.");

        } catch (Exception exception) {
            logger.error("Erro ao iniciar o servidor HTTP.", exception);

        } finally {
            // Verifica se a variável de servidor ficou nulo ou deu erros inesperados, após isso encerra o servidor
            if (server != null && server.isFailed()) {
                try {
                    server.stop();
                } catch (Exception stopException) {
                    logger.error("Erro ao tentar parar o servidor após falha de inicialização.", stopException);
                }
            }
        }
    }
}
