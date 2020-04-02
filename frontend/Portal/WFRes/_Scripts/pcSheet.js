var init = [];
$(function () {
    $("[id*=sheetContent]").show();
    //执行入口
    // console.log("start init : " + new Date());
    $.MvcSheet.Init();
    // console.log("end init : " + new Date());
    // 滚动
    // $("#form1").niceScroll({
    //     touchbehavior:false,     //是否是触摸式滚动效果
    //     cursorcolor:"rgba(0, 0, 0, 0.5)",     //滚动条的颜色值
    //     cursoropacitymax:0.9,   //滚动条的透明度值
    //     cursorwidth:8,         //滚动条的宽度值
    //     background:"rgba(0,0,0,0)",  //滚动条的背景色，默认是透明的
    //     autohidemode:true,      //滚动条是否是自动隐藏，默认值为 true
    // });
    // $("#form1").getNiceScroll(0).scrollend(function(e) {
    //     if(e.current.y > 100) {
    //         $(".back-top").show();
    //         $("#form1").getNiceScroll(0).resize();
    //     } else {
    //         $(".back-top").hide();
    //     }
    // });
    $(".back-top").bind('click', function(elment){
        $("#form1").getNiceScroll(0).doScrollTop(0, 100); // Scroll Y Axis
    });
})