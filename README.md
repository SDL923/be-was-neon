# be-was-2024
코드스쿼드 백엔드 교육용 WAS 2024 개정판

# 학습 내용
## HTTP
### HTTP 통신
HTTP 통신은 **클라이언트**(Front-End)와 **서버**(Back-End)로 나뉘어진 구조.
클라이언트 즉, 사용자가 브라우저를 통해서 어떠한 서비스를 url을 통하거나 다른 것을 통해서 요청(request)을 하면 서버에서는 해당 요청사항에 맞는 결과를 찾아서 사용자에게 응답(response)하는 형태로 동작한다.

### 요청 메세지 Request (클라이언트 ➡️ 서버)
**시작 라인(Start Line)** 
- Method : GET, PUT, POST, PUSH, OPTIONS 등의 요청방식이 온다. 서버에게 요청의 종류를 알려주기 위해서 사용된다.
- 요청URL : 요청하는 자원의 위치를 명시한다.
- HTTP 프로토콜 버전 : 웹 브라우저가 사용하는 http 프로토콜의 버전이다.

**헤더(Header)** <br>
HTTP 전송에 필요한 모든 부가 정보를 담고 있다. (메세지 크기, 압축 여부, 인증, 브라우저 정보, 서버 정보, 캐시 ..등)

**공백 라인(Empty Line)** <br>
헤더와 바디를 구분하기 위한 라인

**요청 바디(Message Body)** <br>
실제 전송할 데이터 (HTML 문서, 이미지, 영상, JSON 등).
요청메소드가 POST나 PUT을 사용하게 됐을 떄 들어오게 된다. GET 방식은 요청할 때 가지고 가야 되는 자원도 URL에 포함되어 있기 때문에 GET방식은 요청바디가 없다.

### 응답 메세지 Response (서버 ➡️ 클라이언트 )
시작 라인(Start Line) <br>
- Version : 사용된 http 버전
- Status Code (상태 코드) : 클라이언트가 보낸 요청이 성공인지 실패인지 숫자 코드로 나타낸다.

**헤더(Header)** <br>
HTTP 전송에 필요한 모든 부가 정보를 담고 있다. (메세지 크기, 압축 여부, 인증, 브라우저 정보, 서버 정보, 캐시 ..등)

**공백 라인(Empty Line)** <br>
헤더와 바디를 구분하기 위한 라인

**바디(Message Body)** <br>
전송 받은 데이터

