import "./App.css";
import { useState } from "react";

function App() {
  const [step, setStep] = useState(1);
  const [verified, setVerified] = useState(false);

  return (
    <div className="app">
      <div className="container">
        <h1 className="logo">InhaLink</h1>
        <p className="subtitle">인하대학교 학생 매칭 플랫폼</p>

       {step === 1 && (
          <div className="box start-box">
            <div className="start-icon">🔗</div>

            <h2>인하링크에 오신 걸 환영합니다</h2>

            <p className="start-text">
              밥친구부터 공모전 팀원까지<br />
              인하대 학생들을 연결해주는 플랫폼
            </p>

            <button onClick={() => setStep(2)}>로그인</button>

            <button className="outline-btn" onClick={() => setStep(3)}>
              회원가입
            </button>
          </div>
        )}

        {step === 2 && (
          <div className="box">
            <h2>로그인</h2>
            <input type="text" placeholder="아이디" />
            <input type="password" placeholder="비밀번호" />
            <button onClick={() => setStep(8)}>로그인</button>
          </div>
        )}

        {step === 3 && (
          <div className="box">
            <h2>회원가입</h2>

            {!verified && (
              <>
                <input type="text" placeholder="이메일" />
                <button>인증번호 발송</button>

                <br />
                <br />

                <input type="text" placeholder="인증번호" />
                <button onClick={() => setVerified(true)}>인증 확인</button>
              </>
            )}

            {verified && (
              <>
                <h3>계정 정보</h3>
                <input type="text" placeholder="아이디" />
                <input type="password" placeholder="비밀번호" />

                <h3>프로필 등록</h3>
                <input type="text" placeholder="이름" />
                <input type="text" placeholder="학번" />
                <input type="text" placeholder="학과" />
                <input type="text" placeholder="관심분야" />
                <textarea rows="4" placeholder="대외활동 경험"></textarea>

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
          </div>
        )}

        {step === 8 && (
          <div className="service-wrap">
            <div className="service-card" onClick={() => setStep(12)}>
              <div className="icon green">👥</div>
              <h2>밥친구 찾기</h2>
              <p>같이 식사할 친구를 찾아보세요</p>
            </div>

            <div className="service-card" onClick={() => setStep(4)}>
              <div className="icon purple">🏆</div>
              <h2>팀플·공모전</h2>
              <p>함께 도전할 팀원을 구해요</p>
            </div>
          </div>
        )}

        {step === 4 && (
          <MainPage
            title="팀플·공모전"
            posts={[
              ["캡스톤 팀원 모집!", "컴공 3학년 · 3/4명"],
              ["알고리즘 스터디", "전체학과 · 2/5명"],
            ]}
            detailStep={6}
            writeStep={5}
            statusStep={7}
            backStep={8}
            setStep={setStep}
          />
        )}

        {step === 5 && (
          <WritePage
            title="공모전 모집글 작성"
            alertText="모집글이 등록되었습니다!"
            backStep={4}
            setStep={setStep}
          />
        )}

        {step === 6 && (
          <DetailPage
            title="공모전 모집글 상세"
            postTitle="캡스톤 팀원 모집!"
            info={["작성자: 컴공 3학년", "모집 인원: 3/4명"]}
            content="캡스톤 프로젝트 함께할 팀원을 모집합니다. 프론트/백엔드 모두 환영합니다!"
            buttonText="지원하기"
            backStep={4}
            setStep={setStep}
          />
        )}

        {step === 7 && (
          <StatusPage title="공모전 모집 현황" backStep={4} setStep={setStep} />
        )}

        {step === 12 && (
          <MainPage
            title="밥친구"
            posts={[
              ["학생식당 같이 먹을 사람!", "오늘 12시 · 학생식당 · 1/2명"],
              ["후문 라멘 먹을 사람", "오늘 6시 · 후문 · 2/4명"],
            ]}
            detailStep={14}
            writeStep={13}
            statusStep={15}
            backStep={8}
            setStep={setStep}
          />
        )}

        {step === 13 && (
          <WritePage
            title="밥친구 모집글 작성"
            alertText="밥친구 모집글이 등록되었습니다!"
            backStep={12}
            setStep={setStep}
          />
        )}

        {step === 14 && (
          <DetailPage
            title="밥친구 모집글 상세"
            postTitle="학생식당 같이 먹을 사람!"
            info={[
              "작성자: 컴공 2학년",
              "장소: 학생식당",
              "시간: 오늘 12시",
              "모집 인원: 1/2명",
            ]}
            content="점심 같이 먹을 밥친구 구합니다. 편하게 이야기하면서 먹을 사람 환영합니다!"
            buttonText="신청하기"
            backStep={12}
            setStep={setStep}
          />
        )}

        {step === 15 && (
          <StatusPage title="밥친구 모집 현황" backStep={12} setStep={setStep} />
        )}
      </div>
    </div>
  );
}

function MainPage({ title, posts, detailStep, writeStep, statusStep, backStep, setStep }) {
  return (
    <div className="box wide page-box">
      <h2>{title} 메인</h2>
      <input type="text" placeholder="검색어를 입력하세요" />

      <div className="simple-post-list">
        {posts.map((post, index) => (
          <div className="simple-post" key={index}>
            <div>
              <h3>{post[0]}</h3>
              <p>{post[1]}</p>
            </div>

            <button className="small-btn" onClick={() => setStep(detailStep)}>
              모집중
            </button>
          </div>
        ))}
      </div>

      <div className="btn-row">
        <button onClick={() => setStep(writeStep)}>작성</button>
        <button onClick={() => setStep(statusStep)}>현황</button>
      </div>

      <button className="back" onClick={() => setStep(backStep)}>
        서비스 선택으로
      </button>
    </div>
  );
}
function WritePage({ title, alertText, backStep, setStep }) {
  return (
    <div className="box">
      <h2>{title}</h2>
      <input type="text" placeholder="제목" />
      <input type="text" placeholder="장소 또는 분야" />
      <input type="text" placeholder="시간" />
      <input type="number" placeholder="모집 인원" />
      <textarea rows="5" placeholder="내용"></textarea>

      <button
        onClick={() => {
          alert(alertText);
          setStep(backStep);
        }}
      >
        등록
      </button>

      <button className="back" onClick={() => setStep(backStep)}>취소</button>
    </div>
  );
}

function DetailPage({ title, postTitle, info, content, buttonText, backStep, setStep }) {
  return (
    <div className="box wide">
      <h2>{title}</h2>
      <div className="post">
        <h3>{postTitle}</h3>
        {info.map((item, index) => (
          <p key={index}>{item}</p>
        ))}
        <p>{content}</p>
        <button>{buttonText}</button>
      </div>

      <button className="back" onClick={() => setStep(backStep)}>뒤로가기</button>
    </div>
  );
}

function StatusPage({ title, backStep, setStep }) {
  return (
    <div className="box wide">
      <h2>{title}</h2>

      <div className="post">
        <h3>내가 올린 모집글</h3>
        <p>지원자 또는 신청자 2명</p>
        <button>목록 보기</button>
      </div>

      <div className="post">
        <h3>내가 신청한 모집글</h3>
        <p>상태: 대기중</p>
      </div>

      <button className="back" onClick={() => setStep(backStep)}>메인으로</button>
    </div>
  );
}

export default App;