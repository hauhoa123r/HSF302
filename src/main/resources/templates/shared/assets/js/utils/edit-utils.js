import {FetchingUtils} from "/templates/shared/assets/js/utils/fetching-utils.js";
import {FormDataUtils} from "/templates/shared/assets/js/utils/form-data.js";
import toast from "/templates/shared/assets/js/utils/toast.js";

export class EditUtils {
    /**
     * EditUtils constructor
     * @param {string} formSelector
     * @param dtoInstance
     * @param {string} apiUrl
     * @param {function} prepareDto
     */
    constructor({
                    formSelector = "#form", dtoInstance, apiUrl, prepareDto
                }) {
        this.form = document.querySelector(formSelector);
        this.dtoInstance = dtoInstance;
        this.apiUrl = apiUrl;
        this.prepareDto = prepareDto;
    }

    async setupFormEvents() {
        if (!this.form) return;
        this.form.addEventListener("submit", async (event) => {
            event.preventDefault();
            const formData = new FormData(this.form);
            let dto = FormDataUtils.getObjectFromFormData(this.dtoInstance, formData);
            if (this.prepareDto) {
                dto = await this.prepareDto(dto);
            }
            const data = await FetchingUtils.fetch(`${this.apiUrl}/${dto.id}`, {
                method: "PUT",
                body: JSON.stringify(dto),
                headers: {
                    "Content-Type": "application/json"
                }
            }, "text");
            if (data !== null) {
                toast.success("Cập nhật thành công", {
                    progress: true,
                    duration: 3000,
                    icon: true
                });
            }
        });
    }
}