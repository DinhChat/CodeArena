# CodeArena - API Quick Guide

## 📋 Tổng quan

Hệ thống CodeArena đã được xây dựng hoàn chỉnh với đầy đủ chức năng quản lý Problems, Testcases và Submissions.

---

## 🔑 Authentication

### Register
```http
POST /auth/register
Content-Type: application/json

{
  "username": "user123",
  "password": "password123",
  "confirmPassword": "password123"
}
```

### Login
```http
POST /auth/login
Content-Type: application/json

{
  "username": "user123",
  "password": "password123"
}
```

**Response:** Nhận JWT token để sử dụng cho các API khác
```json
{
  "username": "user123",
  "jwtToken": "eyJhbGciOiJIUzUxMiJ9...",
  "role": "USER",
  "message": "Login Successfully!"
}
```

---

## 📝 Problem APIs

### 1. Tạo Problem (ADMIN/MANAGER)
```http
POST /api/problems
Authorization: Bearer {token}
Content-Type: application/json

{
  "problemCode": "TWO_SUM",
  "title": "Two Sum",
  "description": "Given an array of integers...",
  "inputFormat": "Line 1: n (array size)\nLine 2: n integers\nLine 3: target",
  "outputFormat": "Two space-separated indices",
  "constraints": "2 <= n <= 10^4",
  "difficultyLevel": "EASY",
  "timeLimit": 2000,
  "memoryLimit": 256
}
```

**Difficulty Levels:** `EASY`, `MEDIUM`, `HARD`

### 2. Cập nhật Problem
```http
PUT /api/problems/{problemId}
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "Updated Title",
  ...
}
```

### 3. Lấy Problem theo ID
```http
GET /api/problems/{problemId}
Authorization: Bearer {token}
```

### 4. Lấy Problem theo Code
```http
GET /api/problems/code/{problemCode}
Authorization: Bearer {token}
```

### 5. Lấy tất cả Problems
```http
GET /api/problems
Authorization: Bearer {token}
```

### 6. Lấy Problems đang active
```http
GET /api/problems/active
Authorization: Bearer {token}
```

### 7. Lấy Problems theo độ khó
```http
GET /api/problems/difficulty/EASY
Authorization: Bearer {token}
```

### 8. Xóa Problem (soft delete)
```http
DELETE /api/problems/{problemId}
Authorization: Bearer {token}
```

---

## 🧪 Testcase APIs

### 1. Tạo Testcase (ADMIN/MANAGER)
```http
POST /api/testcases/problem/{problemId}
Authorization: Bearer {token}
Content-Type: application/json

{
  "input": "4\n2 7 11 15\n9",
  "expectedOutput": "0 1",
  "isSample": true,
  "orderIndex": 1
}
```

- `isSample: true` → Testcase mẫu (user nhìn thấy)
- `isSample: false` → Testcase ẩn (để chấm)

### 2. Cập nhật Testcase
```http
PUT /api/testcases/{testcaseId}
Authorization: Bearer {token}
Content-Type: application/json

{
  "input": "...",
  "expectedOutput": "...",
  "isSample": true,
  "orderIndex": 1
}
```

### 3. Lấy tất cả Testcases của Problem
```http
GET /api/testcases/problem/{problemId}
Authorization: Bearer {token}
```

### 4. Lấy Sample Testcases
```http
GET /api/testcases/problem/{problemId}/samples
Authorization: Bearer {token}
```

### 5. Xóa Testcase
```http
DELETE /api/testcases/{testcaseId}
Authorization: Bearer {token}
```

---

## 🚀 Submission APIs

### 1. Submit Solution
```http
POST /api/submissions
Authorization: Bearer {token}
Content-Type: application/json

{
  "problemCode": "TWO_SUM",
  "code": "public class Solution {\n    // your code here\n}",
  "language": "JAVA"
}
```

**Languages:** `JAVA`, `PYTHON`, `CPP`, `C`, `JAVASCRIPT`

**Submission Status:**
- `PENDING` - Đang chờ chấm
- `RUNNING` - Đang chạy
- `ACCEPTED` - Đúng (AC)
- `WRONG_ANSWER` - Sai (WA)
- `TIME_LIMIT_EXCEEDED` - Quá giờ (TLE)
- `MEMORY_LIMIT_EXCEEDED` - Quá bộ nhớ (MLE)
- `RUNTIME_ERROR` - Lỗi runtime (RE)
- `COMPILATION_ERROR` - Lỗi biên dịch (CE)

### 2. Lấy Submission theo ID
```http
GET /api/submissions/{submissionId}
Authorization: Bearer {token}
```

### 3. Lấy Submissions của User
```http
GET /api/submissions/user
Authorization: Bearer {token}
```

