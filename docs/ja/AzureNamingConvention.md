## Azureリソース命名規則

Azureリソースの命名規則を以下のようにしています。
尚、公式ドキュメントに名前付けの規則の記載がないリソースは独自に命名しています。

参考

* [Azure リソースの名前付け規則と制限事項 | Microsoft Docs](https://docs.microsoft.com/ja-jp/azure/architecture/best-practices/resource-naming)
* [準備完了: 推奨される名前付けおよびタグ付け規則](https://docs.microsoft.com/ja-jp/azure/cloud-adoption-framework/ready/azure-best-practices/naming-and-tagging)


サービス名称

* 1次サービス
    * phrpri
* 2次サービス
    * phrsec

環境{env}

* 自分の環境を表す識別子となります。
* リソースの命名規約上6文字以内で設定してください。


1次サービス

|        Azureリソース        |    Scope     |              ルール              |                備考                 |
| --------------------------- | ------------ | -------------------------------- | ----------------------------------- |
| ResourceGroup               | アカウント   | rg-phrpri-{env}                  |                                     |
|                             |              | rg-phrpri-azapp-{env}            | azapp用のResource Group             |
| IoTHub                      | グローバル   | aih-phrpri-{env}                 | XXXXXX.azure-devices.net            |
| EventHubs                   | グローバル   | evh-phrpri-{env}                 | エンティティ：default               |
| StreamAnalytics             | アカウント   | asa-phrpri-evh-{env}             | EventHubs→CosmosDB,Blob             |
|                             |              | asa-phrpri-aih-{env}             | IoTHub→CosmosDB, Blob               |
| CosmosDB                    | アカウント   | cosdb-phrpri-{env}               |                                     |
| CosmosDB(Container)         | データベース | leases                           | CosmosDBTriggerで使用（自動生成）   |
|                             |              | user                             |                                     |
|                             |              | user_record                      |                                     |
| BlobStorage                 | グローバル   | stphrpriasarefdata{env}          | StreamAnalyticsで利用する参照データ |
|                             |              | stphrpriraw{data}                | Rawデータ                           |
| Functions                   | グローバル   | azfun-phrpri-cosdb-trigger-{env} | CosmosDB Trigger                    |
|                             |              | azfun-phrpri-dataexchange-{env}  | REST API                            |
| APIManagement               | グローバル   | api-phrpri-dataexchange-{env}    | https://XXXXXX.azure-api.net        |
| AzureADアプリケーション     | アカウント   | apl-phrpri-dataexchange-{env}    | アプリ登録                          |
| App Service                 | グローバル   | azapp-phrpri-{env}               | https://XXXXXX.azurewebsites.net    |
| KeyVault                    | グローバル   | kv-phrpri-{env}                  | https://XXXXXX.vault.azure.net/     |
| ACR                         | グローバル   | acrphrpri{env}                   | XXXX.azurecr.io                     |
| Log Analyticsワークスペース | グローバル   | log-phrpri-{env}                 |                                     |

2次サービス

|    Azureリソース    |    Scope     |              ルール              |                備考                 |
| ------------------- | ------------ | -------------------------------- | ----------------------------------- |
| ResourceGroup       | アカウント   | rg-phrsec-{env}                  |                                     |
| ServiceBus          | グローバル   | sb-phrsec-{env}                  | キュー：sbq-dataexchange            |
| CosmosDB            | アカウント   | cosdb-phrsec-{env}               |                                     |
| CosmosDB(Container) | データベース | user                             |                                     |
|                     |              | user_record                      |                                     |
|                     |              | leases                           | CosmosDBTriggerで使用（自動生成）   |
| BlobStorage         | グローバル   | stphrsecasarefdata{env}          | StreamAnalyticsで利用する参照データ |
|                     |              | stphrsecraw{data}                | Rawデータ                           |
| Functions           | グローバル   | azfun-phrsec-dataexchange-{env}  | TimerTrigger                        |
|                     |              | azfun-phrsec-sbq-trigger-{env}   | ServiceBus Queue Trigger            |
|                     |              | azfun-phrsec-cosdb-trigger-{env} | CosmosDB Trigger                    |
| KeyVault            | グローバル   | kv-phrsec-{env}                  | https://XXXX.vault.azure.net/       |

