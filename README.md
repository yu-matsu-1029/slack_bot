# SlackBot
## 概要
ライブラリSlackletを使ったSlackBot

## 説明
現在時刻の表示とクイズを出題してくれるSlackBotです。

## インストール
```
$ git clone https://github.com/yu-matsu-1029/slack_bot
```
src/main/java直下にcredentials.propertiesというファイルを作り、以下のように取得したapi tokenをセットします

```
slack.bot_api_token=xoxb-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```
src/main/java直下にdbconnector.propertiesというファイルを作り、以下のように取得したurl,user,passwordをセットします

```
URL=xxxxxxxxxxxxxxxxxxxx
USER=xxxxxxxxxxxxx
PASSWORD=xxxxxxxxxx
```

### コマンド一覧

```
時間　　:　現在時刻の取得
クイズ　:　クイズを開始
```
