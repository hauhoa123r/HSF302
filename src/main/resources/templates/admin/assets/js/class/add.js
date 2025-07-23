document.getElementById("addClassBtn").addEventListener("click", function () {
    const className = document.getElementById("className").value.trim();
    const classType = document.getElementById("classType").value;
    const classLevel = document.getElementById("classLevel").value;
    const description = document.getElementById("description").value.trim();
    const trainerId = document.getElementById("trainerSelect").value;
    const startTime = document.getElementById("startTime").value;
    const endTime = document.getElementById("endTime").value;
    const capacity = document.getElementById("capacity").value;

    if (!className || !classType || !classLevel || !trainerId || !startTime || !endTime || !capacity) {
        alert("Vui lòng nhập đầy đủ thông tin!");
        return;
    }

    const payload = {
        className,
        classType,
        classLevel,
        description,
        trainerId: parseInt(trainerId),
        startTime,
        endTime,
        capacity: parseInt(capacity)
    };
    console.log(payload);
    fetch("/api/admin/class/add", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    })
        .then(res => {
            if (!res.ok) throw new Error("Gửi dữ liệu thất bại");
            return res.text();
        })
        .then(msg => {
            alert(msg);
            window.location.href = "/admin/getAllClasses";
        })
        .catch(err => alert("Lỗi: " + err.message));
});