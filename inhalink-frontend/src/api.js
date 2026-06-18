const BASE = "http://localhost:8080/api";

function getToken() {
  return localStorage.getItem("token");
}

export function saveToken(token) {
  localStorage.setItem("token", token);
}

export function clearToken() {
  localStorage.removeItem("token");
}

async function request(method, path, body) {
  const token = getToken();
  const headers = { "Content-Type": "application/json" };
  if (token) headers["Authorization"] = `Bearer ${token}`;

  const res = await fetch(`${BASE}${path}`, {
    method,
    headers,
    body: body ? JSON.stringify(body) : undefined,
  });
  let json;
  try {
    json = await res.json();
  } catch {
    throw { message: "서버 응답을 처리할 수 없습니다." };
  }
  if (!res.ok) throw json;
  return json.data;
}

export const api = {
  // 이메일 인증
  sendCode: (email) =>
    request("POST", "/emails/send", { email }),
  verifyCode: (email, verificationCode) =>
    request("POST", "/emails/verify", { email, verificationCode }),

  // 회원가입 / 로그인
  signup: (body) =>
    request("POST", "/users/signup", body),
  login: (studentId, password) =>
    request("POST", "/users/login", { studentId, password }),

  // 내 정보 조회 (토큰 기반)
  getMe: () =>
    request("GET", "/users/me"),

  // 프로필
  getProfile: (studentId) =>
    request("GET", `/users/${studentId}/profile`),
  createProfile: (studentId, body) =>
    request("POST", `/users/${studentId}/profile`, body),
  updateProfile: (studentId, body) =>
    request("PUT", `/users/${studentId}/profile`, body),

  // 모집글
  getPosts: () =>
    request("GET", "/posts"),
  getPost: (postId) =>
    request("GET", `/posts/${postId}`),
  createPost: (studentId, body) =>
    request("POST", `/posts?studentId=${studentId}`, body),

  // 즉시 매칭
  joinMatching: (studentId) =>
    request("POST", `/matching?studentId=${studentId}`),
  getMatchingStatus: (studentId) =>
    request("GET", `/matching?studentId=${studentId}`),
  cancelMatching: (studentId) =>
    fetch(`${BASE}/matching?studentId=${studentId}`, { method: "DELETE" }),
};
