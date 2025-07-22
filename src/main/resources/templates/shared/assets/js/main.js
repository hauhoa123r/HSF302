(function ($) {
    "use strict";

    // Preloader
    $(window).on("load", function () {
        $("#preloader").fadeOut();
    });

    // Sticky Header
    $(window).on("scroll", function () {
        var scroll = $(window).scrollTop();
        if (scroll >= 100) {
            $(".header-area").addClass("header-sticky");
        } else {
            $(".header-area").removeClass("header-sticky");
        }
    });

    // Mobile Navigation
    $(".menu-trigger").on("click", function () {
        $(".main-nav").toggleClass("active");
        $(".menu-trigger").toggleClass("active");
    });

    // Scroll to Section
    $(".scroll-to-section a").on("click", function (e) {
        e.preventDefault();
        var target = $(this).attr("href");
        $("html, body").animate(
            {
                scrollTop: $(target).offset().top - 80,
            },
            1000
        );
    });

    // Set active menu item
    $(".nav a").on("click", function () {
        $(".nav").find("a.active").removeClass("active");
        $(this).addClass("active");
    });

    // BMI Calculator
    $("#bmi-submit").on("click", function (e) {
        e.preventDefault();
        var height = $("#bmi-height").val();
        var weight = $("#bmi-weight").val();

        if (height && weight) {
            var bmi = weight / ((height / 100) * (height / 100));
            var result = "BMI của bạn: " + bmi.toFixed(2) + " - ";

            if (bmi < 18.5) {
                result += "Thiếu cân";
            } else if (bmi >= 18.5 && bmi < 25) {
                result += "Bình thường";
            } else if (bmi >= 25 && bmi < 30) {
                result += "Thừa cân";
            } else {
                result += "Béo phì";
            }

            $("#bmi-result").html(result).show();
        }
    });
})(jQuery);
