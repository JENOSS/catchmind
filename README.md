# CatchMind

캐치 마인드 - JAVA Swing으로 구현한 Catch Mind 게임

# About
- SslSocketFactory와 SslRMISocketFactory 사용
- SSLSocketFactory를 사용한 SSL 소켓 프로그래밍으로, 채팅 및 그림 그리기 기능 제공
- Server에서 RMI를 이용하여 Object를 제공, Client에서 메소드 호출
- Swing을 이용하여 GUI 제공

# Video
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
