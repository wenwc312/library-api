# 圖書館管理系統 Library API
一個使用 Spring Boot 開發的簡易圖書館系統，提供借書、還書、查詢可借閱書籍及搜尋書籍功能，並附有網頁操作介面。

🔗 **線上展示**：https://library-api-wsp0.onrender.com/index.html

> 使用Render免費部署方案，閒置一段時間後服務會修免，第一次開啟可能需要等待約1分鐘

## 功能

- 借書:輸入會員編號與ISBN借閱書籍
- 還書:歸還已借閱的書籍
- 查看可借閱書籍清單
- 依書名、作者或ISBN搜尋書籍，並顯示借閱狀態

## 使用技術

| 類別 | 技術 |
|---|---|
| 後端 | Java 21、Spring Boot |
| 前端 | HTML、CSS、JavaScript(fetch API) |
| 建置工具 | Maven |
| 部署 | Docker、Render |

## API 一覽
| 方法   | 路徑                            | 說明         |
|------|-------------------------------|------------|
| GET  | `/books/available`            | 取得可借閱書籍清單  |
| GET  | `/books/search?keyword=`      | 依關鍵字搜尋書籍 |
| POST | `/books/borrow?userId=&isbn=` | 借書 |
| POST | `/books/return?userId=&isbm=` | 還書 |

## 測試資料
系統啟動時會自動建立以下資料

**會員**:`U0001`、`U0002`

**書籍**:

| 書名 | 作者 | ISBN |
|---|---|---|
| 野性的呼喚 | john | s0001 |
| 侏儸紀公園 | mary | s0002 |
| 哈利波特 | JK | f0001 |
| 三國演義 | 羅貫中 | h0001 |

## 本機執行

需要先安裝 Java 21。

```bash
git clone https://github.com/wenwc312/library-api.git
cd library-api
./mvnw spring-boot:run
```

啟動後開啟瀏覽器前往 http://localhost:8080/index.html

## 專案結構

```
src/main/java/org/example/library_api/
├── controller/ # 接收 HTTP 請求
├── service/ # 商業邏輯
└── model/ # 資料模型（Book、Member）
src/main/resources/static/
└── index.html # 前端頁面
```

## 未來規劃

- [] 串接資料庫，讓資料在重啟後保留
- [] 新增會員與新增書籍功能
- [] 美化前端介面
