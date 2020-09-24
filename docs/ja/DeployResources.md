# AzureへのPHRリソースのデプロイ

## 概要

本PoCテンプレートでは、

- ARM(Azure Resource Manager)テンプレート
- Azure CLIコマンド

を使用してAzureへリソースをデプロイするツールを提供しています。
ツールを使用することで、自動で必要なリソースの作成とリソース間の接続設定を行うことができます。

## 前提条件

- デプロイを行うマシンのOSは、Ubuntu 18.04のみ動作保証しています。 それ以外のUbuntuのバージョンとOSは動作保証対象外です。

- デプロイを行うUbuntuには、以下がインストールされている必要があります。 明記されたバージョン以外は動作保証対象外です。


  |           対象            |    バージョン    |
  | ------------------------- | ---------------- |
  | Git                       | 指定なし         |
  | Open JDK                  | 11.0.2           |
  | Azure CLI                 | 2.0.80           |
  | Node.js                   | 12.x             |
  | npm                       | 6.x              |
  | jq                        | 指定なし         |
  | azure-function-core-tools | v3               |
  | .NET Core SDK             | 3.1              |

- デプロイを実行するAzureアカウントは、サブスクリプションの所有者でかつ、グローバル管理者である必要があります。

## 事前準備

1. Ubuntu 18.04の準備

    Windowsをお使いの場合は、WSLもしくは、VirtualBoxにてUbuntu 18.04をご用意ください。  
    Macをお使いの場合は、VirtualBoxにてUbuntu 18.04をご用意ください。

    VirtualBoxでUbuntu 18.04を用意する場合は、下記を参照ください。  
    [VirtualBoxでUbuntu 18.04を用意する](SetupUbuntuUsingVBox.md)

