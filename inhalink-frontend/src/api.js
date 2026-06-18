const BASE = "http://localhost:8080/api";

async function request(method, path, body) {
  const res = await fetch(`${BASE}${path}`, {
    method,
    headers: { "Content-Type": "application/json" },
    body: body ? JSON.stringify(body) : undefined,
  });
  const json = await res.json();
  if (!res.ok) throw json;
  return json.data;
}

export const api = {
  // 이메일 인증
  sendCode: (email) =>
    request("POST", "/emails/send", { email }),
  verifyCode: (email, verificationCode) =>
    request("POST", "/emails/verify", { email, verificationCode }),

  // 회원가입
  signup: (body) =>
    request("POST", "/users/signup", body),

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
