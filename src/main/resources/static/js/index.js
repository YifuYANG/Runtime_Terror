function getToken() {
    return sessionStorage.getItem("token")
}

function submitAppointmentForm() {

    if(getToken() === null) {
        alert("You need to login first!")
    }

    let data = {
        brand: document.getElementById("dose_select"),
        date: document.getElementById("date_select"),
        center: document.getElementById("center_select")
    }
    const xhr= new XMLHttpRequest();
    xhr.open("POST", "/create-appointment",false)
    xhr.setRequestHeader("Content-Type","application/json")
    xhr.setRequestHeader("token", getToken())
    xhr.onload = function () {
        alert(this.response.data)
    }
    xhr.send(JSON.stringify(data));
}