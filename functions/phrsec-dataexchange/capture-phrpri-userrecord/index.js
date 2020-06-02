const az = require("@azure/identity");
const rp = require('request-promise');
const config = require('./config');
const ClientOAuth2 = require('client-oauth2');
const {SecretClient} = require('@azure/keyvault-secrets');
const applicationUUIDNameSpace = '9576c341-8907-4bf2-a6cc-1036bdcdf54e';
const uuidv3 = require('uuid/v3');

module.exports = async (context) => {

    if (typeof config.keyContainer == 'undefined') {
        context.log('Error: "KEYCONTAINER" is not set.');
        return;
    }
    if (typeof config.accessTokenUri == 'undefined') {
        context.log('Error: "ACCESS_TOKEN_URI" is not set.');
        return;
    }
    if (typeof config.scopes == 'undefined') {
        context.log('Error: "SCOPES" is not set.');
        return;
    }
    if (typeof config.endpoint == 'undefined') {
        context.log('Error: "ENDPOINT" is not set.');
        return;
    }

    const tokenCredential = new az.ManagedIdentityCredential();
    const url = 'https://' + config.keyContainer + '.vault.azure.net';
    const client = new SecretClient(url, tokenCredential);

    const clientId = await client.getSecret("data-exchange-client-id");
    const clientSecret = await client.getSecret("data-exchange-client-secret");

    const oauth2 = new ClientOAuth2({
        clientId: clientId.value,
        clientSecret: clientSecret.value,
        accessTokenUri: config.accessTokenUri,
        scopes: [config.scopes]
    });

    const accessToken = await oauth2.credentials.getToken()
        .then(function (user) {
            return user.accessToken;
        });

    context.log("accessToken:" + accessToken);

    const body = await rp({
        method: 'get',
        url: config.endpoint,
        headers: {
            Authorization: 'Bearer ' + accessToken
        }
    }, function (err, res, body) {
        context.log(body);
        return body;
    });
    const personInformation = JSON.parse(body).person_information;

    context.bindings.outputSbQueue = [];
    await personInformation.forEach(p => {
        const personId = p.person_id;
        const bloodPressures = p.healthcare_information.blood_pressure;
        if (bloodPressures.length !== 0) {
            bloodPressures.forEach(b => {
                let uuidName = personId + '-' + b.observation_time;
                b.id = uuidv3(uuidName, applicationUUIDNameSpace);
                b.person_id = personId;
                context.log(b);
                context.bindings.outputSbQueue.push(b);
            });
        }
    });
};

