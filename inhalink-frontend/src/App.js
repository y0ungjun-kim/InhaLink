import { useState } from "react";

function App() {
  const [step, setStep] = useState(1);
  const [verified, setVerified] = useState(false);
  const [service, setService] = useState("");

  return (
    <div style={{ padding: "20px", width: "350px" }}>
      <h1>인하링크</h1>

      {step === 1 && (
        <div>
          <button onClick={() => setStep(2)}>로그인</button>
          <br /><br />
          <button onClick={() => setStep(3)}>회원가입</button>
        </div>
      )}

      {step === 2 && (
        <>
          <h2>로그인 화면</h2>
          <hr />
          <label>아이디: <input type="text" /></label>
          <br /><br />
          <label>비밀번호: <input type="password" /></label>
          <br /><br />
          <button onClick={() => setStep(8)}>로그인</button>
        </>
      )}

      {step === 3 && (
        <>
          <h2>회원가입 화면</h2>
          <hr />

          {!verified && (
            <>
              <label>이메일: <input type="text" /></label>
              <br /><br />
              <button>인증번호발송</button>
              <br /><br />
              <label>인증번호: <input type="text" /></label>
              <br /><br />
              <button onClick={() => setVerified(true)}>인증 확인</button>
            </>
          )}

          {verified && (
            <>
              <h3>계정 정보</h3>
              <label>아이디: <input type="text" /></label>
              <br /><br />
              <label>비밀번호: <input type="password" /></label>
              <br /><br />

              <h3>프로필 등록</h3>
              <label>이름: <input type="text" /></label>
              <br /><br />
              <label>학번: <input type="text" /></label>
              <br /><br />
              <label>학과: <input type="text" /></label>
              <br /><br />
              <label>관심분야: <input type="text" /></label>
              <br /><br />
              <label>대외활동 경험:</label>
              <br />
              <textarea rows="4" cols="30"></textarea>
              <br /><br />

              <button
                onClick={() => {
                  alert("회원가입이 완료되었습니다!");
                  setVerified(false);
                  setStep(1);
                }}
              >
                회원가입 완료
              </button>
            </>
          )}
        </>
      )}

      {step === 8 && (
        <>
          <h2>서비스 선택</h2>
          <hr />

          <button
            onClick={() => {
              setService("meal");
              setStep(12);
            }}
          >
            밥친구
          </button>

          <br /><br />

          <button
            onClick={() => {
              setService("contest");
              setStep(4);
            }}
          >
            공모전
          </button>
        </>
      )}

      {/* 공모전 메인 */}
      {step === 4 && (
        <>
          <h2>공모전 메인 화면</h2>
          <hr />

          <h3>모집글 탐색</h3>
          <input type="text" placeholder="검색..." />
          <br /><br />

          <div>
            <h4>캡스톤 팀원 모집!</h4>
            <p>컴공 3학년 · 3/4명</p>
            <button onClick={() => setStep(6)}>모집중</button>
          </div>

          <hr />

          <div>
            <h4>알고리즘 스터디</h4>
            <p>전체학과 · 2/5명</p>
            <button onClick={() => setStep(6)}>모집중</button>
          </div>

          <br />
          <hr />

          <button onClick={() => setStep(5)}>작성</button>
          <button onClick={() => setStep(7)}>현황</button>
          <br /><br />
          <button onClick={() => setStep(8)}>서비스 선택으로</button>
        </>
      )}

      {step === 5 && (
        <>
          <h2>공모전 모집글 작성</h2>
          <hr />

          <label>제목: <input type="text" /></label>
          <br /><br />

          <label>내용:</label>
          <br />
          <textarea rows="5" cols="30"></textarea>
          <br /><br />

          <label>모집 인원: <input type="number" /></label>
          <br /><br />

          <button
            onClick={() => {
              alert("모집글이 등록되었습니다!");
              setStep(4);
            }}
          >
            등록
          </button>

          <br /><br />
          <button onClick={() => setStep(4)}>취소</button>
        </>
      )}

      {step === 6 && (
        <>
          <h2>공모전 모집글 상세</h2>
          <hr />

          <h3>캡스톤 팀원 모집!</h3>
          <p><b>작성자:</b> 컴공 3학년</p>
          <p><b>모집 인원:</b> 3/4명</p>

          <hr />

          <p>
            캡스톤 프로젝트 함께할 팀원을 모집합니다.<br />
            프론트/백엔드 모두 환영합니다!
          </p>

          <button>지원하기</button>
          <br /><br />
          <button onClick={() => setStep(4)}>뒤로가기</button>
        </>
      )}

      {step === 7 && (
        <>
          <h2>공모전 모집 현황</h2>
          <hr />

          <h3>내가 올린 모집글</h3>
          <div>
            <h4>캡스톤 팀원 모집!</h4>
            <p>지원자 2명</p>
            <button>지원자 목록 보기</button>
          </div>

          <hr />

          <h3>내가 지원한 모집글</h3>
          <div>
            <h4>알고리즘 스터디</h4>
            <p>상태: 대기중</p>
          </div>

          <div>
            <h4>공모전 팀원 모집</h4>
            <p>상태: 수락됨</p>
          </div>

          <br />
          <button onClick={() => setStep(4)}>메인으로</button>
        </>
      )}

      {/* 밥친구 메인 */}
      {step === 12 && (
        <>
          <h2>밥친구 메인 화면</h2>
          <hr />

          <h3>모집글 탐색</h3>
          <input type="text" placeholder="검색..." />
          <br /><br />

          <div>
            <h4>학생식당 같이 먹을 사람!</h4>
            <p>오늘 12시 · 학생식당 · 1/2명</p>
            <button onClick={() => setStep(14)}>모집중</button>
          </div>

          <hr />

          <div>
            <h4>후문 라멘 먹을 사람</h4>
            <p>오늘 6시 · 후문 · 2/4명</p>
            <button onClick={() => setStep(14)}>모집중</button>
          </div>

          <br />
          <hr />

          <button onClick={() => setStep(13)}>작성</button>
          <button onClick={() => setStep(15)}>현황</button>
          <br /><br />
          <button onClick={() => setStep(8)}>서비스 선택으로</button>
        </>
      )}

      {step === 13 && (
        <>
          <h2>밥친구 모집글 작성</h2>
          <hr />

          <label>제목: <input type="text" /></label>
          <br /><br />

          <label>식사 장소: <input type="text" /></label>
          <br /><br />

          <label>시간: <input type="text" /></label>
          <br /><br />

          <label>모집 인원: <input type="number" /></label>
          <br /><br />

          <label>내용:</label>
          <br />
          <textarea rows="5" cols="30"></textarea>
          <br /><br />

          <button
            onClick={() => {
              alert("밥친구 모집글이 등록되었습니다!");
              setStep(12);
            }}
          >
            등록
          </button>

          <br /><br />
          <button onClick={() => setStep(12)}>취소</button>
        </>
      )}

      {step === 14 && (
        <>
          <h2>밥친구 모집글 상세</h2>
          <hr />

          <h3>학생식당 같이 먹을 사람!</h3>
          <p><b>작성자:</b> 컴공 2학년</p>
          <p><b>장소:</b> 학생식당</p>
          <p><b>시간:</b> 오늘 12시</p>
          <p><b>모집 인원:</b> 1/2명</p>

          <hr />

          <p>
            점심 같이 먹을 밥친구 구합니다.<br />
            편하게 이야기하면서 먹을 사람 환영합니다!
          </p>

          <button>신청하기</button>
          <br /><br />
          <button onClick={() => setStep(12)}>뒤로가기</button>
        </>
      )}

      {step === 15 && (
        <>
          <h2>밥친구 모집 현황</h2>
          <hr />

          <h3>내가 올린 모집글</h3>
          <div>
            <h4>학생식당 같이 먹을 사람!</h4>
            <p>신청자 1명</p>
            <button>신청자 목록 보기</button>
          </div>

          <hr />

          <h3>내가 신청한 모집글</h3>
          <div>
            <h4>후문 라멘 먹을 사람</h4>
            <p>상태: 대기중</p>
          </div>

          <div>
            <h4>학식 저녁 같이 먹을 사람</h4>
            <p>상태: 수락됨</p>
          </div>

          <br />
          <button onClick={() => setStep(12)}>메인으로</button>
        </>
      )}
    </div>
  );
}

export default App;