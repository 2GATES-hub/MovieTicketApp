# 🎬 Movie Ticket App

Ứng dụng đặt vé xem phim Android sử dụng **Firebase** làm backend.

## 📱 Màn hình ứng dụng

| Đăng nhập | Danh sách phim | Chi tiết phim | Đặt vé | Vé của tôi |
|-----------|----------------|---------------|--------|------------|
| Login screen | Movie list | Movie detail | Booking | My tickets |

## ✅ Chức năng đã làm

- [x] **Đăng ký / Đăng nhập** bằng Firebase Authentication (Email & Password)
- [x] **Xem danh sách phim** từ Cloud Firestore (auto seed dữ liệu mẫu)
- [x] **Xem chi tiết phim** (poster, thể loại, thời lượng, mô tả, đánh giá)
- [x] **Đặt vé** – chọn suất chiếu + số ghế + xác nhận
- [x] **Lưu ticket trên Firebase** Firestore (collection: tickets)
- [x] **Push Notification** qua Firebase Cloud Messaging (FCM)
- [x] **Xem lịch sử vé** đã đặt

## 🗄️ Cấu trúc Firestore

```
users/
  {uid}/
    name, email, uid, fcmToken

movies/
  {movieId}/
    title, genre, duration, description, posterUrl, rating

showtimes/
  {showtimeId}/
    movieId, theaterId, theaterName, date, time, availableSeats, price

tickets/
  {ticketId}/
    userId, movieId, movieTitle, showtimeId, theaterName,
    date, time, seatCount, totalPrice, bookingTime, status, fcmToken
```

## 🔧 Công nghệ sử dụng

- **Language**: Java
- **Min SDK**: 24 (Android 7.0)
- **Firebase Authentication** – đăng nhập email/password
- **Cloud Firestore** – lưu phim, suất chiếu, vé
- **Firebase Cloud Messaging (FCM)** – push notification
- **Glide** – load ảnh poster từ URL
- **Material Design Components** – UI

## 🚀 Cách chạy dự án

### Bước 1: Tạo Firebase Project

1. Vào [https://console.firebase.google.com](https://console.firebase.google.com)
2. Click **"Add project"** → đặt tên → tạo project
3. Trong project, vào **"Project settings"** (biểu tượng bánh răng)
4. Tab **"General"** → scroll xuống → click **"Add app"** → chọn Android
5. Điền **Package name**: `com.example.movieticket`
6. Click **"Register app"** → **Download `google-services.json`**
7. Đặt file `google-services.json` vào thư mục `app/`

### Bước 2: Bật Firebase Services

**Authentication:**
- Firebase Console → Authentication → Get started
- Sign-in method → Enable **Email/Password**

**Firestore:**
- Firebase Console → Firestore Database → Create database
- Chọn **"Start in test mode"** (cho phép đọc/ghi tự do)
- Chọn region gần nhất (asia-southeast1)

**Cloud Messaging:**
- Tự động được bật khi tạo project

### Bước 3: Clone và chạy

```bash
git clone https://github.com/YOUR_USERNAME/MovieTicketApp.git
cd MovieTicketApp

# Copy file google-services.json vào app/
cp /path/to/google-services.json app/

# Mở bằng Android Studio và Run
```

### Bước 4: Test Push Notification

1. Chạy app → đăng nhập
2. Firebase Console → **Cloud Messaging** → **Send your first message**
3. Notification title: "Phim mới ra mắt!"
4. Text: "Avengers mới đã có lịch chiếu. Đặt vé ngay!"
5. Target → Topic: `movies`
6. Send → app sẽ nhận notification

## 📁 Cấu trúc project

```
app/src/main/
├── java/com/example/movieticket/
│   ├── activities/
│   │   ├── LoginActivity.java
│   │   ├── RegisterActivity.java
│   │   ├── MainActivity.java          ← Danh sách phim
│   │   ├── MovieDetailActivity.java
│   │   ├── BookingActivity.java       ← Đặt vé
│   │   └── MyTicketsActivity.java
│   ├── adapters/
│   │   ├── MovieAdapter.java
│   │   └── TicketAdapter.java
│   ├── models/
│   │   ├── Movie.java
│   │   ├── Showtime.java
│   │   └── Ticket.java
│   └── utils/
│       └── MyFirebaseMessagingService.java  ← FCM handler
└── res/
    ├── layout/         ← Các XML giao diện
    ├── drawable/       ← Icon, placeholder
    ├── menu/           ← Menu toolbar
    └── values/         ← Colors, strings, themes
```

## ⚠️ Lưu ý

- File `google-services.json` **không được commit** lên GitHub (đã thêm vào .gitignore)
- Mỗi người cần tự tạo Firebase project của mình và đặt file `google-services.json` vào thư mục `app/`
- Firestore rules nên đặt **test mode** khi học tập (không dùng cho production)
