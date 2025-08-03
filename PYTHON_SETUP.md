# Hướng dẫn chạy modul nhận diện comment độc hại
### Yêu cầu pyhton 3.10

### Mở cmd tại tệp tin toxic-comment-detect chạy lần lượt các lệnh
```shell
  python -m venv venv
```
```shell
  venv\Scripts\activate
```
```shell
  pip install -r requirement.txt
```
```shell
  uvicorn main:app --reload
```

Những lần tiếp theo chỉ cần chạy
```shell
  venv\Scripts\activate
```
```shell
  uvicorn main:app --reload
```

Giữ cmd để có thể chạy modul add comment