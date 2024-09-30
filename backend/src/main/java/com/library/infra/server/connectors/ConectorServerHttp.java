package com.library.infra.server.connectors;

import com.library.util.errors.exceptions.InternalServerException;
import com.library.util.errors.exceptions.ServerPortException;
import io.github.cdimascio.dotenv.Dotenv;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConectorServerHttp {
    private static final Logger logger = LoggerFactory.getLogger(ConectorServerHttp.class);

    /**
     * <h3>Método responsável por definir o connector http do projeto</h3>
     * <p>Busca o host e a porta http dentro das variáveis de ambiente</p>
     *
     * @param server -> <strong>Pede o servidor para poder realizar a configuração necessária do conector</strong>
     * @return ServerConnector -> <strong>Retorna o connector HTTP configurado</strong>
     */
    public static ServerConnector setupHttpServerConnector(Server server) {
        try {
            // Carregar variáveis de ambiente do arquivo .env
            Dotenv dotenv = Dotenv.load();

            // Configura um servidor para conexão http
            ServerConnector connector = new ServerConnector(server);
            connector.setHost(dotenv.get("SERVER_HTTP_HOST"));
            connector.setPort(Integer.parseInt(dotenv.get("SERVER_HTTP_PORT")));

            return connector;

        } catch (NumberFormatException exception) {
            logger.error("Erro ao converter a porta do servidor HTTP para número. Verifique a variável 'SERVER_HTTP_PORT' no arquivo .env.", exception);
            throw new ServerPortException(exception.getMessage(), HttpStatus.BAD_GATEWAY_502); // Repassa a mensagem de erro para uma exceção personalizada

        } catch (Exception exception) {
            logger.error("Erro ao configurar o servidor HTTP.", exception);
            throw new InternalServerException(String.format("Falha na configuração do ServerConnector: %s", exception.getMessage()));
        }
    }
}
