const xhr = new XMLHttpRequest();

function getToken() {
    return sessionStorage.getItem("token")
}

function isLoggedIn() {
    return getToken() != null;

}

function submitAppointmentForm() {

    if(getToken() === null) {
        alert("You need to login first!")
    }

    let data = {
        brand: document.getElementById("dose_select").value,
        date: document.getElementById("date_select").value,
        center: document.getElementById("center_select").value
    }

    xhr.open("POST", "/create-appointment",false)
    xhr.setRequestHeader("Content-Type","application/json")
    xhr.setRequestHeader("token", getToken())
    xhr.onload = function () {
        alert(JSON.stringify(this.response))
    }
    xhr.send(JSON.stringify(data));
}

function logout() {
    if(isLoggedIn() === false) {
        alert("You are not logged in.")
        return
    }
    xhr.open("POST", "/logout",false)
    xhr.setRequestHeader("token", getToken())
    xhr.onload = function () {
        sessionStorage.removeItem("token")
        alert(JSON.stringify(this.response))
    }
    xhr.send();
}