### 4. Lấy Submissions của Problem
```http
GET /api/submissions/problem/{problemId}
Authorization: Bearer {token}
```

### 5. Lấy Submissions của User cho Problem
```http
GET /api/submissions/user/problem/{problemId}
Authorization: Bearer {token}
```

---

## 🔐 Phân quyền

### USER
- ✅ Xem danh sách problems
- ✅ Xem chi tiết problem
- ✅ Xem sample testcases
- ✅ Submit solution
- ✅ Xem submissions của mình

### ADMIN
- ✅ Tất cả quyền của USER
- ✅ Tạo/sửa/xóa problems
- ✅ Tạo/sửa/xóa testcases
- ✅ Xem tất cả testcases (kể cả hidden)

### MANAGER
- ✅ Tất cả quyền của ADMIN
- ✅ Quản lý users

---

## 💡 Workflow Examples

### Workflow 1: Admin tạo bài toán mới

```bash
# 1. Login as Admin
POST /auth/login
{
  "username": "admin",
  "password": "admin123"
}

# 2. Tạo Problem
POST /api/problems
{
  "problemCode": "ADD_TWO_NUMBERS",
  "title": "Add Two Numbers",
  "description": "...",
  "difficultyLevel": "EASY",
  "timeLimit": 1000,
  "memoryLimit": 128
}

# 3. Tạo Sample Testcase (user nhìn thấy)
POST /api/testcases/problem/1
{
  "input": "2 3",
  "expectedOutput": "5",
  "isSample": true,
  "orderIndex": 1
}

# 4. Tạo Hidden Testcases (để chấm)
POST /api/testcases/problem/1
{
  "input": "100 200",
  "expectedOutput": "300",
  "isSample": false,
  "orderIndex": 2
}
```

### Workflow 2: User làm bài

```bash
# 1. Login as User
POST /auth/login

# 2. Xem danh sách bài
GET /api/problems/active

# 3. Xem chi tiết bài
GET /api/problems/code/ADD_TWO_NUMBERS

# 4. Xem sample testcases
GET /api/testcases/problem/1/samples

# 5. Submit code
POST /api/submissions
{
  "problemCode": "ADD_TWO_NUMBERS",
  "code": "...",
  "language": "JAVA"
}

# 6. Kiểm tra kết quả
GET /api/submissions/{submissionId}
```

---

## 📊 Database Schema

### User
- `uid` (PK)
- `username` (unique)
- `hashedPassword`
- `role` (USER/ADMIN/MANAGER)
- `createdDate`

### Problem
- `problemId` (PK)
- `problemCode` (unique)
- `title`
- `description`
- `inputFormat`
- `outputFormat`
- `constraints`
- `difficultyLevel` (EASY/MEDIUM/HARD)
- `timeLimit` (ms)
- `memoryLimit` (MB)
- `createdBy` (FK → User)
- `isActive`

### Testcase
- `testcaseId` (PK)
- `problemId` (FK → Problem)
- `input` (TEXT)
- `expectedOutput` (TEXT)
- `isSample` (boolean)
- `orderIndex`

### Submission
- `submissionId` (PK)
- `problemId` (FK → Problem)
- `userId` (FK → User)
- `code` (TEXT)
- `language`
- `status`
- `executionTime` (ms)
- `memoryUsed` (MB)
- `errorMessage`
- `passedTestcases`
- `totalTestcases`
- `submittedAt`
- `judgedAt`

---

## ⚙️ Configuration

### application.properties
```properties
spring.application.name=CodeArena

# Database
spring.datasource.url=jdbc:mariadb://localhost:3306/code_arena
spring.datasource.username=admin
spring.datasource.password=codeArena@#2025
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MariaDBDialect
```

---

## 🎯 Các bước tiếp theo

1. **Tích hợp Judge System** - Tự động chấm bài submission
2. **Add validation** - Validate input data
3. **Add pagination** - Phân trang cho danh sách
4. **Add search & filter** - Tìm kiếm và lọc problems
5. **Add statistics** - Thống kê AC rate, submission count
6. **Add contest management** - Quản lý contest/competition
7. **Add discussion forum** - Thảo luận về bài toán
8. **Add editorial** - Hướng dẫn giải bài

---

## 🛠️ Testing với cURL

### Example: Register & Create Problem
```bash
# Register
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123",
    "confirmPassword": "admin123"
  }'

# Login & get token
TOKEN=$(curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }' | jq -r '.jwtToken')

# Create Problem
curl -X POST http://localhost:8080/api/problems \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "problemCode": "HELLO_WORLD",
    "title": "Hello World",
    "description": "Print Hello World",
    "difficultyLevel": "EASY",
    "timeLimit": 1000,
    "memoryLimit": 128
  }'
```

---

**Chúc bạn code vui vẻ! 🚀**
