# CatchMind

SslSocketFactory와 SslRMISocketFactory로 구현한 캐치마인드 게임

# youtube
[![Video Label](http://img.youtube.com/vi/sayjV7Nh8t4/0.jpg)](https://www.youtube.com/watch?v=sayjV7Nh8t4)

## build

javac & java "10.0.1"

## compile
$ javac *.java -complie utf-8

## run

### 1. 서버에서 사용하는 keystore를 만든다.

$ keytool -genkeypair -alias duke -keyalg RSA -validity 7 -keystore CatchMindKey

### 2. keystore에서 증명서를 export 한다.

$ keytool -export -alias CatchMindcert -keystore CatchMindKey -rfc -file CatchMind.cer

### 3. 증명서를 이용해 truststore를 만든다.

$ keytool -import -alias CatchMindcert -file CatchMind.cer -keystore trustedcerts


keystore 는 src/server에, truststore는 src.client에 위치해야 함


### 서버 실행
$ java -Djavax.net.ssl.keyStore=server/CatchMindKey -Djavax.net.ssl.keyStorePassword=123456 server.Server

### 
$ java -Djavax.net.ssl.trustStore=client/trustedcerts -Djavax.net.ssl.trustStorePassword=123456 client.Client
