# CodeArena
## cấu hình ban đầu mariadb để chạy code arena:
mysql -u root -p => nhập mật khẩu root
```bash
CREATE USER 'admin'@'localhost' IDENTIFIED BY 'codeArena@#2025';
CREATE DATABASE code_arena;
GRANT ALL PRIVILEGES ON code_arena.* TO 'admin'@'localhost';
FLUSH PRIVILEGES;
```
# Tiếp tục config trong intellij IDEA
- tìm kiếm mục database (bên phải) -> chọn + add datasource
- điền thông tin vào về tài khoản và mật khẩu 
- apply -> chạy project
