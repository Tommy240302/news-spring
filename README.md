
# News Website Backend (Spring Boot)

## Giới thiệu
Đây là phần **backend** của hệ thống website tin tức với hỗ trợ AI trong quản lý bình luận.  
Backend được xây dựng bằng **Java Spring Boot**, chịu trách nhiệm xử lý logic nghiệp vụ, quản lý dữ liệu và cung cấp API cho frontend cũng như AI service.

---

## Công nghệ sử dụng
- **Java 11+**
- **Spring Boot**
- **Spring Security** (xác thực & phân quyền)
- **Hibernate**
- **MySQL**
- **Maven**

---

## Chức năng chính
- Đăng ký / đăng nhập người dùng (Reader, Author, Admin)  
- Quản lý bài viết: tạo, duyệt, xóa, thống kê  
- Quản lý người dùng và phân quyền  
- Quản lý thể loại (chuyên mục tin tức)  
- Quản lý bình luận (tích hợp AI kiểm duyệt qua REST API)  
- Quản lý nhuận bút (mô phỏng)  

---

## Cài đặt & chạy backend

### 1. Yêu cầu hệ thống
- JDK 11 hoặc cao hơn  
- MySQL 8.0  
- Maven  

### 2. Cấu hình database
Tạo database `news_db` trong MySQL:  

```sql
CREATE DATABASE news_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
````

Sửa file `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/news_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### 3. Chạy ứng dụng

```bash
mvn spring-boot:run
```

Ứng dụng sẽ chạy tại: `http://localhost:6969`

---

## API chính

* `/api/auth/register` – Đăng ký tài khoản
* `/api/auth/login` – Đăng nhập
* `/api/news` – Quản lý bài viết
* `/api/comments` – Quản lý bình luận
* `/api/admin` – Quản lý hệ thống (Admin)

---



