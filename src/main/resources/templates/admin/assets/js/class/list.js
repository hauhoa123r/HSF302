document.addEventListener("DOMContentLoaded",  () =>  {
    document.getElementById("searchBtn").addEventListener("click", searchClass);
});

function searchClass() {
    const input = document.getElementById("keywordInput");
    const keyword = input.value.trim();
    if (!keyword) {
        alert("Vui lòng nhập từ khóa tìm kiếm.");
        return;
    }
    const url = `/admin/getAllClasses?className=${encodeURIComponent(keyword)}&page=0`;
    window.location.href = url;
}