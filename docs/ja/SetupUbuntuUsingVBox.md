# Ubuntu 18.04をVirtualBoxで用意する

## 概要

Ubuntu 18.04をVirtualBoxで用意する場合、以下の手順に従い準備してください。  
なお記載のバージョン以外では動作保証対象外です。  
手順は、Windowsにインストールする想定で記載されています。  
Macをお使いの場合は、適宜読み替えてください。

## 手順

1. VirtualBoxのインストール

    VirtualBox 6.1.2をデフォルト設定のままインストールしてください。

1. Vagrantのインストール

    Vagrant 2.2.7をデフォルト設定のままインストールしてください。

1. Ubuntu 18.04の作成

    1. Vagrant ProxyConfプラグインの導入

        プロキシ配下で環境を用意する場合は、以下のコマンドでVagrant ProxyConfプラグインを追加してください。

        ```cmd
        vagrant plugin install vagrant-proxyconf
        ```

    1. Ubuntu用のディレクトリ作成

        Vagrantが使用するディレクトリを以下のコマンドで作成してください。  
        作成するフォルダパスは任意に変更できますが、その場合は以降の手順にて適宜読み替えてください。

        ```cmd
        mkdir C:\dev\vagrant\ubuntu1804
        ```

    1. Vagrantfileの作成

        Vagrantfileというファイル名でC:\dev\vagrant\ubuntu1804配下に作成してください。

        - プロキシ配下の環境の場合

          ```cmd
          Vagrant.configure(2) do |config|
            config.vm.define "arm-vm" do |node|
              node.vm.box = "bento/ubuntu-18.04"
              node.vm.hostname = "arm-vm"
              node.vm.network :private_network, ip: "192.168.1.10"
            end
            config.vm.box_download_insecure = true
            config.ssh.insert_key = false
            config.vm.provider :virtualbox do |vb|
               vb.gui = false
               vb.memory = 512
            end
            if Vagrant.has_plugin?("vagrant-proxyconf")
              config.proxy.http = "http://<ホスト>:<ポート>/"
              config.proxy.https = "http://<ホスト>:<ポート>/"
              config.proxy.no_proxy = "localhost,127.0.0.1,192.168."
            end
          end
          ```

        - プロキシ配下**ではない**環境の場合

          ```cmd
          Vagrant.configure(2) do |config|
            config.vm.define "arm-vm" do |node|
              node.vm.box = "bento/ubuntu-18.04"
              node.vm.hostname = "arm-vm"
              node.vm.network :private_network, ip: "192.168.1.10"
            end
            config.vm.box_download_insecure = true
            config.ssh.insert_key = false
            config.vm.provider :virtualbox do |vb|
               vb.gui = false
               vb.memory = 512
            end
          end
          ```

## 作成した環境の操作

1. Ubuntuの起動

   Vagrantfileが置かれているディレクトリまで移動し、以下のコマンドで起動してください。

   ```cmd
   vagrant up
   ```

1. UbuntuへのSSH

    ```cmd
    ssh vagrant@192.168.1.10
    ```

    パスワードはvagrantです。

1. Ubuntuの停止

   Vagrantfileが置かれているディレクトリまで移動し、以下のコマンドで停止してください。
   
   ```cmd
   vagrant halt
   ```
  