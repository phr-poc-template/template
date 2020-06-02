package jp.co.tis.phr.core.cosmosdb.dao;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.AppServiceMSICredentials;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.keyvault.Vault;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ResourceBundle;

@Slf4j
class CosmosDBConfiguration {

    private static final ResourceBundle rb = ResourceBundle.getBundle("cosmosdb");

    private static String database;

    private static String uri;

    private static String cosmosdbKey;

    static String getKey() {

        if (StringUtils.isNotEmpty(cosmosdbKey)) {
            return cosmosdbKey;
        }

        // ローカル環境用
        cosmosdbKey = StringUtils.trimToNull(System.getenv().get("COSMOSDB_KEY"));
        if (StringUtils.isNotEmpty(cosmosdbKey)) {
            log.info("Local Configuration.");
            return cosmosdbKey;
        }

        // Azureでのデプロイ用
        log.info("Azure Configuration.");
        String keyvaultName = StringUtils.trimToNull(System.getenv("KEYVAULT_NAME"));
        if (StringUtils.isEmpty(keyvaultName)) {
            throw new IllegalArgumentException("env['KEYVAULT_NAME'] is not set.");
        }
        String keyvaultRg = StringUtils.trimToNull(System.getenv("KEYVAULT_RG"));
        if (StringUtils.isEmpty(keyvaultRg)) {
            throw new IllegalArgumentException("env['KEYVAULT_RG'] is not set.");
        }

        String cosmosdbKeySecret = StringUtils.trimToNull(System.getenv().get("COSMOSDB_KEY_SECRET"));
        if (StringUtils.isEmpty(cosmosdbKeySecret)) {
            throw new IllegalArgumentException("cosmosdb key secret is not set.");
        }
        // マネージドID経由でアクセスキーを取得
        Azure azure;
        try {
            azure = Azure.authenticate(new AppServiceMSICredentials(AzureEnvironment.AZURE)).withDefaultSubscription();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // アクセスキーを使ってKeyVaultからシークレットを取得
        log.info("Get CosmosDBKey from KeyVault. KEYVAULT_NAME[" + keyvaultName + "] " +
                "KEYVAULT_RG[" + keyvaultRg + "] COSMOSDB_KEY_SECRET[" + cosmosdbKeySecret + "]");
        Vault vault = azure.vaults().getByResourceGroup(keyvaultRg, keyvaultName);
        cosmosdbKey = vault.secrets().getByName("cosmosdb-key").value();
        log.info("COSMOSDB_KEY:" + cosmosdbKey.substring(0, 5) + "*********************************");
        return cosmosdbKey;
    }

    static String getUri() {

        if (StringUtils.isNotEmpty(uri)) {
            return uri;
        }

        // Azureでの実行
        uri = StringUtils.trimToNull(System.getenv("COSMOSDB_URI"));
        if (StringUtils.isEmpty(uri)) {
            // ローカルでの実行用
            uri = rb.getString("azure.cosmosdb.uri");
        }
        if (StringUtils.isEmpty(uri)) {
            throw new IllegalArgumentException("cosmosdb uri is not set.");
        }
        log.info("COSMOSDB_URI:" + uri);
        return uri;
    }

    static String getDatabase() {

        if (StringUtils.isNotEmpty(database)) {
            return database;
        }

        // Azureでの実行
        database = StringUtils.trimToNull(System.getenv("COSMOSDB_DATABASE"));
        if (StringUtils.isEmpty(database)) {
            // ローカルでの実行
            database = rb.getString("azure.cosmosdb.database");
        }
        if (StringUtils.isEmpty(database)) {
            throw new IllegalArgumentException("database name is not set.");
        }
        log.info("COSMOSDB_DATABASE:" + database);
        return database;
    }
}
