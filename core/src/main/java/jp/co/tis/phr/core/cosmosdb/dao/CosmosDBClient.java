package jp.co.tis.phr.core.cosmosdb.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microsoft.azure.documentdb.ConnectionPolicy;
import com.microsoft.azure.documentdb.ConsistencyLevel;
import com.microsoft.azure.documentdb.DocumentClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;

@Slf4j
class CosmosDBClient {

    private static DocumentClient documentClient;

    static {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule jtm = new JavaTimeModule();
        objectMapper.registerModule(jtm);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String proxyHost = System.getenv("PROXY_HOST");
        String proxyPort = System.getenv("PROXY_PORT");
        ConnectionPolicy policy = new ConnectionPolicy();
        if (StringUtils.isNotEmpty(proxyHost) && StringUtils.isNotEmpty(proxyPort)) {
            HttpHost proxy = new HttpHost(proxyHost, Integer.parseInt(proxyPort));
            policy.setProxy(proxy);
        }

        documentClient =
                new DocumentClient(CosmosDBConfiguration.getUri(), CosmosDBConfiguration.getKey(),
                        objectMapper,
                        policy,
                        ConsistencyLevel.Session);
    }

    static DocumentClient getClient() {
        return documentClient;
    }
}
