import {MemberDTO} from "/templates/admin/assets/js/model/dto/MemberDTO.js";
import {MemberResponse, renderResponseListForAdmin} from "/templates/admin/assets/js/model/response/MemberResponse.js";
import {FetchingUtils} from "/templates/shared/assets/js/utils/fetching-utils.js";
import {FormDataUtils} from "/templates/shared/assets/js/utils/form-data.js";
import {Pagination} from "/templates/shared/assets/js/utils/pagination.js";
import {SearchParamsUtils} from "/templates/shared/assets/js/utils/search-params-utils.js";

const $ = document.querySelector.bind(document);
const $$ = document.querySelectorAll.bind(document);

document.addEventListener("DOMContentLoaded", async () => {
    const renderMemberList = new RenderMemberList();
    await renderMemberList.fetchMemberList();
    renderMemberList.setupFormEvents();
});

class RenderMemberList {
    constructor() {
        this.memberDTO = new MemberDTO();
    }

    async fetchMemberList(pageIndex = 0) {
        const params = SearchParamsUtils.toSearchParams(this.memberDTO);
        const data = await FetchingUtils.fetch(`/api/admin/member/page/${pageIndex}?${params}`);
        this.renderMemberList(data);
        this.renderPagination(data);
    }

    renderMemberList(data) {
        const memberListElement = $("#member-list");
        if (!memberListElement) return;
        memberListElement.innerHTML = ""; // Clear previous content
        if (!data || !("members" in data) || !Array.isArray(data.members) || data.members.length === 0) {
            memberListElement.innerHTML = "<tr class='text-center'>Không tìm thấy thành viên nào.</tr>";
            return;
        }
        memberListElement.innerHTML = MemberResponse.fromJsonArray(data.members).map(member => member.setRenderStrategy(renderResponseListForAdmin).render()).join("");
    }

    renderPagination(data) {
        const paginationElement = $("#pagination");
        if (!paginationElement) return;
        paginationElement.innerHTML = ""; // Clear previous content

        if (!data || !("currentPage" in data) || !("totalPages" in data)) return;
        const pagination = new Pagination(data.currentPage, data.totalPages);
        paginationElement.innerHTML = pagination.render();
        pagination.setEvent(this.fetchMemberList.bind(this));
    }

    setupFormEvents() {
        const searchFormElement = $("#search-form");
        if (!searchFormElement) return;
        searchFormElement.addEventListener("submit", async (event) => {
            event.preventDefault();
            const formData = new FormData(searchFormElement);
            this.memberDTO = FormDataUtils.getObjectFromFormData(this.memberDTO, formData);
            await this.fetchMemberList();
        });

        const filterFormElement = $("#filter-form");
        if (!filterFormElement) return;
        filterFormElement.addEventListener("submit", async (event) => {
            event.preventDefault();
            const formData = new FormData(filterFormElement);
            this.memberDTO = FormDataUtils.getObjectFromFormData(this.memberDTO, formData);
            await this.fetchMemberList();
        });
        filterFormElement.addEventListener("reset", async () => {
            searchFormElement.reset();
            this.memberDTO = new MemberDTO();
            await this.fetchMemberList();
        });
    }
}
