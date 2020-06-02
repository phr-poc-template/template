## IoTHubへのデータ送信手順

IoTHubへデータを送信するためのシミュレータです。

### 準備

1. Java11をインストールしてください。
1. Azure Portalで[IoTHub](https://portal.azure.com/?l=ja.ja-jp#blade/HubsExtension/BrowseResource/resourceType/Microsoft.Devices%2FIotHubs)を開きます。
1. 左メニューIoTデバイスを開き、新規作成からデバイスを登録します。（名前は適当で問題ありません）
1. プライマリ接続文字列をコピーしておきます。

### シミュレータの動かし方

1. 環境変数 IOTHUB_CONNECT_STRINGに先ほどコピーしたプライマリ接続文字列を設定します。

```
export IOTHUB_CONNECT_STRING=<プライマリ接続文字列>
```

1. 血圧計を動かしてみましょう。

プロジェクトルートに移動してコマンドを実行します

```
$ poc>gradlew simulator:device2aih:executeSphygmomanometer
> Task :executeSphygmomanometer
[2020-01-10 09:58:27,784] [main] [INFO ] [jp.co.tis.phr.simulator.aih.IoTHubClient] - {"patient":"demo08","time":1578617907,"device":{"makerCode":"Maker-B","productCode":"Sphy
gmomanometer","version":"1.0.0","serialId":"serialId","name":"Maker-A Sphygmomanometer"},"vitals":{"BPH":{"type":"BPH","unit":"mmHg","time":1578617907,"value":121},"BPL":{"
type":"BPL","unit":"mmHg","time":1578617907,"value":82},"PR":{"type":"PR","unit":"bpm","time":1578617907,"value":54}}}
・・・
BUILD SUCCESSFUL in 4s
3 actionable tasks: 1 executed, 2 up-to-date
```

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