1. 必要なもののインストール

    前提条件記載の必要なものをUbuntu 18.04へインストールしてください。  

    1. Gitのインストール

        バージョンは問いません。  
        下記を参照してUbuntu 18.04を準備した場合は、すでにGitがインストールされているため、この手順は不要です。  
        [VirtualBoxでUbuntu 18.04を用意する](SetupUbuntuUsingVBox.md)

    1. Javaのインストール

       以下のコマンドにて、Javaをインストールしてください。

       ```cmd
       wget https://download.java.net/java/GA/jdk11/9/GPL/openjdk-11.0.2_linux-x64_bin.tar.gz -O /tmp/openjdk-11+28_linux-x64_bin.tar.gz
       sudo mkdir /usr/lib/jvm
       sudo tar xfvz /tmp/openjdk-11+28_linux-x64_bin.tar.gz --directory /usr/lib/jvm
       rm /tmp/openjdk-11+28_linux-x64_bin.tar.gz
       sudo update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk-11.0.2/bin/java 10
       sudo update-alternatives --config java
       ```

       sudo update-alternatives --config javaにてインストールしたopenjdk11を選択してください。  
       もし他のJavaがインストールされていない場合は、選択画面が表示されませんが問題ありません。

       インストールが完了したら、以下のコマンドで正しくインストールされたかを確認してください。

       ```cmd
       java -version
       ```

       以下のように表示されれば、正しくインストールができています。

       ```cmd
       openjdk version "11.0.2" 2019-01-15
       OpenJDK Runtime Environment 18.9 (build 11.0.2+9)
       OpenJDK 64-Bit Server VM 18.9 (build 11.0.2+9, mixed mode)
       ```

    1. Azure CLIのインストール

       以下のリンク記載の手動のインストール手順に従い、インストールしてください。

       [Azure CLIインストール手順](https://docs.microsoft.com/ja-jp/cli/azure/install-azure-cli-apt?view=azure-cli-latest)

       インストールが完了したら、以下のコマンドで正しくインストールされたかを確認してください。

       ```cmd
       az -v
       ```

       以下のように表示されれば、正しくインストールができています。

       ```cmd
       azure-cli                         2.0.80
       ```

    1. Node.js/npmのインストール

       以下のコマンドで、Node.jsとnpmをインストールしてください。
       Node.jsは最新版（LTS）をインストールします。

       ```cmd
       sudo apt install -y nodejs
       sudo apt install -y npm
       sudo npm install n -g
       sudo n stable
       sudo apt purge -y nodejs npm
       ```

       インストールが完了したら、以下のコマンドで正しくインストールされたかを確認してください。

       ```cmd
       node -v
       npm -v
       ```

       それぞれ、以下のように表示されれば、正しくインストールができています。  
       *Note: 以下のバージョンは現時点でのLTSのバージョンです。*

       ```cmd
       v12.14.1
       ```

       ```cmd
       6.13.4
       ```

    1. jqのインストール

        以下のコマンドで、jqをインストールしてください。

        ```cmd
        sudo apt install -y jq
        ```

       インストールが完了したら、以下のコマンドで正しくインストールされたかを確認してください。

       ```cmd
       jq --version
       ```

       以下のように表示されれば、正しくインストールができています。

       ```cmd
       jq-1.5-1-a5b5cbe
       ```

    1. Azure Function Core Toolsのインストール

        以下のコマンドで、Azure Function Core Toolsをインストールしてください。

        ```cmd
        wget -q https://packages.microsoft.com/config/ubuntu/18.04/packages-microsoft-prod.deb
        sudo dpkg -i packages-microsoft-prod.deb
        sudo apt-get update
        sudo apt install azure-functions-core-tools=2.7.2508-1
        ```

       インストールが完了したら、以下のコマンドで正しくインストールされたかを確認してください。

       ```cmd
       func -v
       ```

       以下のように表示されれば、正しくインストールができています。

       ```cmd
       3.0.2881
       ```

    1. .NET Core SDKのインストール

        以下のコマンドで、.NET Core SDKをインストールしてください。

        ```cmd
        sudo add-apt-repository universe
        sudo apt-get update
        sudo apt-get install -y apt-transport-https

        sudo apt-get update
        sudo apt-get install -y dotnet-sdk-3.1

        sudo apt-get update
        sudo apt-get install -y aspnetcore-runtime-3.1

        sudo apt-get update
        sudo apt-get install -y dotnet-runtime-3.1
        ```

       インストールが完了したら、以下のコマンドで正しくインストールされたかを確認してください。

       ```cmd
       dotnet --list-sdks
       ```

       以下のように表示されれば、正しくインストールができています。

       ```cmd
       3.1.402 [/usr/share/dotnet/sdk]
       ```

1. プロジェクトのクローン

    Ubuntu 18.04上に、このプロジェクトをGitからクローンしてください。  
    なお、以降の手順では以下のパスにクローンしたものとして記載します。

    ```cmd
    ~/repository/poc
    ```

1. Gradleのプロキシ設定

    Ubuntu 18.04がプロキシ配下にある場合、以下に従いGradleのプロキシ設定をしてください。

    ```cmd
    mkdir ~/.gradle
    touch ~/.gradle/gradle.properties
    ```

    gradle.propertiesには、以下の設定をしてください。

    ```cmd
    systemProp.http.proxyHost=<ホスト>
    systemProp.http.proxyPort=<ポート>
    systemProp.http.proxyUser=****
    systemProp.http.proxyPassword=****
    systemProp.https.proxyHost=<ホスト>
    systemProp.https.proxyPort=<ポート>
    systemProp.https.proxyUser=****
    systemProp.https.proxyPassword=****
    ```

## Primaryサービスのデプロイ

Primaryサービスのデプロイ手順は以下の通りです。

1. parameters.example.jsonを編集する
  
    infrastracture/arm/service/phr/pri/parameters.example.jsonのenvironment.valueを好きな文字に変更してください。  

    ```bash
    "environment": {
      "value": "tisiac"
    }
    ```

    [命名規則](ja/AzureNamingConvention.md)の{env}に相当します。
    グローバルで一意になる必要があるリソース（Storage Accountなど）があるため、必ず変更してください。
    また、Blobストレージ名に文字制限があるため6文字以下の英字小文字で指定してください。

1. Azure CLIでAzureにログインする

    以下のコマンドで、Azureにログインしてください。  
    ログインするユーザーは前提条件記載のアカウントとしてください。

    ```cmd
    az login
    ```

1. デプロイコマンドを実行する

    ```bash
    cd ~/repository/poc/infrastructure/arm/service/phr/pri
    ./deploy_resources_for_pri.azcli <プロジェクトルートパス> <Azureアカウント>
    ```

    <プロジェクトルートパス>は、Gitよりクローンしたプロジェクトまでの絶対パスを指定してください。  
    e.g.) ~/repository/poc

    <Azureアカウント>は、az loginしたアカウントを指定してください。  
    e.g.) tis@tis.co.jp  

## Secondaryサービスのデプロイ

1. parameters.example.jsonを編集する
  
    infrastracture/arm/service/phr/sec/parameters.example.jsonのenvironment.valueをpriで変更した文字と同じになるよう変更してください。

    ```bash
    "environment": {
      "value": "tisiac"
    }
    ```  

1. デプロイコマンドを実行する

    ```bash
    cd ~/repository/poc/infrastructure/arm/service/phr/sec
    ./deploy_resources_for_sec.azcli <プロジェクトルートパス> <Azureアカウント>
    ```

    <プロジェクトルートパス>は、Gitよりクローンしたプロジェクトまでの絶対パスを指定してください。  
    e.g.) ~/repository/poc

    <Azureアカウント>は、az loginしたアカウントを指定してください。  
    e.g.) tis@tis.co.jp  

## デプロイ後の状態について

- Stream Analytics

  リソースのデプロイ後は、Stream Analyticsが停止した状態となっています。  
  必要に応じて、Azure Portalなどから起動してください。

## Primary/Secondaryサービスのリソース削除

- Application Principal以外のリソースの削除

  Azure Portalのリソースグループ > 作成したリソースグループを選択してください。  
  削除したいリソースのチェックボックスをONにして、削除ボタンを押下してください。

- Application Principalの削除

  Client Credentials FlowとしてOAuth2での認証ができるように、Application Principalをデプロイコマンド内で作成しています。  
  削除したい場合は、Azure Active Directory > アプリの登録 > apl-phrpri-dataexchange-{env}を選択し、削除してください。
