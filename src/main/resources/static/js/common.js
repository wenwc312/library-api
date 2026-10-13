// 用 POST 送出 JSON 資料
const API_BASE = "/books";

function postJson(url, data) {
    return fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    });
}

// 把書籍陣列顯示在清單裡
function renderBookList(listElement, books, emptyMessage, showStatus = false) {
    listElement.innerHTML = "";

    if (books.length === 0) {
        listElement.innerHTML = `<li>${emptyMessage}</li>`;
        return;
    }

    books.forEach(book => {
        const li = document.createElement("li");
        let text = `《書名:${book.name} - 作者:${book.author} - ISBN:(${book.ISBN})》`;
        if (showStatus) {
            text += ` 狀態:${book.borrowed ? "[已借出]" : "[可借閱]"}`;
        }
        li.innerText = text;
        listElement.appendChild(li);
    });
}

// 顯示操作結果
function showMessage(element, message, type){
    element.innerText = message;
    element.className = "message " + type;
    void element.offsetWidth; // 讓瀏覽器重新計算，動畫才能重新播放
    element.classList.add("flash");
}

// 連線失敗時顯示提示
function showNetworkError(element){
    showMessage(element,"連線失敗，請確認網路或稍後再試","error");
}