package jp.co.tis.phr.core.cosmosdb.dao;

import com.microsoft.azure.documentdb.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Slf4j
public class BaseDao<T> {

    private String containerName;

    private Class<T> entityClass;

    public BaseDao() {

        Class<?> clazz = this.getClass();
        Type type = clazz.getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) type;
        // BaseDaoの型変数に対するバインドされた型がとれる
        Type[] actualTypeArguments = pt.getActualTypeArguments();
        @SuppressWarnings("unchecked") Class<T> entityClass = (Class<T>) actualTypeArguments[0];
        // annotationからコンテナ名を取得する
        final jp.co.tis.phr.core.cosmosdb.annotation.Document annotation
                = entityClass.getAnnotation(jp.co.tis.phr.core.cosmosdb.annotation.Document.class);
        if (annotation != null && !annotation.container().isEmpty()) {
            this.containerName = annotation.container();
        }
        this.entityClass = entityClass;
    }

    protected void save(T objectToSave, PartitionKey partitionKey) {
        try {
            final RequestOptions options = getRequestOptions(partitionKey);
            ResourceResponse<Document> response = CosmosDBClient.getClient().upsertDocument(
                    getCollectionLink(), objectToSave, options,
                    true);
            log.info("[Upsert]RequestCharge:" + response.getRequestCharge());
        } catch (DocumentClientException e) {
            throw new RuntimeException(e);
        }
    }

    protected T findById(String id) {

        try {
            PartitionKey partitionKey = new PartitionKey(id);
            final RequestOptions options = getRequestOptions(partitionKey);

            // DocumentIDにはidを指定する
            ResourceResponse<Document> resourceResponse =
                    CosmosDBClient.getClient().readDocument(getDocumentLink(id), options);
            log.info("[FindById]RequestCharge:" + resourceResponse.getRequestCharge());
            return new DocumentConverter().read(entityClass, resourceResponse.getResource());

        } catch (DocumentClientException e) {
            // データが見つからない場合はnullを返す
            if (e.getStatusCode() == 404) {
                return null;
            }
            throw new RuntimeException(e);
        }
    }

    protected Optional<Integer> getAggregatedAsInt(SqlQuerySpec sqlQuerySpec, FeedOptions options) {
        FeedResponse<Document> feedResponse =
                CosmosDBClient.getClient().queryDocuments(getCollectionLink(), sqlQuerySpec, options);
        log.info("[Query]" + sqlQuerySpec + ", "
                + "[RequestCharge]" + feedResponse.getRequestCharge());

        Iterator<Document> docs = feedResponse.getQueryIterator();
        if (docs.hasNext()) {
            return Optional.ofNullable(docs.next().getInt("_aggregate").intValue());
        }
        return Optional.empty();
    }

    protected Optional<String> getAggregatedAsString(SqlQuerySpec sqlQuerySpec, FeedOptions options) {
        FeedResponse<Document> feedResponse =
                CosmosDBClient.getClient().queryDocuments(getCollectionLink(), sqlQuerySpec, options);
        log.info("[Query]" + sqlQuerySpec + ", "
                + "[RequestCharge]" + feedResponse.getRequestCharge());

        Iterator<Document> docs = feedResponse.getQueryIterator();
        if (docs.hasNext()) {
            return Optional.ofNullable(docs.next().getString("_aggregate"));
        }
        return Optional.empty();
    }

    protected List<T> find(SqlQuerySpec sqlQuerySpec, FeedOptions options) {
        FeedResponse<Document> feedResponse =
                CosmosDBClient.getClient().queryDocuments(getCollectionLink(), sqlQuerySpec, options);
        log.info("[Query]" + sqlQuerySpec + ", "
                + "[RequestCharge]" + feedResponse.getRequestCharge());

        Iterator<Document> docs = feedResponse.getQueryIterator();

        List<T> results = new ArrayList<>();
        while (docs.hasNext()) {
            Document doc = docs.next();
            results.add(new DocumentConverter().read(entityClass, doc));
        }
        return results;
    }

    public List<T> findAll() {
        String query = "SELECT * FROM " + containerName;
        FeedOptions options = new FeedOptions();
        options.setEnableCrossPartitionQuery(true);
        return find(new SqlQuerySpec(query), options);
    }

    public void delete(String key) {
        try {
            // パーティションキーがあるコンテナはパーティションキーを指定する必要がある。
            PartitionKey partitionKey = new PartitionKey(key);
            final RequestOptions options = getRequestOptions(partitionKey);
            CosmosDBClient.getClient().deleteDocument(getDocumentLink(key), options);
        } catch (DocumentClientException e) {
            throw new RuntimeException(e);
        }
    }

    private String getDatabaseLink() {
        return "dbs/" + CosmosDBConfiguration.getDatabase();
    }

    private String getCollectionLink() {
        return getDatabaseLink() + "/colls/" + containerName;
    }

    private String getDocumentLink(Object documentId) {
        return getCollectionLink() + "/docs/" + documentId;
    }

    private RequestOptions getRequestOptions(PartitionKey key) {
        final RequestOptions options = new RequestOptions();
        if (key != null) {
            options.setPartitionKey(key);
        }
        return options;
    }
}
