$(function () {
	var listUrl = "/o2o/shop/getproductcategory";
	var addUrl = "/o2o/shop/addproductcategories";
	var deleteUrl = "/o2o/shop/removeproductcategory";

	function getlist(){
		$.getJSON(listUrl,function (data) {
			if(data.success){
				var dataList = data.data;
				var html = "";
				dataList.map(function (item, index) {
					html += "<div class='row row-product-category now'>"
					+ "<div class='col-33'>"
					+ item.productCategoryName
					+ "</div>"
					+ "<div class='col-33'>"
					+ item.priority
					+ "</div>"
					+ "<div class='col-33'><a href='#' class='button delete' data-id='"
					+ item.productCategoryId + "'>删除</a></div>" + "</div>";
				});
				$(".category-wrap").append(html);
			}else{
				$.toast("查询失败");
			}
		})
	};
	getlist();

	$("#new").click(function () {
		var tempHtml = "<div class='row row-product-category temp'>"+
			"<div class='col-33'><input class='category-input category' type='text' placeholder='请输入'></div>"+
			"<div class='col-33'><input class='category-input priority' type='number' placeholder='0'></div>"+
			"<div class='col-33'><a href='#' class='button delete'>删除</a></div>" +
			"</div>";
		$(".category-wrap").append(tempHtml);
	});

	$("#submit").click(function () {
		var tempArr = $(".temp");
		var productCategoryList = [];
		tempArr.map(function (index, item) {
			var tempObj = {};
			tempObj.productCategoryName = $(item).find(".category").val();
			tempObj.priority = $(item).find(".priority").val();
			if (tempObj.productCategoryName && tempObj.priority){
				productCategoryList.push(tempObj);
			}
		});
		$.ajax({
			url:addUrl,
			type:"POST",
			data:JSON.stringify(productCategoryList),
			contentType:"application/json",
			success:function (data) {
				if(data.success){
					$.toast("提交成功");
				}else{
					$.toast("提交失败");
				}
				$(".category-wrap").empty();
				getlist();
			}
		});
	});

	//删除前端的
	$(".category-wrap").on("click",".row-product-category.temp .delete",function (e) {
		console.log($(this).parent().parent());
		$(this).parent().parent().remove();
	});
	//删除后端的
	$(".category-wrap").on("click",".row-product-category.now .delete",function (e) {
		var target = e.currentTarget;
		$.confirm("您确认要删除吗？",function () {
			$.ajax({
				url:deleteUrl,
				type:"POST",
				data:{productCategoryId:target.dataset.id},
				dataType:"json",
				success:function (data) {
					if(data.success){
						$.toast("删除成功");
					}else{
						$.toast("删除失败");
					}
					$(".category-wrap").empty();
					getlist();
				}
			})
		})
	});

});