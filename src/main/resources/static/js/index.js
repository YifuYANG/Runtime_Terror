function indexLoading() {
    if(isLoggedIn()) {
        document.getElementById("loginButton").style.display = "none"
        document.getElementById("activityButton").style.display = "inline"
        document.getElementById("adminButton").style.display = "inline"
    }
    else {
        document.getElementById("loginButton").style.display = "inline"
        document.getElementById("activityButton").style.display = "none"
        document.getElementById("adminButton").style.display = "none"
    }
}

function iAmAdmin() {
    if(!isLoggedIn()) alert("Please login first!")
    let xhr = new XMLHttpRequest()
    xhr.open("GET", "/admin", false)
    xhr.setRequestHeader("token", getToken())
    xhr.onload = function () {
        document.body.innerHTML = this.responseText
    }
    xhr.send()
}

function allActivities(){
    if(!isLoggedIn()){
        alert("No user is login")
    }
    let xhr = new XMLHttpRequest()
    xhr.open("GET", "/activity", false)
    xhr.setRequestHeader("token", getToken())
    xhr.onload = function () {
        document.body.innerHTML = this.responseText
    }
    xhr.send()
}

function getToken() {
    return JSON.parse(sessionStorage.getItem("token"))
}

function isLoggedIn() {
    return getToken() != null;

}

function submitAppointmentForm() {

    let xhr = new XMLHttpRequest();

    if(getToken() === null) {
        alert("You need to login first!")
    }

    let data = {
        brand: document.getElementById("dose_select").value,
        date: document.getElementById("date_select").value,
        slot: document.getElementById("slot_select").value,
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

function submitAppointment2Form() {

    let xhr = new XMLHttpRequest();

    if(getToken() === null) {
        alert("You need to login first!")
    }

    let data = {
        brand: document.getElementById("dose_select").value,
        date: document.getElementById("date_select").value,
        center: document.getElementById("center_select").value
    }

    xhr.open("POST", "/create-second-appointment",false)
    xhr.setRequestHeader("Content-Type","application/json")
    xhr.setRequestHeader("token", getToken())
    xhr.onload = function () {
        alert(JSON.stringify(this.response))
    }
    xhr.send(JSON.stringify(data));
}

function logout() {
    if(!isLoggedIn()) {
        alert("You are not logged in.")
        return
    }
    let xhr = new XMLHttpRequest();
    xhr.open("GET", "/sign_out",false)
    xhr.setRequestHeader("token", getToken())
    xhr.onload = function () {
        sessionStorage.removeItem("token")
        document.location.reload()
    }
    xhr.send();
}

function approve(appointmentId, doseNumber) {
    let data = {
        appointmentId: appointmentId,
        doseNumber: doseNumber
    }
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/admin/update-appointment",false)
    xhr.setRequestHeader("Content-Type","application/json")
    xhr.setRequestHeader("token", getToken())
    xhr.onload = function () {
        alert(JSON.stringify(this.responseText))
        document.location.reload()
    }
    xhr.send(JSON.stringify(data));
}

function exit() {
    window.location.href = '/'
}

function renderVis() {
    let xhr = new XMLHttpRequest()
    xhr.open("GET", "/vis/dose-popularity",true)
    xhr.setRequestHeader("Content-Type","application/json")
    xhr.onload = function () {
        if(this.status === 200) {
            let spec = JSON.parse(this.responseText)
            vegaEmbed("#vis", spec)
        }
        else alert("Data Visualisation chart is unavailable, HTTP status code = " + this.status)
    }
    xhr.send()
}