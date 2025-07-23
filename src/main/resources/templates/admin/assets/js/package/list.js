import {PackageDTO} from "/templates/admin/assets/js/model/dto/PackageDTO.js";
import {PackageResponse, renderPackageForAdmin} from "/templates/admin/assets/js/model/response/PackageResponse.js";
import {RenderUtils} from "/templates/shared/assets/js/utils/render-utils.js";

document.addEventListener("DOMContentLoaded", async () => {
    const renderUtils = new RenderUtils(
            {
                dtoInstance: new PackageDTO(),
                responseClass: PackageResponse,
                apiUrl: "/api/admin/package",
                renderStrategy: renderPackageForAdmin
            }
    );
    await renderUtils.init();
});