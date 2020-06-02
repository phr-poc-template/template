const dateFormat = require('dateformat');
const CosmosClient = require('@azure/cosmos').CosmosClient;
const UserRecordDao = require('./cosmosDBDao');

module.exports = async (context, req) => {

    const cosmosdbConnectionString = process.env.COSMOSDB_CONNECTION_STRING;
    context.log("COSMOSDB_CONNECTION_STRING", cosmosdbConnectionString)
    const client = new CosmosClient(cosmosdbConnectionString);

    const documents = await fetch(client, req.params.userId, req.query.offset, req.query.limit);

    if (documents.length == null) {
        context.log("not found.");
    }
    context.log(documents);

    const userData = {
        "format_version": "1.0",
        "send_time": dateFormat(new Date(), "yyyymmddHHMMssl"),
        "consortium_id": "100Z",
    };

    let personInformations = [];
    let healthCareBloodPressures = [];
    let healthCareWeighingScales = [];
    let healthCheckuphbA1ces = [];
    let healthCheckupBloodPressures = [];
    for (let i = 0; i < documents.length; i++) {
        const document = documents[i];

        // 体重
        if (document.weight) {
            healthCareWeighingScales.push(healthCareWeighingScale(document));
        }

        // 血圧
        if (document.systolic_blood_pressure) {
            if (isMedicalExamData(document)) {
                // 健診データ
                healthCheckupBloodPressures.push(healthCheckupBloodPressure(document));
            } else {
                // 家庭用
                healthCareBloodPressures.push(healthCareBloodPressure(document));
            }
        }

        // hba1c
        if (document.hbA1c) {
            // 現状健康診断情報からしか入ってこないため、RecordType="MedicalExam"のみとしている。
            if (isMedicalExamData(document)) {
                healthCheckuphbA1ces.push(healthCheckuphbA1c(document));
            }
        }

        const userId = document.user_id;
        if (documents[i + 1] != null) {
            const nextUserId = documents[i + 1].user_id;
            if (userId === nextUserId) {
                continue;
            }
        }
        // 次のユーザがある場合は以下へ
        const healthcareInformation = {
            "weighing_scale": healthCareWeighingScales,
            "blood_pressure": healthCareBloodPressures
        };
        const healthCheckupInformation = {
            "hba1c": healthCheckuphbA1ces,
            "blood_pressure": healthCheckupBloodPressures
        };
        let personInfomation = {
            "person_id": userId,
            "healthcare_information": healthcareInformation,
            "health_checkup_information": healthCheckupInformation
        };
        personInformations.push(personInfomation);

        personInfomation = [];
        healthCareWeighingScales = [];
        healthCareBloodPressures = [];
        healthCheckuphbA1ces = [];
        healthCheckupBloodPressures = [];
    }

    userData.person_information = personInformations;

    const json = JSON.stringify(userData);
    context.log("found." + json);
    context.done(null, json);
};

const fetch = async (client, user_id, offset, limit) => {
    const userRecordDao = new UserRecordDao(client, "phrdb", "user_record");
    await userRecordDao.init(err => {
        console.error(err)
    }).catch(err => {
        console.error(err);
        process.exit(1);
    });

    let querySpec;
    if (isNaN(offset) || isNaN(offset)) {
        querySpec = {
            query: "SELECT TOP 1000 * FROM user_record u WHERE u.user_id=@user_id ORDER BY u.event.time",
            parameters: [
                {
                    name: "@user_id",
                    value: user_id
                }
            ]
        };
    } else {
        querySpec = {
            query: "SELECT * FROM user_record u WHERE u.user_id=@user_id ORDER BY u.event.time OFFSET @offset LIMIT @limit",
            parameters: [
                {
                    name: "@user_id",
                    value: user_id
                },
                {
                    name: "@offset",
                    value: Number(offset)
                },
                {
                    name: "@limit",
                    value: Number(limit)
                }
            ]
        };
    }
    console.log(querySpec)
    return await userRecordDao.find(querySpec);
};

const isMedicalExamData = (document) => {
    return document.event.record_type === "MedicalExam";
};

const formatDate = (time) => (dateFormat(new Date(time), "yyyymmddHHMMss"));

const healthCareBloodPressure = (document) => {
    return {
        "observation_time": formatDate(document.event.time),
        "systolic": document.systolic_blood_pressure,
        "diastolic": document.diastolic_blood_pressure,
        "pulse_rate": document.pulse_rate,
        "model_manufacturer_code": document.event.maker_code,
        "model_number": document.event.product_code,
        "production_specification_serial": document.event.serialId,
        "input_method_code": "0",
        "origin_of_time_code": "0",
        "update_time": formatDate(document.event.time)
    };
};

const healthCareWeighingScale = (document) => {
    return {
        "observation_time": formatDate(document.event.time),
        "body_weight": document.weight,
        "body_fat": document.fat,
        "model_manufacturer_code": document.event.maker_code,
        "model_number": document.event.product_code,
        "production_specification_serial": document.event.serialId,
        "input_method_code": "0",
        "origin_of_time_code": "0",
        "update_time": formatDate(document.event.time)
    };
};

const healthCheckuphbA1c = (document) => {
    return {
        "observation_time": formatDate(document.event.time),
        "hba1c_value": document.hbA1c,
        "input_method_code": "0",
        "measurement_method_code": "000001",
        "value_criteria_code": "0",
        "test_lab_id": "000000000001",
        "update_time": formatDate(document.event.time),
    };
};

const healthCheckupBloodPressure = (document) => {
    return {
        "observation_time": formatDate(document.event.time),
        "systolic": document.systolic_blood_pressure,
        "diastolic": document.diastolic_blood_pressure,
        "pulse_rate": document.pulse_rate
    };
};

