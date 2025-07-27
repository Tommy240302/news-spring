# CQRS Implementation Guide

## Tổng quan
Dự án này đã được áp dụng kiến trúc CQRS (Command Query Responsibility Segregation) sử dụng Axon Framework để tách biệt các thao tác đọc (Query) và ghi (Command).

## Cấu trúc thư mục

```
src/main/java/com/ptit/news/
├── bus/                    # Command/Query Bus
│   ├── CommandBus.java     # Xử lý Commands
│   └── QueryBus.java       # Xử lý Queries
├── command/                # Commands (thao tác ghi)
│   ├── dto/               # Command DTOs
│   │   ├── CreateUserCommand.java
│   │   ├── SignInCommand.java
│   │   ├── UpdateUserCommand.java
│   │   └── AuthResponse.java
│   └── handler/           # Command Handlers
│       ├── CreateUserCommandHandler.java
│       ├── SignInCommandHandler.java
│       └── UpdateUserCommandHandler.java
├── query/                 # Queries (thao tác đọc)
│   ├── dto/              # Query DTOs
│   │   ├── GetUserByIdQuery.java
│   │   └── GetUserByEmailQuery.java
│   └── handler/          # Query Handlers
│       ├── GetUserByIdQueryHandler.java
│       └── GetUserByEmailQueryHandler.java
└── config/
    └── AxonConfig.java    # Axon Framework Configuration
```

## API Endpoints

### Authentication Endpoints

#### 1. Đăng ký tài khoản
```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "0123456789",
  "avatar": "https://example.com/avatar.jpg"
}
```

#### 2. Đăng nhập
```http
POST /api/auth/signin
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

Response:
```json
{
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "email": "user@example.com",
      "firstName": "John",
      "lastName": "Doe"
    },
    "tokenType": "Bearer"
  },
  "message": "Đăng nhập thành công",
  "status": "Success"
}
```

#### 3. Cập nhật thông tin user
```http
PUT /api/auth/user
Content-Type: application/json

{
  "id": 1,
  "firstName": "Jane",
  "lastName": "Smith",
  "phone": "0987654321",
  "avatar": "https://example.com/new-avatar.jpg"
}
```

#### 4. Lấy thông tin user theo email
```http
GET /api/auth/user/{email}
```

#### 5. Thêm bài đăng
```http
POST /api/author/create
Content-Type: application/json
Authorization: Bearer YOUR_ACCESS_TOKEN
{
    "title":"title",
    "summary":"summary",
    "content":"html",
    "image":"main image",
    "categoryId": 1
}
```
Response:
```json
{
    "data": {
        "id": 1,
        "title": "title",
        "summary": "summary",
        "image": "main image",
        "view": 0,
        "status": false,
        "content": "html",
        "authorId": 2,
        "publishedAt": null
    },
    "message": "Tạo bài đăng thành công",
    "errorMessage": null,
    "status": "Success"
}

```

#### 6. Đề xuất làm tác giả
```http
POST /api/users/request-author
Content-Type: application/json
Authorization: Bearer YOUR_ACCESS_TOKEN
{
    "profileUrl":"http://....",
    "sampleArticles": "http://...",
    "reason":"ssssss"
}
```
Response:
```json
{
    "data": {
        "id": 1,
        "profileUrl": "http://...",
        "sampleArticles": "http://...",
        "reason": "ssssss",
        "status": "PENDING"
    },
    "message": "Yêu cầu thành công hãy đợi phê duyệt",
    "errorMessage": null,
    "status": "Success"
}

```

## Cách thêm Command mới

### 1. Tạo Command DTO
```java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteUserCommand {
    private Long id;
}
```

### 2. Tạo Command Handler
```java
@Slf4j
@Component
public class DeleteUserCommandHandler {

    @Autowired
    private UserRepository userRepository;

    @CommandHandler
    public Response<String> handle(DeleteUserCommand command) {
        try {
            User user = userRepository.findById(command.getId()).orElse(null);
            
            if (user == null) {
                return Response.Error("Không tìm thấy user");
            }
            
            userRepository.delete(user);
            
            return Response.Success("User deleted successfully", "Xóa user thành công");
                
        } catch (Exception e) {
            log.error("Error deleting user: {}", e.getMessage(), e);
            return Response.Error("Lỗi khi xóa user");
        }
    }
}
```

### 3. Thêm endpoint trong Controller
```java
@DeleteMapping("api/auth/user/{id}")
public Response<String> deleteUser(@PathVariable Long id) {
    DeleteUserCommand command = DeleteUserCommand.builder()
        .id(id)
        .build();
    return commandBus.execute(command);
}
```

## Cách thêm Query mới

### 1. Tạo Query DTO
```java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllUsersQuery {
    private int page;
    private int size;
}
```

### 2. Tạo Query Handler
```java
@Slf4j
@Component
public class GetAllUsersQueryHandler {

    @Autowired
    private UserRepository userRepository;

    @QueryHandler
    public Response<Page<User>> handle(GetAllUsersQuery query) {
        try {
            Pageable pageable = PageRequest.of(query.getPage(), query.getSize());
            Page<User> users = userRepository.findAll(pageable);
            
            return Response.Success(users, "Lấy danh sách users thành công");
                
        } catch (Exception e) {
            log.error("Error getting users: {}", e.getMessage(), e);
            return Response.Error("Lỗi khi lấy danh sách users");
        }
    }
}
```

### 3. Thêm endpoint trong Controller
```java
@GetMapping("api/auth/users")
public Response<Page<User>> getAllUsers(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size) {
    GetAllUsersQuery query = GetAllUsersQuery.builder()
        .page(page)
        .size(size)
        .build();
    return queryBus.execute(query);
}
```

## Lợi ích của CQRS

1. **Tách biệt trách nhiệm**: Commands xử lý việc thay đổi dữ liệu, Queries xử lý việc đọc dữ liệu
2. **Tối ưu hiệu suất**: Có thể sử dụng các database khác nhau cho read và write
3. **Dễ mở rộng**: Có thể scale read và write operations độc lập
4. **Dễ test**: Mỗi handler có thể test riêng biệt
5. **Dễ maintain**: Code được tổ chức rõ ràng theo chức năng

## Lưu ý

- Tất cả Commands phải có `@CommandHandler` annotation
- Tất cả Queries phải có `@QueryHandler` annotation
- Sử dụng `Response.Success()` và `Response.Error()` để trả về kết quả
- Log lỗi trong catch block để debug
- Validation nên được thực hiện trong Command/Query DTOs 