# CodeArena
## Cấu hình ban đầu mariadb để chạy code arena:
phiên bản mariadb: 10.x
mysql -u root -p => nhập mật khẩu root
```bash
CREATE USER 'admin'@'localhost' IDENTIFIED BY 'codeArena@#2025';
CREATE DATABASE code_arena;
GRANT ALL PRIVILEGES ON code_arena.* TO 'admin'@'localhost';
FLUSH PRIVILEGES;
```
## Tiếp tục config trong intellij IDEA
- Tìm kiếm mục database (bên phải) -> chọn + add datasource
- Điền thông tin vào về tài khoản và mật khẩu 
- Apply -> chạy project

## Khởi tạo user root role MANAGER:
- Tạo tài khoản như bình thuờng, tài khoản đuợc lưu với role USER, sau đó vào trực tiếp db để thay đổi role cho nó:
``` bash
UPDATE user
SET role = 2
WHERE user_id = <id_cua_user>;
```
- Từ root user MANAGER có thể cấp role ADMIN cho các user thuờng.
- User muốn được làm Admin sẽ gửi yêu cầu đến hệ thống để Manager xác nhận.
