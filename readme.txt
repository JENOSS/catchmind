�ڹ� ��Ʈ��ũ ���α׷��� ������Ʈ [ MindChat ]

------ [ ������ ] ---------

1. key ���� 
  (1) �̸� ������ ���� Ű�� �̿��� ��� 
  ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  |  [������ �Ʒ� ������ �ִ��� Ȯ�� (������ ���� �� �ű�� ./keystore ������ SSLSocketServerKey.cert ����, trustedcerts������ ������� ������ �ֽ��ϴ�.)] 
  | - MindChat/bin/.keystore/SSLSocektServerKey  
  | - MindChat/bin/.keystore/SSLSocektServerKey.cert
  | - MindChat/bin/SSLSocketServerKey.cert
  | - MindChat/bin/trustedcerts   
  | - �� ���ϵ��� �������ٸ� ������ ���� ������ �ٽ� Ǯ�� �� ���ϵ鸸 ���� �� �ű� ������ bin ���� �ȿ� �־��ݴϴ�.
  ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   - KEY import �ϱ�  
     keytool -import -trustcacerts alias SSLSocketServer -file "C:\������ġ\bin\.keystore\SSLSocketServerKey.cert" -keystore "C:\...\Java\jre1.8.0_20\lib\security\cacerts" -storepass changeit
     Ȥ�� (�� �� ���)
     keytool -import -trustcacerts alias SSLSocketServer -file "C:\������ġ\bin\.keystore\SSLSocketServerKey.cert" -keystore "C:\...\Java\jdk1.8.0_211\jre\lib\security\cacerts" -storepass changeit

  (2) ���� Ű�� ������ ��� (MindChat/bin ���� �۾�) (��й�ȣ�� 123456 ���� ����)
  - keytool -genkey -alias SSLSocketServer -keyalg RSA -v -storetype JKS -keystore .keystore\SSLSocketServerKey
  - keytool -export -keystore .keystore\SSLSocketServerKey -alias SSLSocketServer -file .keystore\SSLSocketServerKey.cert
  - copy .keystore\SSLSocketServerKey.cert .
  - keytool -import -keysotre trustedcerts -alias SSLSocketServer -file SSLSocketServerKey.cert 

  - KEY import �ϱ�  
    keytool -import -trustcacerts alias SSLSocketServer -file "C:\������ġ\bin\.keystore\SSLSocketServerKey.cert" -keystore "C:\...\Java\jre1.8.0_20\lib\security\cacerts" -storepass changeit
    Ȥ�� (�� �� ���)
    keytool -import -trustcacerts alias SSLSocketServer -file "C:\������ġ\bin\.keystore\SSLSocketServerKey.cert" -keystore "C:\...\Java\jdk1.8.0_211\jre\lib\security\cacerts" -storepass changeit

2. ������ (MindChat/src ���� �۾�)
   (1) ChatServer.java ������ ���� runroot�� �ڵ带 ���� ["������ġ/bin/"]
   (2) javac Game_func.java 
   (3) javac ChatServer.java
   (4) javac ChatClient.java
   (5) javac Game_Impl.java


------ [ ���� ] ---------
[cmd â�� 4�� (Ŭ���̾�Ʈ 2��) �Ǵ� 5�� (Ŭ���̾�Ʈ 3��) ����]
[�ڡڡڡڡڸ�� ������ (������ġ/bin) ���� ���ּ���.�ڡڡڡڡ�]

1. start rmiregistry
2. java ChatServer 8000 
3. (���� cmd â ���) java ChatClient localhost 8000 

4. ���� ����
 - �� Ŭ���̾�Ʈ�� ó���� �̸��� �Է��ؾ� �մϴ�. (�̸��Է� - > �������ư)
 - �� Ŭ���̾�Ʈ�� �̸��� �Է��ϸ� ä�ð� �׸� ���([��]��ư�� ������ �׸��� �׷����ϴ�.) �� �̿��� �� �ֽ��ϴ�.
 - [���� ����] ��ư�� ������ �������� ���� �����ڰ� ���� �Ǿ� �����ڸ� ������ Ŭ���̾�Ʈ�� �׸��� �׸� �� �����ϴ�.
 - �����ڴ� ������ �׸��� �׷� ������ �ϰ� ������ �������� �׸��� ���� ������ �����մϴ�. 
 - ������ �Է��ϰ� [����] ��ư�� ������ �������� �ƴ��� Ȯ�� �����մϴ�.
 - �ٽ� [���� ����] ��ư�� ���� ������ �����մϴ�.
 - �����ڴ� [�ٸ�����] ��ư�� [���ʳѱ��] ��ư�� �̿��� �� �ֽ��ϴ�.



@@ �����մϴ� @@
 
 
