import {MemberDTO} from "/templates/admin/assets/js/model/dto/MemberDTO.js";
import {MemberResponse, renderResponseListForAdmin} from "/templates/admin/assets/js/model/response/MemberResponse.js";
import {RenderUtils} from "/templates/shared/assets/js/utils/render-utils.js";

document.addEventListener("DOMContentLoaded", async () => {
    const renderUtils = new RenderUtils(
            {
                dtoInstance: new MemberDTO(),
                responseClass: MemberResponse,
                apiUrl: "/api/admin/member",
                renderStrategy: renderResponseListForAdmin,
                formSelector: ["#filter-form", "#search-form"]
            }
    );
    await renderUtils.init();
});
