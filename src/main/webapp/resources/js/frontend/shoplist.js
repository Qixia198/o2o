$(function () {
    var loading = false;
    //分页允许的最大条数，如果超过就会禁止访问后台
    var maxItems = 999;
    var pageSize = 3;
    var listUrl = '/o2o/frontend/listshops';
    var searchDivUrl = '/o2o/frontend/listshopspageinfo';
    var pageNum = 1;
    //尝试获取parent	shop category id
    var parentId = getQueryString('parentId');
    var areaId = '';
    var shopCategoryId = '';
    var shopName = '';


	getSearchDivData();
	//预先加载10条店铺信息
	addItems(pageSize, pageNum);

	/**
	 * 获取店铺类别列表以及区域信息
	 */
	function getSearchDivData() {
        var url = searchDivUrl + '?' + 'parentId=' + parentId;
        $.getJSON(url, function (data) {
            if (data.success) {
                var shopCategoryList = data.shopCategoryList;
                var html = '';
                html += '<a href="#" class="button" data-category-id=""> 全部类别 </a>';
                shopCategoryList.map(function (item, index) {
                    html += '<a href="#" class="button" data-category-id='
                        + item.shopCategoryId
                        + '>'
                        + item.shopCategoryName
                        + '</a>';
                });
                $('#shoplist-search-div').html(html);
                var selectOptions = '<option value="">全部区域</option>';
                var areaList = data.areaList;
                areaList.map(function (item, index) {
                    selectOptions += '<option value="'
                        + item.areaId + '">'
                        + item.areaName + '</option>';
                });
                $('#area-search').html(selectOptions);
            }
        });
    }

	/**
	 * 获取分页展示的店铺列表信息
	 * @param pageSize
	 * @param pageIndex
	 */
    function addItems(pageSize, pageIndex) {
        //拼接查询的url地址
        var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
			+ pageSize + '&parentId=' + parentId + '&areaId=' + areaId
			+ '&shopCategoryId=' + shopCategoryId + '&shopName=' + shopName;
        //设定加载符，若还在后台获取数据，就不能再次访问后台了
		loading = true;
        $.getJSON(url,function (data) {
            if (data.success) {
                maxItems = data.count;
                var html = '';
                data.shopList.map(function (item, index) {
                    html += '' + '<div class="card" data-shop-id="'
                        + item.shopId + '">' + '<div class="card-header">'
                        + item.shopName + '</div>'
                        + '<div class="card-content">'
                        + '<div class="list-block media-list">' + '<ul>'
                        + '<li class="item-content">'
                        + '<div class="item-media">' + '<img src="'
                        + item.shopImg + '" width="44">' + '</div>'
                        + '<div class="item-inner">'
                        + '<div class="item-subtitle">' + item.shopDesc
                        + '</div>' + '</div>' + '</li>' + '</ul>'
                        + '</div>' + '</div>' + '<div class="card-footer">'
                        + '<p class="color-gray">'
                        + new Date(item.lastEditTime).Format("yyyy-MM-dd")
                        + '更新</p>' + '<span>点击查看</span>' + '</div>'
                        + '</div>';
                });
                //将组合的html控件添加到对应的控件中
                $('.list-div').append(html);
                var total = $('.list-div .card').length;
                //达到预定的记录条数后，就会停止后台的加载
                if (total >= maxItems) {
                    // 加载完毕，则注销无限加载事件，以防不必要的加载
                    //$.detachInfiniteScroll($('.infinite-scroll'));
                    // 删除加载提示符
                    $('.infinite-scroll-preloader').hide();
                    loading = true;
                }else{
                    $(".infinite-scroll-preloader").show();
                    loading = false;
                }
                //如果没有达到限制，则页面继续加1
                pageIndex += 1;
                //loading = false;
				//容器发生改变,如果是js滚动，需要刷新滚动
				$.refreshScroller();
            }
        });
    }

    //下滑屏幕自动进行分页搜索
    $(document).on('infinite', '.infinite-scroll-bottom', function () {
        if (loading){
            return;
        }
        addItems(pageSize, pageNum);
    });
	//点击某个商铺卡片时链接到该商铺中
    $('.shop-list').on('click', '.card', function (e) {
        var shopId = e.currentTarget.dataset.shopId;
        window.location.href = '/o2o/frontend/shopdetail?shopId=' + shopId;
    });
	//选择新的店铺类别后，重置页码，清空原来的店铺列表，按照新的类别查询并加载
    $('#shoplist-search-div').on('click', '.button', function (e) {
        if (parentId) {
        	// 如果传递过来的是一个父类下的子类
            shopCategoryId = e.target.dataset.categoryId;
            if ($(e.target).hasClass('button-fill')) {
                $(e.target).removeClass('button-fill');
                shopCategoryId = '';
            } else {
                $(e.target).addClass('button-fill').siblings()
                    .removeClass('button-fill');
            }
            $('.list-div').empty();
            pageNum = 1;
            addItems(pageSize, pageIndex);
        } else {
        	// 如果传递过来的父类为空，则按照父类查询
            parentId = e.target.dataset.categoryId;
            if ($(e.target).hasClass('button-fill')) {
                $(e.target).removeClass('button-fill');
                parentId = '';
            } else {
                $(e.target).addClass('button-fill').siblings()
                    .removeClass('button-fill');
            }
            $('.list-div').empty();
            //重置页码
            pageNum = 1;
            addItems(pageSize, pageNum);
            parentId = '';
        }
    });

	//查询
    $('#search').on('change', function (e) {
        shopName = e.target.value;
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
    });
	//区域信息选择
    $('#area-search').on('change', function () {
        areaId = $('#area-search').val();
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
    });
	//侧栏
    $('#me').click(function () {
        $.openPanel('#panel-left-demo');
    });

    $.init();
});
