const az = require("@azure/identity");
const {BlobServiceClient} = require("@azure/storage-blob");
require('dotenv').config();

module.exports = async (context, documents) => {

    if (!documents || documents.length <= 0) {
        return;
    }
    context.log("documents length " + documents.length)

    const storageAccount = process.env.STORAGE_ACCOUNT;
    if (typeof process.env.STORAGE_ACCOUNT == 'undefined') {
        context.log('Error: "STORAGE_ACCOUNT" is not set.');
        return;
    }
    context.log("StorageAccount:" + storageAccount);
    const containerName = process.env.CONTAINER_NAME;
    if (typeof process.env.CONTAINER_NAME == 'undefined') {
        context.log('Error: "CONTAINER_NAME" is not set.');
        return;
    }
    context.log("ContainerName:" + containerName);

    for (let i = 0; i < documents.length; i++) {
        const id = documents[i].id;
        context.log('Document Id: ', id);

        const json = JSON.stringify(documents[i]);

        const tokenCredential = new az.ManagedIdentityCredential();
        const blobServiceClient = new BlobServiceClient('https://' + storageAccount + '.blob.core.windows.net', tokenCredential);

        const containerClient = blobServiceClient.getContainerClient(containerName);

        const time = documents[i].event.time;
        context.log("EventTime: " + time);
        const blobName = getBlobName(id, documents[i].event.time);
        const blockBlobClient = containerClient.getBlockBlobClient(blobName);

        await blockBlobClient.upload(json, json.length);
        context.log("Upload block blob successfully. ID is " + id);
    }

};

const getBlobName = (id, time) => (time.split('T')[0] + "/" + id + ".json");
