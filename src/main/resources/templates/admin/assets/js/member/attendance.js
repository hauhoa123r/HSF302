import {AttendanceDTO} from "/templates/admin/assets/js/model/dto/AttendanceDTO.js";
import {
    AttendanceResponse, renderAttendanceForAdmin
} from "/templates/admin/assets/js/model/response/AttendanceResponse.js";
import {RenderUtils} from "/templates/shared/assets/js/utils/render-utils.js";

document.addEventListener("DOMContentLoaded", async () => {
    const renderUtils = new RenderUtils({
        dtoInstance: new AttendanceDTO(),
        responseClass: AttendanceResponse,
        apiUrl: "/api/admin/attendance",
        renderStrategy: renderAttendanceForAdmin
    });
    await renderUtils.init();
});