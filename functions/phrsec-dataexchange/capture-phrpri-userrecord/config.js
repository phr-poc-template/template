const config = {};

config.keyContainer = process.env.KEYCONTAINER;
config.accessTokenUri = process.env.ACCESS_TOKEN_URI;
config.scopes = process.env.SCOPES;
config.endpoint = process.env.ENDPOINT;

module.exports = config;

