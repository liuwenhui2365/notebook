javascript
===================

- HTML 中的脚本必须位于 < script>与< /script> 标签之间。脚本可被放置在 HTML 页面的 < body> 和 < head> 部分中。通常的做法是把函数放入 < head> 部分中，或者放在页面底部。这样就可以把它们安置到同一处位置，不会干扰页面的内容。

>注意：外部脚本不能包含 < script> 标签。
请使用 document.write() 仅仅向文档输出写内容。如果在文档已完成加载后执行 document.write，整个 HTML 页面将被覆盖。
在 JavaScript 中，用分号来结束语句是可选的。块由左花括号开始，由右花括号结束。块的作用是使语句序列一起执行。
注意：JavaScript 对大小写敏感。

- JavaScript会忽略多余的空格。您可以向脚本添加空格，来提高其可读性。您可以在文本字符串中使用反斜杠对代码行进行换行。

- 单行注释以 // 开头。与C语言一样 变量名称对大小写敏感（y 和 Y 是不同的变量）   JavaScript 语句和 JavaScript 变量都对大小写敏感。

- 当您向变量分配文本值时，应该用双引号或单引号包围这个值。当您向变量赋的值是数值时，不要使用引号。如果您用引号包围数值，该值会被作为文本来处理。如果重新声明 JavaScript 变量，该变量的值不会丢失。

- JavaScript 拥有动态类型。这意味着相同的变量可用作不同的类型，当您声明新变量时，可以使用关键词"new"来声明其类型,JavaScript 变量均为对象。当您声明一个变量时，就创建了一个新的对象。

- 函数就是包裹在花括号中的代码块，前面使用了关键词function：JavaScript 对大小写敏感。关键词 function 必须是小写的，并且必须以与函数名称相同的大小写来调用函数。如果您把值赋给尚未声明的变量，该变量将被自动作为全局变量声明。

- JavaScript for/in 语句循环遍历对象的属性.如需标记 JavaScript 语句，请在语句之前加上冒号：continue 语句（带有或不带标签引用）只能用在循环中。break 语句（不带标签引用），只能用在循环或 switch 中。通过标签引用，break 语句可用于跳出任何 JavaScript 代码块。

- throw 语句允许我们创建自定义错误。正确的技术术语是：创建或抛出异常（exception）。JavaScript 可用来在数据被送往服务器前对 HTML 表单中的这些输入数据进行验证。

###js html DMO

找到该元素。有三种方法来做这件事：
通过 id 找到 HTML 元素
通过标签名找到 HTML 元素
通过类名找到 HTML 元素

**onload 和 onunload 事件会在用户进入或离开页面时被触发。**

- onload 事件可用于检测访问者的浏览器类型和浏览器版本，并基于这些信息来加载网页的正确版本。onload 和 onunload 事件可用于处理cookie。

- JavaScript 提供多个内建对象，比如 String、Date、Array 等等。

- 对象只是带有属性和方法的特殊数据类型。

**在 JavaScript 中，不会创建类，也不会通过类来创建对象（就像在其他面向对象的语言中那样）。JavaScript 基于 prototype，而不是基于类的。**

###JS Window

- 所有 JavaScript 全局对象、函数以及变量均自动成为 window 对象的成员。全局变量是 window 对象的属性。全局函数是 window 对象的方法。

- screen.availHeight 属性返回访问者屏幕的高度，以像素计，减去界面特性，比如窗口任务栏。

- location.protocol 返回所使用的 web 协议（http:// 或 https://）cookie 是存储于访问者的计算机中的变量。每当同一台计算机通过浏览器请求某个页面时，就会发送这个 cookie。你可以使用 JavaScript 来创建和取回 cookie 的值。

###JS 库

jQuery 是目前最受欢迎的 JavaScript 框架。它使用 CSS 选择器来访问和操作网页上的 HTML 元素（DOM 对象）。

- jQuery 同时提供 companion UI（用户界面）和插件。为了引用某个库，请使用 < script> 标签，其 src 属性设置为库的 URL。

- Query 返回 jQuery 对象，与已传递的 DOM 对象不同。jQuery 对象拥有的属性和方法，与 DOM 对象的不同。您不能在 jQuery 对象上使用 HTML DOM 的属性和方法。
链接（Chaining）是一种在同一对象上执行多个任务的便捷方法。


