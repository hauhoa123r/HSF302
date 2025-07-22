import {MemberDTO} from "/templates/admin/assets/js/model/dto/MemberDTO.js";
import {Base64Utils} from "/templates/shared/assets/js/utils/base64-utils.js";
import {FetchingUtils} from "/templates/shared/assets/js/utils/fetching-utils.js";
import {FormDataUtils} from "/templates/shared/assets/js/utils/form-data.js";
import toast from "/templates/shared/assets/js/utils/toast.js";

const $ = document.querySelector.bind(document);
const $$ = document.querySelectorAll.bind(document);

document.addEventListener("DOMContentLoaded", async () => {
    await setupFormEvents();
});

async function setupFormEvents() {
    const editMemberForm = $("#add-member-form");
    if (!editMemberForm) return;
    editMemberForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        const formData = new FormData(editMemberForm);
        const memberDTO = FormDataUtils.getObjectFromFormData(new MemberDTO(), formData);
        if (memberDTO?.userEntity && memberDTO?.userEntity.avatar instanceof File) {
            memberDTO.userEntity.avatar = await Base64Utils.getBase64(memberDTO.userEntity.avatar);
        }
        const data = await FetchingUtils.fetch(`/api/admin/member`, {
            method: "POST",
            body: JSON.stringify(memberDTO),
            headers: {
                "Content-Type": "application/json"
            }
        }, "text");
        if (data !== null) {
            toast.success("Thêm thành viên thành công", {
                progress: true,
                duration: 3000,
                icon: true
            });
        }
    });
}