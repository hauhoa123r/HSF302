document.getElementById("addEquipmentBtn").addEventListener("click", function () {
    const name = document.getElementById("equipmentName").value.trim();
    const code = document.getElementById("equipmentCode").value.trim();
    const type = document.getElementById("equipmentType").value;
    const brand = document.getElementById("equipmentBrand").value.trim();
    const model = document.getElementById("equipmentModel").value.trim();
    const serialNumber = document.getElementById("equipmentSerial").value.trim();
    const status = document.getElementById("equipmentStatus").value;
    const location = document.getElementById("equipmentLocation").value;
    const description = document.getElementById("equipmentDescription").value.trim();
    const nameCompany = document.getElementById("supplierName").value.trim();
    const phoneNumber = document.getElementById("supplierPhone").value.trim();
    const emailCompany = document.getElementById("supplierEmail").value.trim();
    const addressCompany = document.getElementById("supplierAddress").value.trim();
    const purchaseDate = document.getElementById("purchaseDate").value;
    const purchasePrice = document.getElementById("purchasePrice").value;
    const warrantyPeriod = document.getElementById("warrantyPeriod").value;

    if (!name || !code || !type || !brand || !purchaseDate || !location || !status) {
        alert("Vui lòng nhập đầy đủ các trường bắt buộc.");
        return;
    }

    const equipmentDTO = {
        name,
        code,
        type,
        brand,
        model,
        serialNumber,
        status,
        location,
        description,
        nameCompany,
        phoneNumber,
        emailCompany,
        addressCompany,
        purchaseDate,
        purchasePrice,
        warrantyPeriod
    };


    fetch("/api/admin/equipment/add", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(equipmentDTO)
    })
        .then(response => {
            if (response.ok) {
                alert("Thiết bị đã được thêm thành công!");
            } else {
                alert("Lỗi khi thêm thiết bị.");
            }
        });
});
