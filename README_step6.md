be-was-2024 (step6)
=== 
코드스쿼드 백엔드 교육용 WAS 2024 개정판

# 👨‍💻구현 방법
## 클래스 구조
<img>

## manager 패키지 구현
### RequestManager 인터페이스
```java
public interface RequestManager {
    default void fillResponse(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if(httpRequest.isMethodGet()){
            manageGet(httpRequest, httpResponse);
        }
        if(httpRequest.isMethodPost()){
            managePost(httpRequest, httpResponse);
        }
    }

    void manageGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;

    void managePost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;
}
```
default 매소드인 fillResponse는 상황에 맞게 httpResponse를 채워준다. request method(GET/POST)에 맞는 추상 매소드를 실행한다. 

### RegisterManager 클래스
url이 "/register"일때 처리하는 Manager (implements RequestManager)

- **manageGet**  
회원가입 페이지 보여주기
  1) ```
     start line: HTTP/1.1 200 OK  
     headers: context-type, content-length  
     body: /register 폴더에 있는 파일 읽기 
     ```

- **managePost**  
회원가입 후 login 페이지로 redirect  
  1) User 객체 생성 후 db에 저장
  2) login 페이지로 redirect
     ``` 
     start line: HTTP/1.1 302 FOUND  
     headers: location("/login") 
     body: X 
     ```

### LoginManager 클래스
- **manageGet**  
로그인 페이지 보여주기
  1) ```
     start line: HTTP/1.1 200 OK  
     headers: context-type, content-length  
     body: /register 폴더에 있는 파일 읽기 
     ```
  
- **managePost**  
  - 로그인 성공   
    1) cookie sid 값 랜덤 생성 후 Session db에 저장
    2) myPage로 redirect
        ``` 
       start line: HTTP/1.1 302 FOUND  
       headers: location("/myPage"), Set-Cookie(sid=??????) 
       body: X 
       ```

  - 로그인 실패
    1) 로그인 실패 페이지로 redirect
       ``` 
       start line: HTTP/1.1 302 FOUND  
       headers: location("/login/fail") 
       body: X 
       ```
       
### LogoutManager 클래스
- **manageGet**
  1) Sesson db에서 현재 request의 cookie(sid)값 삭제
  2) 초기 페이지로 redirect  
     ``` 
      start line: HTTP/1.1 302 FOUND  
      headers: location("/") 
      body: X 
     ```

- **managePost**
    1) bad request
       ``` 
       start line: HTTP/1.1 404 Not Found  
       headers: context-type, content-length 
       body: <h1>404 Not Found</h1>
       ```

### MyPageManager 클래스
- **manageGet**  
    - 로그인 상태이면
        1) html 파일에 user name 추가 
        2) myPage 보여주기
            ``` 
            start line: HTTP/1.1 200 OK  
            headers: context-type, content-length  
            body: /myPage 폴더에 있는 파일 + name
            ```

    - 로그인 상태가 아니면
        1) 로그인 페이지로 redirect
           ``` 
           start line: HTTP/1.1 302 FOUND  
           headers: location("/login") 
           body: X 
           ```

- **managePost**
    1) bad request
       ``` 
       start line: HTTP/1.1 404 Not Found  
       headers: context-type, content-length 
       body: <h1>404 Not Found</h1>
       ```

### UserListManager 클래스
- **manageGet**
    - 로그인 상태이면
        1) 동적으로 user 목록 html 만들기
        2) 동적으로 만든 페이지 보여주기
            ``` 
            start line: HTTP/1.1 200 OK  
            headers: context-type, content-length  
            body: 동적으로 만든 user 목록 html
            ```

    - 로그인 상태가 아니면
        1) 로그인 페이지로 redirect
           ``` 
           start line: HTTP/1.1 302 FOUND  
           headers: location("/login") 
           body: X 
           ```

- **managePost**
    1) bad request
       ``` 
       start line: HTTP/1.1 404 Not Found  
       headers: context-type, content-length 
       body: <h1>404 Not Found</h1>
       ```

### DefaultManager 클래스
- **manageGet**
    1) 주어진 경로의 파일 보여주기
        ```
         start line: HTTP/1.1 200 OK  
         headers: context-type, content-length  
         body: 주어진 파일 읽기
         ```

- **managePost**  
    1) bad request
       ``` 
       start line: HTTP/1.1 404 Not Found  
       headers: context-type, content-length 
       body: <h1>404 Not Found</h1>
       ```
  


