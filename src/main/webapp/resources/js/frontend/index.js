$(function() {
    var url = "/o2o/frontend/listmainpageinfo";

    $.getJSON(url,function (data) {
       if(data.success){
           //从后台获取头条列表
           var headLineList = data.headLineList;
           var swiperHtml = "";
           //遍历头条列表，并且轮番播出
           headLineList.map(function (item, index) {
              swiperHtml += "<div class='swiper-slide img-wrap'>" +
                  "<a href='"+ item.lineLink +" external'><img class='banner-img' src='"+ item.lineImg +
                  "' alt='"+ item.lineName +"'></a>" + "</div>";
           });
           //将轮播图组赋值给前端HTML控件
           $(".swiper-wrapper").html(swiperHtml);
           //设定轮播图轮换时间为3秒
           $(".swiper-container").swiper({
              autoplay:3000,
               //用户对轮播图进行操作时，是否自动停止
               autoplayDisableOnInteraction:false
           });
           //获取后台传递过来的大类列表
           var shopCategoryList = data.shopCategoryList;
           var categoryHtml = "";
           //拼接html
           shopCategoryList.map(function (item,index) {
               categoryHtml += ''
                   +  '<div class="col-50 shop-classify" data-category='+ item.shopCategoryId +'>'
                   +      '<div class="word">'
                   +          '<p class="shop-title">'+ item.shopCategoryName +'</p>'
                   +          '<p class="shop-desc">'+ item.shopCategoryDesc +'</p>'
                   +      '</div>'
                   +      '<div class="shop-classify-img-warp">'
                   +          '<img class="shop-img" src="'+ item.shopCategoryImg +'">'
                   +      '</div>'
                   +  '</div>';
           });
           //将拼接好的类别赋值给前端
           $(".row").html(categoryHtml);
       }
    });

    //打开侧栏
    $('#me').click(function () {
        $.openPanel('#panel-left-demo');
    });

    //给类别按钮绑定事件
    $(".row").on("click",".shop-classify",function (e) {
        var shopCategoryId = e.currentTarget.dataset.category;
        var newUrl = "/o2o/frontend/shoplist?parentId="+shopCategoryId;
        window.location.href = newUrl;
    });
});
