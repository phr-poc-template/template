# サンプルアプリケーションを使った動作確認

## 概要

ここではEventHubs経由でデータを送信し、画面上でのグラフの表示をやってみましょう。

## 前提条件

* [デプロイ](DeployResources.md)が完了していること。
* EventHubsへのメトリクスの送信プロトコルはhttps通信ですが、2020年2月時点で「Azure sdk for Java」を使ってProxy経由の通信はサポートされていません。プロキシ経由でデプロイした方はプロキシがない環境で本手順をお試しください。

## 手順

{env}は、[デプロイ](DeployResources.md)手順で使った環境の識別子です。
下記の手順では読み替えてください。

### StreamAnalyticsの起動

1. AzurePortalでStream Analyticsを起動しておきます。

  * asa-phrpri-evh-{env}

### 動作確認

1. 1次サービスの画面にアクセスしてみます

   https://azapp-phrpri-{env}.azurewebsites.net

1. 「Register a new membership」をクリックして、ユーザを作成します。

   ユーザ名はdemo01としてください。

1. EventHubs経由でメトリクスを流し込んでみます。
   今回はdemo01というユーザに2週間分のデータを流し込んでみます。

   1. Java11をインストールしてください。
   1. Azure Portalで[EventHubs](https://portal.azure.com/?l=ja.ja-jp#blade/HubsExtension/BrowseResourceBlade/resourceType/Microsoft.EventHub%2Fnamespaces)を開き、送信先のEventHubs名前空間を開きます。
   1. エンティティ→Event Hubsをクリックし、送信先のEventHubsのインスタンスを開きます。
   1. SendOnlyPolicyを開き、接続文字列 - [接続文字列 – 主キー]をコピーしておきます。
   1. 環境変数 EVENTHUBS_CONNECT_STRINGにコピーした接続文字列を設定します。

      ```
      export EVENTHUBS_CONNECT_STRING=<EventHubsの接続文字列>
      ```

   1. データをEventhubsに流し込みます。
 
      ```
      gradlew simulator:services2evh:executeDemoDataCreator
      ```

 1. 1次サービスの画面で左メニューにある「ヘルスデータ」でグラフ表示ができていればサービスは正常に稼働しています。


