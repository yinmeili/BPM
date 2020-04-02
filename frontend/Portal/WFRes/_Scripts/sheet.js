

var IsMobile = getUrlParam("IsMobile");
var local = window.localStorage.getItem("H3.Language"); // 本地语言
// console.log(local, '_localLan');
var _localLan = local || 'zh_cn';
window.localStorage.setItem("H3.Language", _localLan);

var _PORTALROOT_GLOBAL = "/Portal";
if (typeof (pageInfo) != "undefined") {
    pageInfo.LockImage = "WFRes/images/WaitProcess.gif";
}
window.localStorage.setItem("H3.PortalRoot", _PORTALROOT_GLOBAL);
var OnSubmitForm = function () {
    if (IsMobile) {
        return false;
    }
    return true;
}
//获取表单html
getSheetHtml();

function getSheetHtml (){
    $.ajax({
        method:"POST",
        url:"/Portal/MvcDefaultSheet/getHtmlContent"+location.search,
        async:false,
        success:function(res){
            if(res.code==200){
                $("#content-wrapper").html(res.data);
            }
            else if(res.code=="401") // 未登录
            {
                if (!getUrlParam("IsMobile"))
                    location.href = "index.html"
                else {
                    location.href = "Mobile/index.html"
                }
            }
        }
    })
}


$(function () {
    function load() {
        line.animate(1, {
            duration: 300
        }, function() {
            line.destroy();
            $('#container').remove()
        });
        console.timeEnd("结束时间");
    }
    /* 绑定click方法 TODO 可能其他方法也需要绑定 */
    $("[data-onclick]").each(function () {
        var functionString = $(this).data("onclick");
        $(this).bind('click', function () {
            eval(functionString);
        })
    });
    /* OnlyData 不显示 label */
    $("#divSheet").find("[data-bindtype]").each(function () {
        //系统字段
        var sysKeyWords = ["ActivityCode", "ActivityName",
            "InstanceId", "InstanceName", "InstancePriority",
            "OriginateTime", "SequenceNo", "Originator",
            "Originator.FullName", "Originator.LoginName",
            "Originator.UserName", "Originator.UserID",
            "Originator.Email", "Originator.EmployeeNumber",
            "Originator.EmployeeRank",
            "Originator.Appellation", "Originator.OfficePhone",
            "Originator.Mobile", "Originator.OUName",
            "Originator.OUFullName", "Originator.OU"
        ];
        var datafield = $(this).data("datafield");
        if (sysKeyWords.indexOf(datafield)) {
            return;
        }
        if ($(this).data("bindtype") == "OnlyData") {
            $(this).hide();
        }
    });
    var speed = 200;
    $('body,html').animate({scrollTop: 0}, speed);

    $('div.bannerTitle').next().addClass("divContent-title-show");
    $('div.bannerTitle').bind('click', function(elment){
        var ndiv = $(this).next();
        if(ndiv.hasClass("divContent-title-show")) {
            // 执行隐藏
            ndiv.slideUp(100).removeClass("divContent-title-show");
            $(this).find('.aufontAll').removeClass("expanded");
            setTimeout(function(){
                $("#form1").getNiceScroll(0).resize();
            },300)
        } else {
            // 显示
            $(this).find('.aufontAll').addClass("expanded");
            ndiv.slideDown(100).addClass("divContent-title-show");
            setTimeout(function(){
                $("#form1").getNiceScroll(0).resize();
            },300)
        }
    });

    LazyLoad.js([
        'js/jQuery.md5.min.js?v=<%=Version%>',
        'js/sweetalert.min.js?v=<%=Version%>'
    ]);
    var u = navigator.userAgent;
    var ios = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
    if(ios) {
        document.body.addEventListener('touchmove', function (e) {
            e.preventDefault() // 阻止默认的处理方式(阻止下拉滑动的效果)
        }, {passive: false}) // passive 参数不能省略，用来兼容ios和android
    } else {
        document.body.addEventListener('touchmove', function (e) {
            if(!e.cancelable) {
                return true
            }
            // e.preventDefault() // 阻止默认的处理方式(阻止下拉滑动的效果)
        }, {passive: false}) // passive 参数不能省略，用来兼容ios和android
    }
    var end = new Date().getTime();
    var duration = (end - start)/1000;
    load(duration);
})

function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
};