const CosmosClient = require('@azure/cosmos').CosmosClient;

class CosmosDBDao {
    /**
     * Manages reading, adding, and updating Tasks in Cosmos DB
     * @param {CosmosClient} cosmosClient
     * @param {string} databaseId
     * @param {string} containerId
     */
    constructor(cosmosClient, databaseId, containerId) {
        this.client = cosmosClient;
        this.databaseId = databaseId;
        this.collectionId = containerId;
        this.database = null;
        this.container = null;
    }

    async init() {
        console.log('Setting up the database...');
        const dbResponse = await this.client.databases.createIfNotExists({
            id: this.databaseId
        });
        this.database = dbResponse.database;
        console.log('Setting up the database...done!');
        console.log('Setting up the container...');
        const coResponse = await this.database.containers.createIfNotExists({
            id: this.collectionId
        });
        this.container = coResponse.container;
        console.log('Setting up the container...done!')
    }

    async find(querySpec) {
        console.log('Querying for items from the database');
        if (!this.container) {
            throw new Error('Collection is not initialized.')
        }
        const { resources } = await this.container.items.query(querySpec).fetchAll();
        return resources
    }
}

module.exports = CosmosDBDao;