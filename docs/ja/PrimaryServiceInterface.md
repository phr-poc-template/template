## 一次サービス連携インタフェース仕様

### インタフェース仕様

[経済産業省：健康情報等交換規約定義書](https://www.meti.go.jp/committee/kenkyukai/shoujo/jisedai_healthcare/kenkou_toushi_wg/kenkou_iryou_joho/pdf/h28_s02_00_01.pdf)に認可における記載がなかったため、[FHIR SMART Backend Services: Authorization Guide](https://hl7.org/fhir/uv/bulkdata/authorization/index.html)を参考に実装を行いました。

具体的には、OAuth2.0 Authorization Frameworkに既定されている[4.4.  Client Credentials Grant](https://tools.ietf.org/html/rfc6749#page-40)を利用しています。  
2次サービスのキーコンテナーに格納されたClient Credentialを使ってAccessTokenを取得し、AccessTokenを使ったAPIアクセスをAzureのサービスを使って実装しました。

* [RFC6749, The OAuth 2.0 Authorization Framework](https://tools.ietf.org/html/rfc6749)

### 項目定義

 インタフェース項目については、[経済産業省：健康情報等交換規約定義書](https://www.meti.go.jp/committee/kenkyukai/shoujo/jisedai_healthcare/kenkou_toushi_wg/kenkou_iryou_joho/pdf/h28_s02_00_01.pdf)に記載されている「別紙1健康情報等交換規約データ項目定義一覧 (1) 計測データ項目定義」を参考に実装されています。
  
 データのフォーマットは「P7. 文字コード」に記載されている通りJSONであり、文字コードはUTF-8です。  
  
1次サービスにおいてデバイス経由で取得したデータは、健康情報（healthcare_information）としてマッピングし、健康診断データは検査情報（health_checkup_information）にマッピングされています。尚、コンソーシアムIDなど本サンプルにない概念については固定値が設定されています。（備考欄に（固定）)   


|      項目名      |          |                 |                    |         物理名（英字）          |   型   |  桁  | 必須  |            備考            |
| ---------------- | -------- | --------------- | ------------------ | ------------------------------- | :----: | ---: | :---: | -------------------------- |
| バージョン       |          |                 |                    | format_version                  | 文字列 |    3 |   *   | 固定                       |
| 送信日時         |          |                 |                    | send_time                       |  日付  |   17 |   *   | yyyyMMddHHmmssSSS          |
| コンソーシアムID |          |                 |                    | consortium_id                   | 文字列 |    6 |   *   | 半角英数字                 |
| 個人情報         |          |                 |                    | person_information              |  配列  |      |   *   |                            |
|                  | 個人ID   |                 |                    | person_id                       | 文字列 |   20 |   *   | 半角英数字                 |
|                  | 健康情報 |                 |                    | healthcare_information          |  配列  |      |       |                            |
|                  |          | 体重            |                    | weighing_scale                  |  配列  |      |   *   |                            |
|                  |          |                 | 計測日時           | ovservation_time                |  日付  |   14 |   *   | yyyyMMddHHmmss             |
|                  |          |                 | 体重               | body_weight                     |  数値  |    6 |   *   | kg                         |
|                  |          |                 | 体脂肪率           | body_fat                        |  数値  |    5 |       | %                          |
|                  |          |                 | 機器メーカーコード | model_manufacturer_code         | 文字列 |   50 |   *   |                            |
|                  |          |                 | 機器型番           | model_number                    | 文字列 |   50 |       |                            |
|                  |          |                 | 機器識別ID         | production_specification_serial | 文字列 |   50 |   *   |                            |
|                  |          |                 | データ入力コード   | input_method_code               | 文字列 |    1 |   *   | 0:システム入力（固定）     |
|                  |          |                 | 時刻補正コード     | origin_of_time_code             | 文字列 |    1 |   *   | 0:機器の時刻を使用（固定） |
|                  |          |                 | 更新日時           | update_time                     |  日付  |   17 |   *   | yyyyMMddHHmmssSSS          |
|                  |          | 家庭血圧        |                    | blood_pressure                  |  配列  |      |       |                            |
|                  |          |                 | 計測日時           | ovservation_time                |  日付  |   14 |   *   | yyyyMMddHHmmss             |
|                  |          |                 | 収縮期血圧測定値   | systolic                        |  数値  |    3 |   *   | mmHg                       |
|                  |          |                 | 拡張期血圧測定値   | diastolic                       |  数値  |    3 |   *   | mmHg                       |
|                  |          |                 | 脈拍               | pulse_rate                      |  数値  |    3 |   *   | bpm（/分）                 |
|                  |          |                 | 測定箇所           | measurement_position            | 文字列 |  200 |       |                            |
|                  |          |                 | 機器メーカーコード | model_manufacturer_code         | 文字列 |   50 |   *   |                            |
|                  |          |                 | 機器型番           | model_number                    | 文字列 |   50 |       |                            |
|                  |          |                 | 機器識別ID         | production_specification_serial | 文字列 |   50 |   *   |                            |
|                  |          |                 | データ入力コード   | input_method_code               | 文字列 |    1 |   *   | 0:システム入力（固定）     |
|                  |          |                 | 時刻補正コード     | origin_of_time_code             | 文字列 |    1 |   *   | 0:機器の時刻を使用（固定） |
|                  |          |                 | 更新日時           | update_time                     |  日付  |   17 |   *   | yyyyMMddHHmmssSSS          |
|                  | 検査情報 |                 |                    | health_checkup_information      |  配列  |      |       |                            |
|                  |          | HbA1c           |                    | hba1c                           |  配列  |      |       |                            |
|                  |          |                 | 計測日時           | ovservation_time                |  日付  |   14 |   *   | yyyyMMddHHmmss             |
|                  |          |                 | HbA1c              | hba1c_value                     |  数値  |    4 |   *   | %                          |
|                  |          |                 | データ入力コード   | input_method_code               | 文字列 |    1 |   *   | 0:システム入力（固定）     |
|                  |          |                 | 測定法コード       | measurement_method_code         | 文字列 |    6 |       | 000001（固定）             |
|                  |          |                 | 参考値コード       | value_criteria_code             | 文字列 |    1 |       | 0:正常値（固定）           |
|                  |          |                 | 検査機関ID         | test_lab_id                     | 文字列 |   12 |   *   | 000000000001（固定）       |
|                  |          |                 | 更新日時           | update_time                     |  日付  |   17 |   *   | yyyyMMddHHmmssSSS          |
|                  |          | 健診/診察室血圧 |                    | blood_pressure                  |  配列  |      |       |                            |
|                  |          |                 | 計測日時           | ovservation_time                |  日付  |   14 |   *   | yyyyMMddHHmmss             |
|                  |          |                 | 収縮期血圧測定値   | systolic                        |  数値  |    3 |   *   | mmHg                       |
|                  |          |                 | 拡張期血圧測定値   | diastolic                       |  数値  |    3 |   *   | mmHg                       |
|                  |          |                 | 脈拍               | pulse_rate                      |  数値  |    3 |   *   | bpm（/分）                 |
|                  |          |                 | 測定箇所           | measurement_position            | 文字列 |  200 |       |                            |
|                  |          |                 | データ入力コード   | input_method_code               | 文字列 |    1 |   *   | 0:システム入力（固定）     |
|                  |          |                 | 検査機関ID         | test_lab_id                     | 文字列 |   12 |   *   | 000000000001（固定）       |
|                  |          |                 | 更新日時           | update_time                     |  日付  |   17 |   *   | yyyyMMddHHmmssSSS          |

### 制限事項

* [経済産業省：健康情報等交換規約定義書](https://www.meti.go.jp/committee/kenkyukai/shoujo/jisedai_healthcare/kenkou_toushi_wg/kenkou_iryou_joho/pdf/h28_s02_00_01.pdf)には活動量計の項目が定義されているが、本サンプルでは活動量計のサンプルが含まれていないためインタフェース項目からも削除されています。
* インタフェースで返されるレコード数は100を上限にしています。