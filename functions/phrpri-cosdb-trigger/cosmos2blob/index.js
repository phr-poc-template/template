const applicationUUIDNameSpace = '9576c341-8907-4bf2-a6cc-1036bdcdf54e';
const { v3: uuidv3 } = require('uuid');
const az = require("@azure/identity");
const {BlobServiceClient} = require("@azure/storage-blob");
require('dotenv').config();

module.exports = async (context, documents) => {
    context.log.info("Starting to create documents into a journal")

    if (!shouldBeProcessed(context, documents)) {
        return;
    }

    let journalDocument = documents[0];

    const blockBlobClient = getblockBlobClient(
        context,
        process.env.STORAGE_ACCOUNT,
        process.env.CONTAINER_NAME,
        journalDocument.user_id,
        journalDocument.event.publish
    );

    journalDocument.operation = journalDocument.deleted ? "DELETE" : "UPSERT";
    journalDocument.operated_by = journalDocument.user_id;

    const json = JSON.stringify(journalDocument);
    await blockBlobClient.upload(json, json.length);

    context.log.info("Uploaded: " + json)
    context.log.info("Finish to create documents into a journal")
};

const shouldBeProcessed = (context, documents) => {
    let validateResult = true;

    if (!documents || documents.length <= 0) {
        context.log.error('Error: "documents" is not valid.');
        validateResult = false;
    }
    if (typeof process.env.STORAGE_ACCOUNT == 'undefined') {
        context.log.error('Error: "STORAGE_ACCOUNT" is not set.');
        validateResult = false;
    }
    if (typeof process.env.CONTAINER_NAME == 'undefined') {
        context.log.error('Error: "CONTAINER_NAME" is not set.');
        validateResult = false;
    }
    // Deviceデータ以外はBlobStorageに格納する
    if (isDeviceData(documents[0])) {
        context.log.info(documents[0].id + " isDeviceData");
        validateResult = false;
    }

    return validateResult;
};

const isDeviceData = (json) => (json['event']['record_type'] === 'Device');

const getBlobName = (id, time) => (time.split('T')[0] + "/" + id + ".json");

const getblockBlobClient = (context, storageAccount, containerName, user_id, publish) => {
    context.log.info("Get Block Blob Client using the following storage account and container name. " + "StorageAccount: " + storageAccount + ", ContainerName: " + containerName)

    const tokenCredential = new az.ManagedIdentityCredential();
    const blobServiceClient = new BlobServiceClient('https://' + storageAccount + '.blob.core.windows.net', tokenCredential);
    const containerClient = blobServiceClient.getContainerClient(containerName);

    const blobName = getBlobName(uuidv3(user_id + publish, applicationUUIDNameSpace), publish);

    return containerClient.getBlockBlobClient(blobName);
};