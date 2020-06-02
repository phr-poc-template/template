const applicationUUIDNameSpace = '9576c341-8907-4bf2-a6cc-1036bdcdf54e';
const uuidv3 = require('uuid/v3');
const az = require("@azure/identity");
const {BlobServiceClient} = require("@azure/storage-blob");

module.exports = async function(context, mySbMsg) {
    context.log('Received message', mySbMsg);

    // Retrieve products information from Blob using input bindings
    const downloaded = context.bindings.productsRecord;

    // Find appropriate product using sent data
    const productData = downloaded.filter((item, index) => {
        context.log(item.productCode);
        if (item.productCode == mySbMsg.model_number) return true;
    });

    let userRecord = {
        "id": uuidv3(
            mySbMsg.person_id + mySbMsg.observation_time + mySbMsg.production_specification_serial, 
            applicationUUIDNameSpace),
        "event": createEvent(
            mySbMsg.observation_time, 
            mySbMsg.model_manufacturer_code, 
            mySbMsg.model_number, 
            null, 
            mySbMsg.production_specification_serial, 
            mySbMsg.model_number, 
            productData[0].reliability, 
            productData[0].recordType),
        "user_id": mySbMsg.person_id,
        "systolic_blood_pressure": mySbMsg.systolic,
        "diastolic_blood_pressure": mySbMsg.diastolic,
        "heart_rate": mySbMsg.pulse_rate
    }

    context.log('User Record that will be loaded into cosmos: ' + userRecord);

    context.bindings.userRecord = userRecord;
    context.done();
};


function createEvent(timeString, makerCode, productCode, version, serialId, name, reliability, recordType) {
  var obj = new Object();
  obj.time = toDate(timeString);
  obj.maker_code =makerCode;
  obj.product_code = productCode;
  obj.version = version;
  obj.serialId = serialId;
  obj.name = name;
  obj.reliability = reliability;
  obj.record_type = recordType;
  return obj;
}

function toDate(strDate) {
    var pattern = /^(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})$/;
    var arrayDate = strDate.match(pattern);
    return new Date(arrayDate[1], arrayDate[2]-1, arrayDate[3], arrayDate[4], arrayDate[5], arrayDate[6]);
}