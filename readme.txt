자바 네트워크 프로그래밍 프로젝트 [ MindChat ]

------ [ 컴파일 ] ---------

1. key 생성 
  (1) 미리 생성해 놓은 키를 이용할 경우 
  ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  |  [폴더에 아래 파일이 있는지 확인 (폴더를 복사 후 옮기면 ./keystore 폴더와 SSLSocketServerKey.cert 파일, trustedcerts파일이 사라지는 경향이 있습니다.)] 
  | - MindChat/bin/.keystore/SSLSocektServerKey  
  | - MindChat/bin/.keystore/SSLSocektServerKey.cert
  | - MindChat/bin/SSLSocketServerKey.cert
  | - MindChat/bin/trustedcerts   
  | - 위 파일들이 없어졌다면 제출한 압축 파일을 다시 풀어 위 파일들만 복사 후 옮긴 폴더의 bin 폴더 안에 넣어줍니다.
  ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   - KEY import 하기  
     keytool -import -trustcacerts alias SSLSocketServer -file "C:\폴더위치\bin\.keystore\SSLSocketServerKey.cert" -keystore "C:\...\Java\jre1.8.0_20\lib\security\cacerts" -storepass changeit
     혹은 (안 될 경우)
     keytool -import -trustcacerts alias SSLSocketServer -file "C:\폴더위치\bin\.keystore\SSLSocketServerKey.cert" -keystore "C:\...\Java\jdk1.8.0_211\jre\lib\security\cacerts" -storepass changeit

  (2) 새로 키를 생성할 경우 (MindChat/bin 에서 작업) (비밀번호는 123456 으로 생성)
  - keytool -genkey -alias SSLSocketServer -keyalg RSA -v -storetype JKS -keystore .keystore\SSLSocketServerKey
  - keytool -export -keystore .keystore\SSLSocketServerKey -alias SSLSocketServer -file .keystore\SSLSocketServerKey.cert
  - copy .keystore\SSLSocketServerKey.cert .
  - keytool -import -keysotre trustedcerts -alias SSLSocketServer -file SSLSocketServerKey.cert 

  - KEY import 하기  
    keytool -import -trustcacerts alias SSLSocketServer -file "C:\폴더위치\bin\.keystore\SSLSocketServerKey.cert" -keystore "C:\...\Java\jre1.8.0_20\lib\security\cacerts" -storepass changeit
    혹은 (안 될 경우)
    keytool -import -trustcacerts alias SSLSocketServer -file "C:\폴더위치\bin\.keystore\SSLSocketServerKey.cert" -keystore "C:\...\Java\jdk1.8.0_211\jre\lib\security\cacerts" -storepass changeit

2. 컴파일 (MindChat/src 에서 작업)
   (1) ChatServer.java 파일을 열어 runroot의 코드를 수정 ["폴더위치/bin/"]
   (2) javac Game_func.java 
   (3) javac ChatServer.java
   (4) javac ChatClient.java
   (5) javac Game_Impl.java


------ [ 실행 ] ---------
[cmd 창을 4개 (클라이언트 2개) 또는 5개 (클라이언트 3개) 띄우기]
[★★★★★모든 실행은 (폴더위치/bin) 에서 해주세요.★★★★★]

1. start rmiregistry
2. java ChatServer 8000 
3. (남은 cmd 창 모두) java ChatClient localhost 8000 

4. 게임 진행
 - 각 클라이언트는 처음에 이름을 입력해야 합니다. (이름입력 - > 보내기버튼)
 - 각 클라이언트가 이름을 입력하면 채팅과 그림 기능([펜]버튼을 눌러야 그림이 그려집니다.) 을 이용할 수 있습니다.
 - [게임 시작] 버튼을 누르면 랜덤으로 문제 출제자가 선택 되어 출제자를 제외한 클라이언트는 그림을 그릴 수 없습니다.
 - 출제자는 정답을 그림을 그려 설명을 하고 나머지 유저들은 그림을 보고 정답을 유추합니다. 
 - 정답을 입력하고 [정답] 버튼을 누르면 정답인지 아닌지 확인 가능합니다.
 - 다시 [게임 시작] 버튼을 눌러 게임을 시작합니다.
 - 출제자는 [다른문제] 버튼과 [차례넘기기] 버튼을 이용할 수 있습니다.



@@ 감사합니다 @@
 
 
