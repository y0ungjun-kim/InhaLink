import "./App.css";
import { useState, useEffect, useRef } from "react";
import { api } from "./api";

function App() {
  const [step, setStep] = useState(1);
  const [verified, setVerified] = useState(false);
  const [currentUser, setCurrentUser] = useState(null); // { studentId, name }
  const [posts, setPosts] = useState([]);
  const [selectedPost, setSelectedPost] = useState(null);

  // 팀플·공모전 목록 진입 시 API에서 불러옴
  const loadPosts = async () => {
    try {
      const data = await api.getPosts();
      setPosts(data || []);
    } catch {
      setPosts([]);
    }
  };

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
          <LoginBox
            setStep={setStep}
            setCurrentUser={setCurrentUser}
          />
        )}

        {step === 3 && (
          <SignupBox
            verified={verified}
            setVerified={setVerified}
            setStep={setStep}
          />
        )}

        {/* 최초 프로필 작성 (로그인 후 profileComplete=false 일 때) */}
        {step === 9 && currentUser && (
          <ProfileCreateBox
            studentId={currentUser.studentId}
            setCurrentUser={setCurrentUser}
            setStep={setStep}
          />
        )}

        {/* 마이페이지 프로필 수정 */}
        {step === 10 && currentUser && (
          <ProfileEditBox
            studentId={currentUser.studentId}
            setStep={setStep}
          />
        )}

        {step === 8 && (
          <div className="service-wrap">
            <div className="service-card" onClick={() => setStep(12)}>
              <div className="icon green">👥</div>
              <h2>밥친구 찾기</h2>
              <p>같이 식사할 친구를 찾아보세요</p>
            </div>
            <div className="service-card" onClick={() => { loadPosts(); setStep(4); }}>
              <div className="icon purple">🏆</div>
              <h2>팀플·공모전</h2>
              <p>함께 도전할 팀원을 구해요</p>
            </div>
            <div className="service-card" onClick={() => setStep(20)}>
              <div className="icon green">⚡</div>
              <h2>즉시 매칭</h2>
              <p>지금 바로 인하대 학우와 연결돼요</p>
            </div>
          </div>
        )}

        {step === 4 && (
          <TeamMainPage
            posts={posts}
            setSelectedPost={setSelectedPost}
            setStep={setStep}
          />
        )}

        {step === 5 && currentUser && (
          <TeamWritePage
            studentId={currentUser.studentId}
            setStep={setStep}
            reloadPosts={loadPosts}
          />
        )}

        {step === 6 && selectedPost && (
          <TeamDetailPage
            post={selectedPost}
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
            info={["작성자: 컴공 2학년", "장소: 학생식당", "시간: 오늘 12시", "모집 인원: 1/2명"]}
            content="점심 같이 먹을 밥친구 구합니다. 편하게 이야기하면서 먹을 사람 환영합니다!"
            buttonText="신청하기"
            backStep={12}
            setStep={setStep}
          />
        )}

        {step === 15 && (
          <StatusPage title="밥친구 모집 현황" backStep={12} setStep={setStep} />
        )}

        {/* 즉시 매칭 대기 화면 */}
        {step === 20 && currentUser && (
          <MatchingWaitPage
            studentId={currentUser.studentId}
            setStep={setStep}
          />
        )}

        {/* 즉시 매칭 완료 화면 */}
        {step === 21 && (
          <MatchingResultPage
            setStep={setStep}
          />
        )}
      </div>
    </div>
  );
}

