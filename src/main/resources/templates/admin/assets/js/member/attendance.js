import {AttendanceDTO} from "/templates/admin/assets/js/model/dto/AttendanceDTO.js";
import {
    AttendanceResponse, renderAttendanceForAdmin
} from "/templates/admin/assets/js/model/response/AttendanceResponse.js";
import {FetchingUtils} from "/templates/shared/assets/js/utils/fetching-utils.js";
import {FormDataUtils} from "/templates/shared/assets/js/utils/form-data.js";
import {FormatUtils} from "/templates/shared/assets/js/utils/format-utils.js";
import {Pagination} from "/templates/shared/assets/js/utils/pagination.js";
import {SearchParamsUtils} from "/templates/shared/assets/js/utils/search-params-utils.js";

const $ = document.querySelector.bind(document);
const $$ = document.querySelectorAll.bind(document);

document.addEventListener("DOMContentLoaded", async () => {
    const attendance = new RenderAttendance();
    await attendance.fetchAttendance();
    attendance.setupFormEvents();
});

class RenderAttendance {
    constructor() {
        this.attendanceDTO = new AttendanceDTO();
    }

    async fetchAttendance(pageIndex = 0) {
        const params = SearchParamsUtils.toSearchParams(this.attendanceDTO);
        const data = await FetchingUtils.fetch(`/api/admin/attendance/page/${pageIndex}?${params}`);
        this.renderAttendance(data);
        this.renderPagination(data);
    }

    renderAttendance(data) {
        const attendanceList = $("#attendance-list");
        if (!attendanceList) return;
        attendanceList.innerHTML = "";
        if (!data || !("attendances" in data) || data.attendances.length === 0) return;
        attendanceList.innerHTML = AttendanceResponse.fromJsonArray(data.attendances)
                .map(attendance => attendance.setRenderStrategy(renderAttendanceForAdmin).render()).join("");
    }

    renderPagination(data) {
        const paginationElement = $("#pagination");
        if (!paginationElement) return;
        paginationElement.innerHTML = "";
        if (!data || !("currentPage" in data) || !("totalPages" in data)) return;
        const pagination = new Pagination(data.currentPage, data.totalPages);
        paginationElement.innerHTML = pagination.render();
        pagination.setEvent(this.fetchAttendance.bind(this));
    }

    setupFormEvents() {
        const filterForm = $("#filter-form");
        if (!filterForm) return;
        filterForm.addEventListener("submit", async (event) => {
            event.preventDefault();
            this.attendanceDTO = FormDataUtils.getObjectFromFormData(new AttendanceDTO(), new FormData(filterForm));
            if (this.attendanceDTO.checkInTime) {
                this.attendanceDTO.checkInTime = FormatUtils.formatTimeStamp(this.attendanceDTO.checkInTime);
            }
            if (this.attendanceDTO.checkOutTime) {
                this.attendanceDTO.checkOutTime = FormatUtils.formatTimeStamp(this.attendanceDTO.checkOutTime);
            }
            await this.fetchAttendance();
        });
        filterForm.addEventListener("reset", async () => {
            this.attendanceDTO = new AttendanceDTO();
            await this.fetchAttendance();
        });
    }
}