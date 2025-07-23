import {PackageDTO} from "/templates/admin/assets/js/model/dto/PackageDTO.js";
import {EditUtils} from "/templates/shared/assets/js/utils/edit-utils.js";

document.addEventListener("DOMContentLoaded", async () => {
    const editUtils = new EditUtils({
        dtoInstance: new PackageDTO(),
        apiUrl: "/api/admin/package"
    });
    await editUtils.setupFormEvents();
});