// ── 로그인 ──────────────────────────────────────────────
function LoginBox({ setStep, setCurrentUser }) {
  const [studentId, setStudentId] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleLogin = async () => {
    if (!studentId.trim() || !password.trim()) {
      setError("아이디와 비밀번호를 입력해주세요.");
      return;
    }
    setLoading(true);
    setError("");
    try {
      const profile = await api.getProfile(studentId);
      setCurrentUser({ studentId, name: profile.name });
      if (!profile.profileComplete) {
        setStep(9); // 최초 프로필 작성
      } else {
        setStep(8);
      }
    } catch {
      setError("학번 또는 비밀번호가 올바르지 않습니다.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="box">
      <h2>로그인</h2>
      <input
        type="text"
        placeholder="학번"
        value={studentId}
        onChange={(e) => setStudentId(e.target.value)}
      />
      <input
        type="password"
        placeholder="비밀번호"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      {error && <p style={{ color: "#e24b4a", fontSize: "13px", margin: "4px 0" }}>{error}</p>}
      <button onClick={handleLogin} disabled={loading}>
        {loading ? "확인 중..." : "로그인"}
      </button>
    </div>
  );
}

// ── 회원가입 ─────────────────────────────────────────────
function SignupBox({ verified, setVerified, setStep }) {
  const [email, setEmail] = useState("");
  const [code, setCode] = useState("");
  const [studentId, setStudentId] = useState("");
  const [password, setPassword] = useState("");
  const [name, setName] = useState("");
  const [signupStudentId, setSignupStudentId] = useState("");
  const [gender, setGender] = useState("");
  const [contact, setContact] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSendCode = async () => {
    if (!email.trim()) { setError("이메일을 입력해주세요."); return; }
    setLoading(true); setError("");
    try {
      await api.sendCode(email);
      alert("인증번호가 발송되었습니다.");
    } catch (e) {
      setError(e?.email || e?.message || "이메일 발송에 실패했습니다.");
    } finally { setLoading(false); }
  };

  const handleVerify = async () => {
    if (!code.trim()) { setError("인증번호를 입력해주세요."); return; }
    setLoading(true); setError("");
    try {
      const result = await api.verifyCode(email, code);
      if (result === true) {
        setVerified(true);
        setError("");
      } else {
        setError("인증번호가 올바르지 않거나 만료되었습니다.");
      }
    } catch {
      setError("인증 확인에 실패했습니다.");
    } finally { setLoading(false); }
  };

  const handleSignup = async () => {
    if (!studentId.trim() || !password.trim() || !name.trim() || !signupStudentId.trim() || !gender || !contact.trim()) {
      setError("모든 필수 항목을 입력해주세요.");
      return;
    }
    setLoading(true); setError("");
    try {
      await api.signup({
        email,
        password,
        name,
        studentId: signupStudentId,
        gender,
        contact,
      });
      alert("회원가입이 완료되었습니다!");
      setVerified(false);
      setStep(1);
    } catch (e) {
      const msgs = Object.values(e || {}).join(" / ");
      setError(msgs || "회원가입에 실패했습니다.");
    } finally { setLoading(false); }
  };

  return (
    <div className="box">
      <h2>회원가입</h2>
      {!verified && (
        <>
          <input type="text" placeholder="이메일 (@inha.ac.kr 또는 @inha.edu)" value={email} onChange={(e) => setEmail(e.target.value)} />
          <button onClick={handleSendCode} disabled={loading}>인증번호 발송</button>
          <br /><br />
          <input type="text" placeholder="인증번호" value={code} onChange={(e) => setCode(e.target.value)} />
          <button onClick={handleVerify} disabled={loading}>인증 확인</button>
        </>
      )}
      {verified && (
        <>
          <h3>계정 정보</h3>
          <input type="text" placeholder="학번" value={signupStudentId} onChange={(e) => setSignupStudentId(e.target.value)} />
          <input type="password" placeholder="비밀번호 (8자 이상)" value={password} onChange={(e) => setPassword(e.target.value)} />
          <h3>기본 정보</h3>
          <input type="text" placeholder="이름" value={name} onChange={(e) => setName(e.target.value)} />
          <select value={gender} onChange={(e) => setGender(e.target.value)} style={{ width: "100%", padding: "13px", marginBottom: "14px", border: "1px solid #ddd", borderRadius: "12px", fontSize: "15px" }}>
            <option value="">성별 선택</option>
            <option value="MALE">남성</option>
            <option value="FEMALE">여성</option>
          </select>
          <input type="text" placeholder="연락처 (010-xxxx-xxxx)" value={contact} onChange={(e) => setContact(e.target.value)} />
        </>
      )}
      {error && <p style={{ color: "#e24b4a", fontSize: "13px", margin: "4px 0" }}>{error}</p>}
      {verified && (
        <button onClick={handleSignup} disabled={loading}>
          {loading ? "처리 중..." : "회원가입 완료"}
        </button>
      )}
    </div>
  );
}

// ── 최초 프로필 작성 ─────────────────────────────────────
function ProfileCreateBox({ studentId, setCurrentUser, setStep }) {
  const [form, setForm] = useState({ name: "", gender: "MALE", contact: "", department: "", domains: "", activities: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const set = (k) => (e) => setForm((f) => ({ ...f, [k]: e.target.value }));

  const handleSubmit = async () => {
    const { name, contact, department, domains } = form;
    if (!name.trim() || !contact.trim() || !department.trim() || !domains.trim()) {
      setError("이름, 연락처, 학과, 관심분야는 필수 입력 사항입니다.");
      return;
    }
    setLoading(true); setError("");
    try {
      const updated = await api.createProfile(studentId, form);
      setCurrentUser({ studentId, name: updated.name });
      setStep(8);
    } catch (e) {
      const msgs = Object.values(e || {}).join(" / ");
      setError(msgs || "프로필 작성에 실패했습니다.");
    } finally { setLoading(false); }
  };

  return (
    <div className="box">
      <h2>프로필 작성</h2>
      <p style={{ fontSize: "13px", color: "#6b7280", marginBottom: "16px" }}>서비스 이용을 위해 프로필을 작성해주세요.</p>
      <input type="text" placeholder="이름 *" value={form.name} onChange={set("name")} />
      <select value={form.gender} onChange={set("gender")} style={{ width: "100%", padding: "13px", marginBottom: "14px", border: "1px solid #ddd", borderRadius: "12px", fontSize: "15px" }}>
        <option value="MALE">남성</option>
        <option value="FEMALE">여성</option>
      </select>
      <input type="text" placeholder="연락처 * (010-xxxx-xxxx)" value={form.contact} onChange={set("contact")} />
      <input type="text" placeholder="학과 *" value={form.department} onChange={set("department")} />
      <input type="text" placeholder="관심 분야 * (예: 백엔드, AI, 디자인)" value={form.domains} onChange={set("domains")} />
      <textarea rows="4" placeholder="대외활동 이력 (선택)" value={form.activities} onChange={set("activities")} />
      {error && <p style={{ color: "#e24b4a", fontSize: "13px", margin: "4px 0" }}>{error}</p>}
      <button onClick={handleSubmit} disabled={loading}>
        {loading ? "저장 중..." : "프로필 작성 완료"}
      </button>
    </div>
  );
}

// ── 마이페이지 프로필 수정 ────────────────────────────────
function ProfileEditBox({ studentId, setStep }) {
  const [form, setForm] = useState({ name: "", gender: "", contact: "", department: "", domains: "", activities: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    api.getProfile(studentId).then((p) => {
      setForm({
        name: p.name || "",
        gender: p.gender || "",
        contact: p.contact || "",
        department: p.department || "",
        domains: p.domains || "",
        activities: p.activities || "",
      });
    }).catch(() => {});
  }, [studentId]);

  const set = (k) => (e) => setForm((f) => ({ ...f, [k]: e.target.value }));

  const handleSubmit = async () => {
    setLoading(true); setError("");
    try {
      await api.updateProfile(studentId, form);
      alert("프로필이 수정되었습니다.");
      setStep(8);
    } catch (e) {
      const msgs = Object.values(e || {}).join(" / ");
      setError(msgs || "프로필 수정에 실패했습니다.");
    } finally { setLoading(false); }
  };

  return (
    <div className="box">
      <h2>프로필 수정</h2>
      <input type="text" placeholder="이름" value={form.name} onChange={set("name")} />
      <select value={form.gender} onChange={set("gender")} style={{ width: "100%", padding: "13px", marginBottom: "14px", border: "1px solid #ddd", borderRadius: "12px", fontSize: "15px" }}>
        <option value="MALE">남성</option>
        <option value="FEMALE">여성</option>
      </select>
      <input type="text" placeholder="연락처" value={form.contact} onChange={set("contact")} />
      <input type="text" placeholder="학과" value={form.department} onChange={set("department")} />
      <input type="text" placeholder="관심 분야" value={form.domains} onChange={set("domains")} />
      <textarea rows="4" placeholder="대외활동 이력" value={form.activities} onChange={set("activities")} />
      {error && <p style={{ color: "#e24b4a", fontSize: "13px", margin: "4px 0" }}>{error}</p>}
      <button onClick={handleSubmit} disabled={loading}>
        {loading ? "저장 중..." : "수정 완료"}
      </button>
      <button className="back" onClick={() => setStep(8)}>취소</button>
    </div>
  );
}

// ── 팀플·공모전 목록 ─────────────────────────────────────
function TeamMainPage({ posts, setSelectedPost, setStep }) {
  return (
    <div className="box wide page-box">
      <h2>팀플·공모전 메인</h2>
      <input type="text" placeholder="검색어를 입력하세요" />
      <div className="simple-post-list">
        {posts.length === 0 && (
          <p style={{ color: "#6b7280", fontSize: "14px", padding: "12px 0" }}>등록된 모집글이 없습니다.</p>
        )}
        {posts.map((post) => (
          <div className="simple-post" key={post.id}>
            <div>
              <h3>{post.title}</h3>
              <p>{post.categoryDescription} · {post.projectName} · {post.maxMembers}명 모집</p>
            </div>
            <button className="small-btn" onClick={() => { setSelectedPost(post); setStep(6); }}>
              {post.statusDescription}
            </button>
          </div>
        ))}
      </div>
      <div className="btn-row">
        <button onClick={() => setStep(5)}>작성</button>
        <button onClick={() => setStep(7)}>현황</button>
      </div>
      <button className="back" onClick={() => setStep(8)}>서비스 선택으로</button>
    </div>
  );
}

// ── 팀플·공모전 모집글 작성 ──────────────────────────────
function TeamWritePage({ studentId, setStep, reloadPosts }) {
  const [form, setForm] = useState({
    title: "", category: "", projectName: "", content: "",
    maxMembers: "", deadline: "", teamFormationDate: "",
    preferredQualifications: "", message: "", activityMethod: "",
  });
  const [error, setError] = useState("");
  const [fieldErrors, setFieldErrors] = useState({});
  const [loading, setLoading] = useState(false);

  const set = (k) => (e) => setForm((f) => ({ ...f, [k]: e.target.value }));

  const handleSubmit = async () => {
    const required = { title: "제목", category: "카테고리", projectName: "프로젝트/공모전명", content: "내용", maxMembers: "모집 인원", deadline: "마감일", activityMethod: "활동 방식" };
    const missing = Object.entries(required).filter(([k]) => !form[k]?.toString().trim()).map(([, v]) => v);
    if (missing.length > 0) {
      setError(`필수 항목을 입력해주세요: ${missing.join(", ")}`);
      return;
    }
    setLoading(true); setError(""); setFieldErrors({});
    try {
      await api.createPost(studentId, {
        ...form,
        maxMembers: Number(form.maxMembers),
        deadline: form.deadline + ":00",
        teamFormationDate: form.teamFormationDate ? form.teamFormationDate + ":00" : null,
      });
      alert("모집글이 등록되었습니다!");
      await reloadPosts();
      setStep(4);
    } catch (e) {
      if (typeof e === "object" && !e.message) {
        setFieldErrors(e);
        setError("입력 정보를 확인해주세요.");
      } else {
        setError(e?.message || "모집글 등록에 실패했습니다.");
      }
    } finally { setLoading(false); }
  };

  const fe = (k) => fieldErrors[k] ? <p style={{ color: "#e24b4a", fontSize: "12px", margin: "-10px 0 8px" }}>{fieldErrors[k]}</p> : null;

  return (
    <div className="box">
      <h2>공모전 모집글 작성</h2>
      <input type="text" placeholder="제목 *" value={form.title} onChange={set("title")} />
      {fe("title")}
      <select value={form.category} onChange={set("category")} style={{ width: "100%", padding: "13px", marginBottom: "14px", border: "1px solid #ddd", borderRadius: "12px", fontSize: "15px" }}>
        <option value="">카테고리 선택 *</option>
        <option value="CONTEST">공모전</option>
        <option value="TEAM_PROJECT">팀플</option>
        <option value="PROJECT">프로젝트</option>
      </select>
      <input type="text" placeholder="프로젝트/공모전 이름 *" value={form.projectName} onChange={set("projectName")} />
      {fe("projectName")}
      <textarea rows="4" placeholder="내용 *" value={form.content} onChange={set("content")} />
      {fe("content")}
      <input type="number" placeholder="모집 인원 *" value={form.maxMembers} onChange={set("maxMembers")} />
      {fe("maxMembers")}
      <label style={{ fontSize: "13px", color: "#6b7280" }}>모집 마감일 *</label>
      <input type="datetime-local" value={form.deadline} onChange={set("deadline")} />
      {fe("deadline")}
      <label style={{ fontSize: "13px", color: "#6b7280" }}>팀 결성 희망일</label>
      <input type="datetime-local" value={form.teamFormationDate} onChange={set("teamFormationDate")} />
      <select value={form.activityMethod} onChange={set("activityMethod")} style={{ width: "100%", padding: "13px", marginBottom: "14px", border: "1px solid #ddd", borderRadius: "12px", fontSize: "15px" }}>
        <option value="">활동 방식 선택 *</option>
        <option value="ONLINE">온라인</option>
        <option value="OFFLINE">오프라인</option>
        <option value="BOTH">온/오프라인 병행</option>
      </select>
      <textarea rows="3" placeholder="우대사항 (선택)" value={form.preferredQualifications} onChange={set("preferredQualifications")} />
      <textarea rows="3" placeholder="하고 싶은 말 (선택)" value={form.message} onChange={set("message")} />
      {error && <p style={{ color: "#e24b4a", fontSize: "13px", margin: "4px 0" }}>{error}</p>}
      <button onClick={handleSubmit} disabled={loading}>
        {loading ? "등록 중..." : "등록"}
      </button>
      <button className="back" onClick={() => setStep(4)}>취소</button>
    </div>
  );
}

// ── 팀플·공모전 상세 ─────────────────────────────────────
function TeamDetailPage({ post, setStep }) {
  return (
    <div className="box wide">
      <h2>공모전 모집글 상세</h2>
      <div className="post">
        <h3>{post.title}</h3>
        <p>카테고리: {post.categoryDescription}</p>
        <p>프로젝트명: {post.projectName}</p>
        <p>작성자: {post.writerName}</p>
        <p>모집 인원: {post.maxMembers}명</p>
        <p>활동 방식: {post.activityMethodDescription}</p>
        <p>마감일: {new Date(post.deadline).toLocaleDateString("ko-KR")}</p>
        {post.preferredQualifications && <p>우대사항: {post.preferredQualifications}</p>}
        <p style={{ marginTop: "12px" }}>{post.content}</p>
        {post.message && <p style={{ color: "#6b7280" }}>{post.message}</p>}
        <button>지원하기</button>
      </div>
      <button className="back" onClick={() => setStep(4)}>뒤로가기</button>
    </div>
  );
}

// ── 즉시 매칭 대기 ────────────────────────────────────────
function MatchingWaitPage({ studentId, setStep }) {
  const [dots, setDots] = useState(".");
  const intervalRef = useRef(null);
  const pollRef = useRef(null);

  useEffect(() => {
    // 매칭 시작
    api.joinMatching(studentId).then((res) => {
      if (res?.status === "MATCHED") {
        sessionStorage.setItem("matchingResult", JSON.stringify(res.partner));
        setStep(21);
      }
    }).catch(() => {});

    // 점 애니메이션
    intervalRef.current = setInterval(() => {
      setDots((d) => (d.length >= 3 ? "." : d + "."));
    }, 500);

    // 3초 폴링
    pollRef.current = setInterval(async () => {
      try {
        const res = await api.getMatchingStatus(studentId);
        if (res?.status === "MATCHED") {
          sessionStorage.setItem("matchingResult", JSON.stringify(res.partner));
          clearInterval(intervalRef.current);
          clearInterval(pollRef.current);
          setStep(21);
        }
      } catch {}
    }, 3000);

    return () => {
      clearInterval(intervalRef.current);
      clearInterval(pollRef.current);
    };
  }, [studentId, setStep]);

  const handleCancel = async () => {
    clearInterval(intervalRef.current);
    clearInterval(pollRef.current);
    await api.cancelMatching(studentId);
    setStep(8);
  };

  return (
    <div className="box start-box">
      <div className="start-icon">⚡</div>
      <h2>매칭 중{dots}</h2>
      <p className="start-text">
        지금 접속한 인하대 학우와 연결하고 있어요.<br />
        잠시만 기다려주세요.
      </p>
      <button className="back" onClick={handleCancel}>취소</button>
    </div>
  );
}

// ── 즉시 매칭 완료 ────────────────────────────────────────
function MatchingResultPage({ setStep }) {
  const partner = JSON.parse(sessionStorage.getItem("matchingResult") || "{}");

  return (
    <div className="box wide">
      <h2>매칭 완료!</h2>
      <div className="post">
        <h3>{partner.name || "상대방"}</h3>
        <p>학과: {partner.department || "-"}</p>
        <p>관심 분야: {partner.domains || "-"}</p>
        {partner.activities && <p>대외활동: {partner.activities}</p>}
        <p style={{ fontWeight: "bold", marginTop: "12px" }}>연락처: {partner.contact || "-"}</p>
      </div>
      <button className="back" onClick={() => { sessionStorage.removeItem("matchingResult"); setStep(8); }}>
        서비스 선택으로
      </button>
    </div>
  );
}

// ── 기존 컴포넌트 (밥친구 등 미연동 유지) ───────────────
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
            <button className="small-btn" onClick={() => setStep(detailStep)}>모집중</button>
          </div>
        ))}
      </div>
      <div className="btn-row">
        <button onClick={() => setStep(writeStep)}>작성</button>
        <button onClick={() => setStep(statusStep)}>현황</button>
      </div>
      <button className="back" onClick={() => setStep(backStep)}>서비스 선택으로</button>
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
      <button onClick={() => { alert(alertText); setStep(backStep); }}>등록</button>
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
        {info.map((item, index) => <p key={index}>{item}</p>)}
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
