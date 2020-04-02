/// <reference path="../MvcSheetUI.js" />
//文本框(SheetTextBox/SheetBizTextBox/SheetTime)
(function ($) {
    // 控件执行
    // 参数{AutoTrim:true,DefaultValue:datavalue,OnChange:""}
    //可以通过  $("#id").SheetTextBox(参数)  来渲染控件和获取控件对象
    $.fn.SheetRichTextBox = function () {
        return $.MvcSheetUI.Run.call(this, "SheetRichTextBox", arguments);
    };

    // 构造函数
    $.MvcSheetUI.Controls.SheetRichTextBox = function (element, ptions, sheetInfo) {
        this.EditorObject = null;
        this.EditorIndex = 0;
        $.MvcSheetUI.Controls.SheetRichTextBox.Base.constructor.call(this, element, ptions, sheetInfo);
    };

    // 继承及控件实现
    $.MvcSheetUI.Controls.SheetRichTextBox.Inherit($.MvcSheetUI.IControl, {
        //控件渲染函数
        Render: function () {
            if (!this.Visiable) {
                $(this.Element).hide();
                return;
            }
            // 查看痕迹
            if (this.TrackVisiable && !this.Originate && this.DataField.indexOf(".") == -1) { this.RenderDataTrackLink(); }
            if (!this.Element.id) {
                alert("控件:" + this.DataField + "未设置ID");
                return;
            }
            //把属性重新渲染到页面上去
            // 移动端设置PlaceHolder
            if (this.IsMobile) {
                this.PlaceHolder = this.PlaceHolder || SheetLanguages.Current.PleaseInput;
                this.ionicInit($(this.Element), $.MvcSheetUI.IonicFramework);
            }
            $(this.Element).attr("PlaceHolder", this.PlaceHolder);
            //富文本在移动端显示为长文本样式
            if (this.RichTextBox && this.Editable && this.IsMobile) {
                this.RichTextBox = false;
            }

            var v;
            if ($.MvcSheetUI.SheetInfo.SheetMode == $.MvcSheetUI.SheetMode.Originate && !this.V) {
                v = this.DefaultValue || "";
            } else {
                v = this.V || "";
            }

            this.SetValue(v);

            if (this.RichTextBox && this.Editable && !this.IsMobile) {
                //if (typeof (CKEDITOR) == "undefined") {
                //    //动态加载
                //    $.ajax({
                //        url: _PORTALROOT_GLOBAL + "/WFRes/ckeditor/ckeditor.js",
                //        type: "GET",
                //        dataType: "script",
                //        async: false,//同步请求
                //        global: false
                //    });
                //    CKEDITOR.basePath = "http://" + location.host + _PORTALROOT_GLOBAL + "/WFRes/ckeditor/";
                //}

                // CKEDITOR.replace(this.Element.id);

                this.InitKindEditor();
            }
            //if (typeof (CKEDITOR) != "undefined" && this.RichTextBox && this.Editable) {  }
            if (!this.Editable) { this.SetReadonly(true); return; }

            if (this.ToolTip) {
                $(this.Element).attr("title", this.ToolTip);
            }

            $(this.Element).unbind("change.SheetRichTextBox").bind("change.SheetRichTextBox", [this], function (e) {
                e.data[0].Validate();
            });
        },
        ionicInit: function (element, ionic) {
            element.attr("rows", "4");
            element.parent().addClass("textarea");
        },
        InitKindEditor: function () {
            if (KindEditor == undefined) return;
            this.EditorIndex = KindEditor.instances.length;
            var index = this.EditorIndex;
            var sheetInfo = this.SheetInfo;
            KindEditor.SchemaCode = sheetInfo.SchemaCode;
            KindEditor.UserID = sheetInfo.UserID;
            KindEditor.BizObjectID = sheetInfo.BizObjectID;

			//update by luwei : 使用jsp上传
            this.EditorObject = KindEditor.create(this.Element, {
                cssPath: 'WFRes/editor/plugins/code/prettify.css',
                uploadJson: _PORTALROOT_GLOBAL + '/RichTextBox/uploadFileByRichTextBox',
                fileManagerJson: _PORTALROOT_GLOBAL + '/RichTextBox/fileManager',
                allowFileManager: true,
                items: [
                    'source', '|', 'undo', 'redo', '|', 'code', 'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter',
                    'justifyright', 'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
                    'superscript', 'clearhtml', 'selectall', '|', 'fullscreen', '/',
                    'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline', 'strikethrough',
                    'lineheight', 'removeformat', '|', 'image', 'table', 'hr', 'emoticons', 'anchor', 'link', 'unlink', '|', 'about'
                ],
                afterCreate: function () {
                    var self = this;
                    // 给KindEditor绑定paste事件，用于粘贴截图
                    $(self.edit.doc).on("paste", function (e) {
                        // 需支持HTML5
                        if (!(!!window.ActiveXObject || "ActiveXObject" in window) && typeof(Worker) === "undefined") {
                            return;
                        }

                        var itmes = (e.clipboardData || e.originalEvent.clipboardData).items,
                            blob = null,
                            i,
                            length;

                        for (i = 0, length = itmes.length; i < length; i++) {
                            if (itmes[i].type.indexOf("image") === 0) {
                                blob = itmes[i].getAsFile();
                                break;
                            }
                        }

                        if (blob) {
                            var data = new FormData();
                            data.append("imgFile", blob, "screenshot.png");
                            data.append("BizObjectID", sheetInfo.BizObjectID);
                            data.append("UserID", sheetInfo.UserID);
                            data.append("SchemaCode", sheetInfo.SchemaCode);
                            data.append("EditorIndex", index);

                            $.ajax({
                                url: _PORTALROOT_GLOBAL + "/RichTextBox/uploadFileByRichTextBox",
                                type: "POST",
                                data: data,
                                cache: false,
                                processData: false, // 告诉jQuery不要去处理发送的数据
                                contentType: false, // 告诉jQuery不要去设置Content-Type请求头
                                dataType: "json",
                                success: function (data) {
                                    if (data.error === 0) {
                                        self.KindEditor.instances[data.editorIndex].insertHtml("<img src=\"" + data.url + "\" alt=\"\" /> ");
                                    }
                                }
                            });
                        }
                    });
                }
            });
        },
        SetReadonly: function (v) {
            if (v) {
                $(this.Element).hide();

                //var parentContainer = $("<div class=\"SheetRichTextBox\"></div>");
                var parentContainer = $("<label class=\"SheetRichTextBox\"></div>");
                // style=\"overflow: auto;\"
                var contentValue = this.Element.value;
                //update by ouyangsk 调整white-space样式，处理AutoTrim无效的问题
                //parentContainer.html("<div style=\"overflow-x:auto;display:block;white-space:normal;padding:7px 0;\" id=\"divRich_" + this.Element.id + "\">" + contentValue + "</div>")
                //    .insertAfter($(this.Element));

                 //update by xl@Future
                //去除<>转义 解决长文本内容 不换行问题
                // contentValue = contentValue.replace(/\"/g,"&quot;");
                // contentValue = contentValue.replace(/\</g,"&lt;").replace(/\>/g,"&gt;").replace(/\"/g,"&quot;");
                // 对于富文本 有两种方案
                // 1.过滤标签
                // 2.列白名单加属性
                // var html = filterXSS(contentValue,{
                //     onTag : function (tag, html, options) {
                //         console.log(tag, html, options)
                //         if (tag === 'script') {
                //             // 不对其属性列表进行过滤
                //             return filterXSS(html);
                //         } else {
                //             return html
                //         }
                //     }
                // });
                var html = contentValue;
                // var html = filterXSS(contentValue,{
                //      whiteList: {
                //          a: ["target", "href", "title","style","class","name"],
                //          abbr: ["title","style","class"],
                //          address: ["style","class"],
                //          area: ["shape", "coords", "href", "alt","style","class"],
                //          article: ["style","class"],
                //          aside: ["style","class"],
                //          audio: ["autoplay", "controls", "loop", "preload", "src","style","class"],
                //          b: ["style","class"],
                //          bdi: ["dir","style","class"],
                //          bdo: ["dir","style","class"],
                //          big: ["style","class"],
                //          blockquote: ["cite","style","class"],
                //          br: ["style","class"],
                //          caption: ["style","class"],
                //          center: ["style","class"],
                //          cite: ["style","class"],
                //          code: ["style","class"],
                //          col: ["align", "valign", "span", "width","style","class"],
                //          colgroup: ["align", "valign", "span", "width","style","class"],
                //          dd: ["style","class"],
                //          del: ["datetime","style","class"],
                //          details: ["open","style","class"],
                //          div: ["style","class"],
                //          dl: ["style","class"],
                //          dt: ["style","class"],
                //          em: ["style","class"],
                //          font: ["color", "size", "face","style","class"],
                //          footer: ["style","class"],
                //          h1: ["style","class"],
                //          h2: ["style","class"],
                //          h3: ["style","class"],
                //          h4: ["style","class"],
                //          h5: ["style","class"],
                //          h6: ["style","class"],
                //          header: ["style","class"],
                //          hr: ["style","class"],
                //          i: ["style","class"],
                //          img: ["src", "alt", "title", "width", "height","style","class"],
                //          ins: ["datetime","style","class"],
                //          li: ["style","class"],
                //          mark: ["style","class"],
                //          nav: ["style","class"],
                //          ol: ["style","class"],
                //          p: ["style","class"],
                //          pre: ["style","class"],
                //          s: ["style","class"],
                //          section: ["style","class"],
                //          small: ["style","class"],
                //          span: ["style","class"],
                //          sub: ["style","class"],
                //          sup: ["style","class"],
                //          strong: ["style","class"],
                //          table: ["width", "border", "align", "valign","style","class"],
                //          tbody: ["align", "valign","style","class"],
                //          td: ["width", "rowspan", "colspan", "align", "valign","style","class"],
                //          tfoot: ["align", "valign","style","class"],
                //          th: ["width", "rowspan", "colspan", "align", "valign","style","class"],
                //          thead: ["align", "valign","style","class"],
                //          tr: ["rowspan", "align", "valign","style","class"],
                //          tt: ["style","class"],
                //          u: ["style","class"],
                //          ul: ["style","class"],
                //          video: ["autoplay", "controls", "loop", "preload", "src", "height", "width","style","class"]
                //      }
                // });

                parentContainer.html("<div style=\"overflow-x:auto;display:block;white-space:inherit;padding:7px 0;\" id=\"divRich_" + this.Element.id + "\">" + html + "</div>")
                	.insertAfter($(this.Element));

                if (this.IsMobile) {
                    var defaults = { ox: 0, oy: 0, cx: 0, cy: 0 };
                    this.Element.addEventListener("touchstart", function () {
                        defaults.ox = event.targetTouches[0].pageX;
                        defaults.oy = event.targetTouches[0].pageY;
                        //console.log("ox->" + defaults.ox + ",oy->" + defaults.oy);
                        //if (window.navigator.userAgent.toLowerCase().indexOf("android") == -1) {
                        //    $(this).parent().trigger("touchstart");
                        //    event.stopPropagation();
                        //}
                    });
                    this.Element.addEventListener("touchmove", function () {
                        defaults.cx = event.targetTouches[0].pageX;
                        defaults.cy = event.targetTouches[0].pageY;
                        // 左右滑动大于上下滑动
                        if (Math.abs(defaults.cy - defaults.oy) < Math.abs(defaults.cx - defaults.ox)) {
                            event.stopPropagation();
                        }
                    });
                }

                parentContainer.find("p").each(function () {
                    var indent = $(this).css("text-indent");
                    if (indent) {
                        indent = parseInt(indent.replace("px", ""));
                        if (indent < 0) {
                            $(this).css("text-indent", 0);
                        }
                    }
                });
                //.html(this.Element.value)
                //.insertAfter($(this.Element));
            } else {
                $(this.Element).show();
                if ($(this.Element).next().hasClass("Readonly")) $(this.Element).next().remove();
            }
        },
        // 数据验证
        Validate: function (effective, initValid) {
            if (!this.Editable) return true;
            if (initValid) {
                if (this.Required && !this.GetValue()) {
                    this.AddInvalidText(this.Element, "*", false);
                    return false;
                }
            }
            if (!effective) {
                // 必填验证
                var v = this.GetValue();
                if (this.Required && !this.DoValidate(this.Valid.Required, [v], "*")) {
                    return false;
                }
                if (this.RegularExpression) {
                    if (!this.DoValidate(this.Valid.RegularExpression, [v, this.RegularExpression], this.RegularInvalidText)) {
                        return false;
                    }
                }
            }
            return true;
        },
        // 获取值
        GetValue: function () {
            var value;
            if (this.EditorObject != null && this.EditorObject != "" && typeof (this.EditorObject) != "undefined" && this.RichTextBox) { // 富文本框
                value = this.EditorObject.html();
            } else {
                value = $(this.Element).val();
            }
            if (this.AutoTrim) {
                value = value.trim()
            }
            return value;
        },
        // 设置值
        SetValue: function (val) {
        // if(this.RichTextBox){
        // 		//update by xl@Future
        // 		val = val ? val.replace(/\</g,"&lt;"):val;
        // 		val = val ? val.replace(/\>/g,"&gt;"):val;
        // 		val = val ? val.replace(/\"/g,"&quot;"):val;
        // 	}
            if (this.RichTextBox && this.EditorObject) { // 富文本框
                this.EditorObject.html(val);
            } else {
                if (!this.RichTextBox && !this.Editable) {
                    val = val.replace(/\n/g, "<br>");
                }
                // console.log(val,'val');
                $(this.Element).val(val);
            }
        },
        // 设置焦点
        SetFocus: function () {
            if (this.RichTextBox) { // 富文本框
                // return CKEDITOR.instances[this.Element.id].focus();
                this.EditorObject.focus();
            } else {
                return this.Element.focus();
            }
        },
        //返回数据值
        SaveDataField: function () {
            var result = {};
            if (!this.Editable) return result;
            result[this.DataField] = this.SheetInfo.BizObject.DataItems[this.DataField];
            if (!result[this.DataField]) {
                if (this.DataField.indexOf(".") == -1) { alert(SheetLanguages.Current.DataItemNotExists + ":" + this.DataField); }
                return {};
            }
            var v = this.GetValue();
            if (result[this.DataField].V != v) {
                result[this.DataField].V = v;
                return result;
            }
            return {};
        }
    });
})(jQuery);