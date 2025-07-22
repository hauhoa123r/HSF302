import {NotificationDTO} from "/templates/admin/assets/js/model/dto/NotificationDTO.js";
import {FetchingUtils} from "/templates/shared/assets/js/utils/fetching-utils.js";
import {FormDataUtils} from "/templates/shared/assets/js/utils/form-data.js";
import toast from "/templates/shared/assets/js/utils/toast.js";

document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("notify-form");
    form.addEventListener("submit", function (event) {
        event.preventDefault();
        const data = FetchingUtils.fetch(`/api/admin/notification`, {
            method: "POST",
            body: JSON.stringify(FormDataUtils.getObjectFromFormData(new NotificationDTO(), new FormData(form))),
            headers: {
                "Content-Type": "application/json"
            }
        }, "text");

        if (data !== null) {
            toast.success("Thông báo đã được gửi thành công!",
                    {
                        icon: true,
                        progress: true,
                        duration: 3000
                    });
            form.reset();
        }
    });
});
