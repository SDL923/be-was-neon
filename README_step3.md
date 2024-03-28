# be-was-2024 (step3)
코드스쿼드 백엔드 교육용 WAS 2024 개정판

## 👨‍💻구현 방법 
### 클래스 구조
```WebServer 클래스```  
```RequestHandler 클래스```  
```HttpRequest 클래스``` StartLine 정보 파싱, 회원가입 정보 파싱, 파일 타입 확인 기능을 하는 매소드 구현.  
```HttpResponseHeader 클래스``` response의 header 부분을 만들어 반환하는 매소드 구현.  
```HttpResponseBody 클래스``` response의 body 부분을 만들어 반환하는 매소드 구현.  
```ContentType enum 클래스``` 파일 타입별 content type을 저장한 enum.  

### static 파일 경로를 반환하는 기능
```HttpRequest 클래스  ```

```java
private void parseStartLine(String startLine){
    String[] splitStartLine = startLine.split(" ");
    this.method = splitStartLine[0];
    this.url = splitStartLine[1];
    this.version = splitStartLine[2];
}
```
parseStartLine 매소드로 request의 startLine을 파싱한다. 인덱스 1의 값이 url이다.

```java
public String getCompletePath(){
    StringBuilder completePath = new StringBuilder(BASIC_FILE_PATH);
    if(!url.contains(REGISTER_ACTION)){
        completePath.append(url);
    }
    File file = new File(completePath.toString());
    if(file.isDirectory()){ // file이 아니라 폴더이면 "/index.html" 추가
        completePath.append(INDEX_FILE_NAME);
    }
    return completePath.toString();
}
```
getCompletePath 매소드에서 url을 이용해 전체 파일 경로를 만든다.
- REGISTER_ACTION(/user/create)를 포함할 때는 url을 추가하지 않는다.
- 만든 경로가 파일이 아니라 폴더라면 INDEX_FILE_NAME(/index.html)을 붙여준다. 

### 회원가입 정보 받아오는 기능
```HttpRequest 클래스  ```
```java
private HashMap<String, String> registerUserData;

// GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1

HttpRequest(String startLine) {
    this.startLine = startLine;
    this.registerUserData = new HashMap<String, String>();
    parseStartLine(startLine);
}

// 회원가입 정보 파싱
public void parseRegisterData() {
    for(String token : extractUserData().split("[& ]")){
        String[] splitInfo = token.split("="); // 이름과 값을 = 로 분리
        try {
            registerUserData.put(splitInfo[0], URLDecoder.decode(splitInfo[1], "UTF-8")); // 해쉬 맵에 정보 저장
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
```
- 우선 "?"을 기준으로 split.
- 다음으로 "="을 기준 split 한 뒤 이름을 key 값을 value로 HashMap에 저장.

### 다양한 컨텐츠 타입 지원 기능
HttpResponseHeader 클래스의 content-type을 파일 타입에 맞는 content-type으로 설정해 준다.

