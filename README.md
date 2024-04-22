# TripFriend 트립친
>테킷 앱스쿨 : AOS 1기 최종 프로젝트<br/>
>2023.09.01 ~ 2023.09.25

![Group 26086575](https://github.com/APPSCHOOL2-Android/FinalProject-TripFriend/assets/106245278/a633a6b9-e5a4-4309-96da-5b7f260c640f)

<br/>

## 🎖️ 수상 내역
<img src="https://github.com/APPSCHOOL2-Android/FinalProject-TripFriend/assets/106245278/726fd761-7ccd-4664-a959-f7aa55701f34" width = 500>
<br/>

## 🌟 주요 기능
#### 👀 시연 영상
<a href="https://youtu.be/JOU5Lf0hqXs"><img src="https://img.shields.io/badge/YouTube-FF0000?style=for-the-badge&logo=youtube&logoColor=white">
<br/><br/>
#### 로그인 및 회원가입
- Firebase Authentication을 통한 이메일 인증 지원
- 카카오, 네이버 API를 이용한 소셜 로그인 지원
<table>
    <tr>
        <th><code>이메일 회원가입</code></th>
        <th><code>카카오 로그인</code></th>
        <th><code>네이버 로그인</code></th>
    </tr>
    <tr>
        <td><img src="https://github.com/APPSCHOOL2-Android/FinalProject-TripFriend/assets/106245278/375dfa69-fb5e-4528-a62c-500d4774e95f" width=200></td>
        <td><img src="https://github.com/APPSCHOOL2-Android/FinalProject-TripFriend/assets/106245278/162d6c2a-e5fd-47ed-aa75-631ea174a1f0" width=200 height=400></td>
        <td><img src="https://github.com/APPSCHOOL2-Android/FinalProject-TripFriend/assets/106245278/3842aa73-d2d1-4730-90cb-33ddd9d31c38" width=200 height=400></td>
    </tr>
</table>

#### 지도
- Google Map API의 Places SDK를 이용해 원하는 장소 검색 시 자동완성 검색어 제공
- 등록된 장소 목록을 마커로 표시
<table>
    <tr>
        <th><code>장소 등록</code></th>
        <th><code>장소 목록</code></th>
    </tr>
    <tr>
        <td><img src="https://github.com/APPSCHOOL2-Android/FinalProject-TripFriend/assets/106245278/e0a907d6-f70e-4cac-bc51-df9a5af1ec46" width=200 height=400></td>
        <td><img src="https://github.com/APPSCHOOL2-Android/FinalProject-TripFriend/assets/106245278/797c446b-92bc-4ee6-9fad-2063a02b6e96" width=200 height=400></td>
    </tr>
</table>

#### 채팅
- Firebase Firestore Database를 이용한 실시간 채팅 구현
- 1:1 채팅 및 그룹 채팅 지원
<table>
    <tr>
        <th><code>채팅 홈</code></th>
        <th><code>그룹 채팅</code></th>
    </tr>
    <tr>
        <td><img src="https://github.com/APPSCHOOL2-Android/FinalProject-TripFriend/assets/106245278/c6c1ca44-f6ff-4fe6-ba62-96dd5fb69032" width=200 height=400></td>
        <td><img src="https://github.com/APPSCHOOL2-Android/FinalProject-TripFriend/assets/106245278/077d2bd1-dd4b-41dd-bb11-bf2e805c3274" width=200 height=400></td>
    </tr>
</table>

#### 알림
- Firebase Cloud Messaging을 이용해 특정 사용자(토큰)에게 알림 전송
- 동행 요청 후 상대방의 수락 또는 거절 여부를 표시
<table>
    <tr>
        <th><code>알림 센터</code></th>
    </tr>
    <tr>
        <td><img src="https://github.com/APPSCHOOL2-Android/FinalProject-TripFriend/assets/106245278/05e3f6da-e66a-4246-9230-e44624acd060" width=200 height=400></td>
    </tr>
</table>

#### 게시글 필터
- 여행 카테고리 및 성별, 날짜 필터 제공
- 게시글 제목, 내용, 해시태그 검색 제공
<table>
    <tr>
        <th><code>게시글 필터</code></th>
    </tr>
    <tr>
        <td><img src="https://github.com/APPSCHOOL2-Android/FinalProject-TripFriend/assets/106245278/174c275d-2aa4-4451-8173-542a493918f6" width=200 height=400></td>
    </tr>
</table>

#### 동행글 CRUD
- Firebase Firestore Database를 이용한 동행글 정보 관리
- 동행글 등록, 보기, 수정, 삭제 제공
<table>
    <tr>
        <th><code>동행글 등록</code></th>
        <th><code>동행글 보기(상세)</code></th>
    </tr>
    <tr>
        <td><img src="https://github.com/APPSCHOOL2-Android/FinalProject-TripFriend/assets/106245278/13d53179-8a79-42e6-bd4e-24741325b01b" width=200 height=400></td>
        <td><img src="https://github.com/APPSCHOOL2-Android/FinalProject-TripFriend/assets/106245278/62cec8d9-3730-473d-a424-c6f6b4eb4d98" width=200 height=400></td>
    </tr>
</table>

<br/>

## 📌 트러블 슈팅
### 1. 채팅 메시지 갱신 이슈
<code>문제 상황</code><br>
채팅 홈 화면에서 채팅방 목록 Recycler View에 최신 채팅 내용이 갱신되지 않는 문제 발생<br/><br/>
<code>원인 추론</code><br>
채팅 Fragment 하위 View Pager에 포함된 Recycler View가 부모 Fragment 생명주기에 종속되어 있기 때문에 하위 Fragment 생명주기에서 Recycler View를 갱신해도 변동이 없는 것으로 예상<br/><br/>
<code>해결 방안</code><br>
1. 실시간 업데이트 수신대기 리스너를 Recycler View의 아이템마다 부여<br>
2. View Pager 내부에 Fragment Container를 달아서 하위 Fragment를 전환하는 방식으로 변경<br>
→ 2안을 채택할 경우 방대한 양의 코드를 수정해야 했기 때문에 1안으로 진행<br/>

<code>해결 과정</code><br>
실시간 업데이트 수신대기 리스너를 RecyclerView 아이템마다 부여하여 아이템 데이터에 변동이 생기면 Recycler View에 데이터 갱신 명령<br/>
→ 채팅 홈 화면에서 채팅방 별 최신 상태 반영됨
<br/><br/>
### 2. 지도 스크롤 이슈
<code>문제 상황</code><br>
메인 홈 화면에서 Tab Layout과 View Pager를 함께 사용하여 내부 Fragment에 지도를 띄울 때 포커스를 이동하기 위해 좌우 스크롤 시 지도 대신 Tab Layout이 전환되고, Map View가 Scroll View의 하위에 위치하여 지도가 아닌 프레임 자체가 움직이는 문제 발생<br/><br/>
<code>원인 추론</code><br>
화면 스크롤 시 지도가 담긴 Fragment 전체가 슬라이드 되어 하위 Map View가 움직이지 않는 것으로 예상<br/><br/>
<code>해결 과정</code><br>
View Pager를 제거하고 Tab Layout만 사용해 Fragment를 이동하는 구조로 변경하고, Scroll View와 Map View를 분리함<br/>
→ 지도 스크롤 시 프레임이 움직이거나 탭이 전환되지 않고 정상적으로 지도 시점이 옮겨짐

<br/>

## 💻 기술 스택
<img src="https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white"><img src="https://img.shields.io/badge/Android_Studio-3DDC84?style=for-the-badge&logo=android-studio&logoColor=white"><img src="https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=Firebase&logoColor=white"><img src="https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white"><img src="https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white">
#### 개발 환경
- Windows 10, Android Studio 8.1(Oreo), jdk 1.8
#### 협업 도구
- Figma, Github, Notion, Discord
#### 기술 스택
- Language : Kotlin
- Architecture : MVVM
- BackEnd : Firebase(Firestore Database, Cloud Messaging, Authentication)
- API : Google Map, Kakao Login, Naver Login
- Library : Retrofit, Glide, Coroutine

<br/>

## 👩‍💻 팀원 소개
- 2조 - E중IN격
<table>
    <tr align="center">
        <td><B>이지은(리더)<B></td>
        <td><B>강현구(부리더)<B></td>
        <td><B>장용진<B></td>
        <td><B>김민우<B></td>
        <td><B>유동호<B></td>  
    </tr>
    <tr align="center">
        <td>
          <img src="https://github.com/nueijeel.png?size=120">
            <br>
            <a href="https://github.com/nueijeel"><I>nueijeel</I></a>
        </td>
        <td>
            <img src="https://github.com/Ganghyungoo.png?size=120">
            <br>
            <a href="https://github.com/Ganghyungoo"><I>Ganghyungoo</I></a>
        </td>
        <td>
            <img src="https://github.com/YonjjinJang.png?size=120">
            <br>
            <a href="https://github.com/YonjjinJang"><I>YonjjinJang</I></a>
        </td>
        <td>
            <img src="https://github.com/DoReMinWoo.png?size=120">
            <br>
            <a href="https://github.com/DoReMinWoo"><I>DoReMinWoo</I></a>
        </td>
        <td>
            <img src="https://picsum.photos/120/120">
            <br>
            <a href="https://github.com/y-d-h"><I>y-d-h</I></a>
        </td>
    </tr>
    <tr>
        <td>
            <p>내 정보, 설정, 동행요청 목록 화면 기능 담당 및 동행요청 푸시알림 구현</p>
        </td>
        <td>
            <p>동행 리뷰, 채팅 화면 기능 담당</p>
        </td>
        <td>
            <p>회원가입, 소셜 로그인 화면 기능 담당 및 버그 보완</p>
        </td>
        <td>
            <p>메인 홈, 동행글 상세, 동행글 수정, 찜 목록 화면 기능 담당</p>
        </td>
        <td>
            <p>동행글 목록, 동행글 등록 화면 기능 담당 및 검색 기능 구현</p>
        </td>
    </tr>
</table>
