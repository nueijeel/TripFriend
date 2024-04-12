# TripFriend 트립친
>테킷 앱스쿨 : AOS 1기 최종 프로젝트<br/>
>2023.09.01 ~ 2023.09.25

![Group 26086573](https://github.com/APPSCHOOL2-Android/FinalProject-TripFriend/assets/106245278/75c6c0b4-c669-45ac-95cd-d286c5895dce)

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
            <br>
            <p>내 정보, 설정, 동행요청 목록 화면 기능 담당 및 동행요청 푸시알림 구현</p>
        </td>
        <td>
            <img src="https://github.com/Ganghyungoo.png?size=120">
            <br>
            <a href="https://github.com/Ganghyungoo"><I>Ganghyungoo</I></a>
            <br>
            <p>동행 리뷰, 채팅 화면 기능 담당</p>
        </td>
        <td>
            <img src="https://github.com/YonjjinJang.png?size=120">
            <br>
            <a href="https://github.com/YonjjinJang"><I>YonjjinJang</I></a>
            <br>
            <p>회원가입, 소셜 로그인 화면 기능 담당 및 버그 보완</p>
        </td>
        <td>
            <img src="https://github.com/DoReMinWoo.png?size=120">
            <br>
            <a href="https://github.com/DoReMinWoo"><I>DoReMinWoo</I></a>
            <br>
            <p>메인 홈, 동행글 상세, 동행글 수정, 찜 목록 화면 기능 담당</p>
        </td>
        <td>
            <img src="https://picsum.photos/120/120">
            <br>
            <a href="https://github.com/y-d-h"><I>y-d-h</I></a>
            <br>
            <p>동행글 목록, 동행글 등록 화면 기능 담당 및 검색 기능 구현</p>
        </td>
    </tr>
</table>

<br/>

## 👀 시연 영상
<br>[![image](https://github.com/APPSCHOOL2-Android/FinalProject-TripFriend/assets/106245278/30c87c50-dac4-43de-ba0c-55338f0e7985)](https://youtu.be/JOU5Lf0hqXs)

<br/>

## 🌟 주요 기능
### 로그인 및 회원가입
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

### 지도
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

### 채팅
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

### 알림
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

### 게시글 필터
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

### 동행글 CRUD
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

## 트러블 슈팅


<br/>

## 기술 스택

