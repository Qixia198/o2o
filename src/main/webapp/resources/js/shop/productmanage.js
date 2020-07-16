$(function () {
    var productListUrl = "/o2o/shop/getpoductlistbyshop?pageIndex=1&pageSize=999";
    var editUrl = "/o2o/shopadmin/productoperation?productId=";
    var statusUrl = "/o2o/shop/modifyproduct";
    getList();

    /**
     * 获取该店铺得商品列表
     */
    function getList(){
        $.getJSON(productListUrl,function (data) {
            var productList = data.page.list;
            var html = "";
            productList.map(function (item, index){
                var textOp = "下架";
                var contraryStatus = 0;
                if(item.enableStatus == 0){
                    textOp = "上架";
                    contraryStatus = 1;
                }else{
                    textOp = "下架";
                    contraryStatus = 0;
                }
                html += "<div class='row row-product'>"+
                    "<div class='col-33'>" + item.productName + "</div>" +
                    "<div class='col-20'>" + item.priority + "</div>" +
                    "<div class='col-40'>" +
                    "<a href='#'class='edit' data-id='"+ item.productId +"'>编辑</a>" +
                    "<a href='#' class='status' data-id='"+ item.productId +"' data-status='"+ contraryStatus +"'>"+ textOp +"</a>" +
                    "<a href='#' class='preview' data-id='"+ item.productId +"' data-status='"+ item.enableStatus +"'>预览</a>" +
                    "</div>" + "</div>";
            });
            $(".product-wrap").append(html);
        });
    }

    //绑定单击事件
    $(".product-wrap").on("click","a",function (e) {
        var target = $(e.currentTarget);
        if(target.hasClass("edit")){
            //编辑，并带有productId参数
            window.location.href = editUrl + e.currentTarget.dataset.id;
        }else if(target.hasClass("status")){
            //上下架，并带有productId参数和enableStatus参数
            changeItemStatus(e.currentTarget.dataset.id,e.currentTarget.dataset.status);
        }else if(target.hasClass("preview")){
            //编辑，并带有productId参数
            window.location.href = "#" + e.currentTarget.dataset.id;
        }
    });
    //改变上下架状态
    function changeItemStatus(id, enableStatus) {
        var product = {};
        product.productId = id;
        product.enableStatus = enableStatus;
        $.confirm("您确定执行该操作吗？",function () {
           //上下架对应商品
           $.ajax({
               url:statusUrl,
               type:"POST",
               data:{productStr:JSON.stringify(product),statusChange:true},
               dataType:"json",
               success:function (data) {
                   if(data.success){
                       $.toast("操作成功");
                   }else{
                       $.toast("操作失败");
                   }
                   $(".product-wrap").empty();
                   getList();
               }
           })
        });
    }

})