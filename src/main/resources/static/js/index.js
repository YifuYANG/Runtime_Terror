const xhr= new XMLHttpRequest();

function getToken() {
    return sessionStorage.getItem("token")
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
    xhr.open("POST", "/logout",false)
    xhr.setRequestHeader("token", getToken())
    xhr.onload = function () {
        alert(this.response.data)
    }
    xhr.send();
}