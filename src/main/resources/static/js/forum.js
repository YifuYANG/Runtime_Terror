function createPost() {
    let xhr = new XMLHttpRequest()
    let data = {
        content: document.getElementById("queryInputArea").value
    }
    xhr.open("POST", "/forum/post",false)
    xhr.setRequestHeader("Content-Type","application/json")
    xhr.setRequestHeader("token", getToken())
    xhr.onload = function () {
        alert(this.responseText)
        document.location.reload()
    }
    xhr.send(JSON.stringify(data))
}