var msgDiv = document.getElementById("msg");
if (msgDiv) {
    var msg = msgDiv.textContent || msgDiv.innerText;
    if (msg) {
        alert(msg);
    }
}

