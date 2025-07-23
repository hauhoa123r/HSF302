import {FetchingUtils} from "/templates/shared/assets/js/utils/fetching-utils.js";
import {FormDataUtils} from "/templates/shared/assets/js/utils/form-data.js";
import {Pagination} from "/templates/shared/assets/js/utils/pagination.js";
import {SearchParamsUtils} from "/templates/shared/assets/js/utils/search-params-utils.js";

export class RenderUtils {
    /**
     * RenderUtils is a utility class for rendering lists and pagination
     * @param dtoInstance
     * @param responseClass
     * @param {string} apiUrl
     * @param {function} renderStrategy
     * @param {string[]} formSelector
     * @param {string} listSelector
     * @param {string} paginationSelector
     */
    constructor(
            {
                dtoInstance, responseClass, apiUrl, renderStrategy, formSelector: formSelector = ["#filter-form"],
                listSelector = "#list", paginationSelector = "#pagination"
            }
    ) {
        this.dtoInstance = dtoInstance;
        this.responseClass = responseClass;
        this.apiUrl = apiUrl;
        this.renderStrategy = renderStrategy;
        this.formSelector = formSelector;
        this.listSelector = listSelector;
        this.paginationSelector = paginationSelector;
    }

    async init() {
        await this.fetchData();
        this.setupFormEvents();
    }

    async fetchData(pageIndex = 0) {
        const params = SearchParamsUtils.toSearchParams(this.dtoInstance);
        const data = await FetchingUtils.fetch(`${this.apiUrl}/page/${pageIndex}?${params}`);
        this.renderList(data);
        this.renderPagination(data);
    }

    renderList(data) {
        const listElement = document.querySelector(this.listSelector);
        if (!listElement) return;
        listElement.innerHTML = ""; // Clear previous content
        if (!data || !("items" in data) || !Array.isArray(data.items) || data.items.length === 0) return;
        listElement.innerHTML = this.responseClass.fromJsonArray(data.items)
                .map(item => item.setRenderStrategy(this.renderStrategy).render())
                .join("");
    }

    renderPagination(data) {
        const paginationElement = document.querySelector(this.paginationSelector);
        if (!paginationElement) return;
        paginationElement.innerHTML = ""; // Clear previous content

        if (!data || !("currentPage" in data) || !("totalPages" in data)) return;
        const pagination = new Pagination(data.currentPage, data.totalPages);
        paginationElement.innerHTML = pagination.render();
        pagination.setEvent(this.fetchData.bind(this));
    }

    setupFormEvents() {
        this.formSelector.forEach((selector) => {
            const formElement = document.querySelector(selector);
            if (!formElement) return;
            formElement.addEventListener("submit", async (event) => {
                event.preventDefault();
                const formData = new FormData(formElement);
                this.dtoInstance = FormDataUtils.getObjectFromFormData(this.dtoInstance, formData);
                await this.fetchData();
            });
            formElement.addEventListener("reset", async (event) => {
                event.preventDefault();
                this.dtoInstance = new this.dtoInstance.constructor(); // Reset to default instance
                await this.fetchData();
            });
        });
    }
}