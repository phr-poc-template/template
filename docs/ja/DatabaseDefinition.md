## PHRデータベース定義

### User

|     カラム     |  物理名（英名）   |   型   | 必須  |     備考      |
| -------------- | ----------------- | ------ | :---: | ------------- |
| ID             | id                | 文字列 |   *   | user_idを設定 |
| ユーザID       | user_id           | 文字列 |   *   |               |
| パスワード     | password          | 文字列 |   *   |               |
| メールアドレス | email             | 文字列 |   *   |               |
| 登録日         | registration_date | 文字列 |   *   |               |

### UserRecord

[日本医療情報学会：生活習慣病コア項目セット集（第2版）および生活習慣病自己管理項目セット集（第2版）](http://jami.jp/medicalFields/2018Oct23_02.pdf)を参考に定義しています。  
健康診断の情報の登録画面では図表1-2に記載されている「健康などから」に〇がついている項目入力できるようになっており、デバイスからのデータは血圧などの情報に加え、機器の情報が入ってきます。

|      カラム       |                  |            |      物理名（英名）      |   型   | 必須  | 健康診断 | デバイス |               備考               |
| ----------------- | ---------------- | ---------- | ------------------------ | ------ | :---: | :------: | :------: | -------------------------------- |
| ID(partition key) |                  |            | id                       | 文字列 |   *   |    *     |    *     | *1                               |
|                   |                  |            | event                    | 数値   |       |    *     |    *     |                                  |
|                   | 測定日/基準日    |            | time                     | 文字列 |   *   |    *     |    *     | ISO-8601(UTC)                    |
|                   | 登録日           |            | publish                     | 文字列 |   *   |    *     |    *     | ISO-8601(UTC)                    |
|                   | メーカー         |            | maker_code               | 文字列 |       |          |    *     |                                  |
|                   | プロダクトコード |            | product_code             | 文字列 |       |          |    *     | *2                               |
|                   | バージョン       |            | version                  | 文字列 |       |          |    *     |                                  |
|                   | シリアル番号     |            | serialId                 | 文字列 |       |          |    *     |                                  |
|                   | 機器の名称       |            | name                     | 文字列 |       |          |    *     |                                  |
|                   | 信頼度           |            | reliability              | 文字列 |   *   |    *     |    *     | *2                               |
|                   | レコードタイプ   |            | record_type              | 文字列 |   *   |    *     |    *     | Device/MedicalExam               |
| ユーザID          |                  |            | user_id                  | 文字列 |       |    *     |    *     |                                  |
| 身長              |                  |            | height                   | 文字列 |       |    *     |    *     | cm                               |
| 体重              |                  |            | weight                   | 文字列 |       |    *     |    *     | kg                               |
| 拡張時血圧        |                  |            | systolic_blood_pressure  | 数値   |       |    *     |    *     | mmHg                             |
| 収縮時血圧        |                  |            | diastolic_blood_pressure | 数値   |       |    *     |    *     | mmHg                             |
| LDLコレステロール |                  |            | ldl_cholesterol          | 数値   |       |    *     |          | mg/dL                            |
| HDLコレステロール |                  |            | hdl_cholesterol          | 数値   |       |    *     |          | mg/dL                            |
| 喫煙              |                  |            | tobacco_history          | 文字列 |       |    *     |          | あり、なし、過去にあり           |
| 尿蛋白            |                  |            | urine_protein            | 文字列 |       |    *     |          | －、±、＋、2＋、3＋以上          |
| 血糖              |                  |            | fasting_plasma_glucose   | 数値   |       |    *     |          | mg/dL                            |
| HbA1c             |                  |            | hbA1c                    | 数値   |       |    *     |          | %                                |
| ALT               |                  |            | alt                      | 数値   |       |    *     |          | IU/L                             |
| 中性脂肪          |                  |            | neutral_fat              | 数値   |       |    *     |          | mg/dL                            |
| AST               |                  |            | ast                      | 数値   |       |    *     |          | IU/L                             |
| 腹囲              |                  |            | waist_circum_ference     | 数値   |       |    *     |          | cm                               |
| 尿糖              |                  |            | urinary_glucose          | 数値   |       |    *     |          | －、±、＋、2＋以上               |
| γ GTP            |                  |            | gamma_Gtp                | 数値   |       |    *     |          | IU/L                             |
|                   |                  |            | sleep                    |        |       |          |          |                                  |
|                   |                  | 状態       | state                    | 文字列 |       |          |    *     | *3                               |
|                   |                  | モーション | motion                   | 数値   |       |          |    *     | -1:計測なし、0:体動あり、 1:なし |
| 削除フラグ        |                  |            | deleted                  | 文字列 |   *   |    *     |    *     | *4                               |
| TTL               |                  |            | ttl                      | 数値   |       |    *     |    *     | *4                               |

*1ID

イベントに対して一意になるように設計されています。  
健康診断情報の登録ではユーザIDと受診日を使ってv3 UUIDを使って算出され、StreamAnalytics経由で登録されるデバイスのデータはetMetadataPropertyValueを使って設定されます。
* [GetMetadataPropertyValue (Azure Stream Analytics)](https://docs.microsoft.com/en-us/stream-analytics-query/getmetadatapropertyvalue)

*2プロダクトコードと信頼度

StreamAnalyticsの参照データを使ってマスター登録されています。  
信頼度は健康診断で取得したデータやデバイスのプロダクトコードに対して1～4の値が設定されるもので、より信頼できるデータの場合は高い値が設定されます。

*3睡眠の状態

メーカの機器から取得できる睡眠状態です。

|     値     |         状態          |
| ---------- | --------------------- |
| off        | 離床中                |
| sitting    | 端座位                |
| up         | 起き上がり            |
| wake       | 覚醒/体動（動き出し） |
| endmost    | 最短部寄              |
| lightSleep | 浅い睡眠              |
| sleep      | 睡眠（臥床）          |

*4削除フラグとTTL

CosmosDBに登録された健康診断情報はCosmosDB Triggerを使ってストレージにrawデータの保管を行います。  
削除された場合は、該当レコードにdeleted=trueが設定され、CosmosDBTriggerを使ってrawデータの削除を行います。  
一定期間後（TTL後）、CosmosDBのTTL機能によってレコードが削除されます。  
つまり削除フラグ（deleted）=trueで、TTLが設定されるレコードは通常残りません。 


