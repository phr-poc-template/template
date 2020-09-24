## EventHubsへのデータ送信手順

EventHubsへデータを送信するためのシミュレータです。

### 準備

1. Java11をインストールしてください。
1. Azure Portalで[EventHubs](https://portal.azure.com/?l=ja.ja-jp#blade/HubsExtension/BrowseResourceBlade/resourceType/Microsoft.EventHub%2Fnamespaces)を開き、送信先のEventHubs名前空間を開きます。
1. エンティティ→Event Hubsをクリックし、送信先のEventHubsのインスタンスを開きます。
1. 共有アクセスポリシー→SendOnlyPolicyを開き、[接続文字列 - 主キー]をコピーしておきます。

### シミュレータの動かし方

1. 環境変数 EVENTHUBS_CONNECT_STRINGに先ほどコピーした接続文字列を設定します。

    ```
    export EVENTHUBS_CONNECT_STRING=<EventHubsの接続文字列>
    ```

1. 体温計のデータを送信してみましょう。

    プロジェクトルートに移動してコマンドを実行します

    ```
    $ poc>gradlew simulator:services2evh:executeThermometer
    > Task :executeThermometer
    [2020-01-10 08:54:33,206] [main] [INFO ] [jp.co.tis.phr.simulator.evh.EventHubJob] - {"patient":"demo01","time":1578614072,"device":{"makerCode":"Maker-A","productCode":"Th
    ermometer","version":"1.0.0","serialId":"serialId","name":"Maker-A Thermometer"},"vitals":{"BT":{"type":"BT","unit":"℃","time":1578614072,"value":36.6}}}
    ・・・
    BUILD SUCCESSFUL in 5s
    3 actionable tasks: 2 executed, 1 up-to-date
    ```

    体温計の他に色々なデバイスのシミュレータを用意しています。

    |           デバイス           |                         コマンド                          |                      送信されるデータ                      |
    | ---------------------------- | --------------------------------------------------------- | ---------------------------------------------------------- |
    | 体温計                       | gradlew simulator:services2evh:executeThermometer         | 体温                                                       |
    | 血圧計                       | gradlew simulator:services2evh:executeSphygmomanometer    | 拡張期血圧、収縮期血圧                                     |
    | パルスオキシメーター         | gradlew simulator:services2evh:executePulseOximeter       | SpO2、脈拍                                                 |
    | 血糖値計                     | gradlew simulator:services2evh:executeBloodGlucoseMeter   | 血糖値                                                     |
    | 睡眠データスキャン用デバイス | gradlew simulator:services2evh:executeSleepDataScanDevice | 睡眠状態、呼吸、心拍                                       |
    | VoiceAssistant               | gradlew simulator:services2evh:executeVoiceAssistant      | 体温、血圧、SpO2、脈拍、血糖値、睡眠状態、身長、体重、呼吸 |

### オプション

環境変数 USER_NAMEでユーザ名を指定できます。

```
export USER_NAME=demo02
```

環境変数 CRON_EXPRESSIONを指定することで定期的にデータを送信できます。

例えば、10秒おきにデータを送信する場合は以下のように指定します。

```
set  CRON_EXPRESSION=*/10 * * * * ?
```

停止する場合はCtrl + Cで停止してください。
