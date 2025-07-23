import {MemberDTO} from "/templates/admin/assets/js/model/dto/MemberDTO.js";
import {Base64Utils} from "/templates/shared/assets/js/utils/base64-utils.js";
import {EditUtils} from "/templates/shared/assets/js/utils/edit-utils.js";

document.addEventListener("DOMContentLoaded", async () => {
    const editUtils = new EditUtils({
        formSelector: "#edit-member-form",
        dtoInstance: new MemberDTO(),
        apiUrl: "/api/admin/member",
        prepareDto: async (dto) => {
            if (dto?.userEntity && dto?.userEntity.avatar instanceof File) {
                dto.userEntity.avatar = await Base64Utils.getBase64(dto.userEntity.avatar);
            }
            return dto;
        }
    });
    await editUtils.setupFormEvents();
});