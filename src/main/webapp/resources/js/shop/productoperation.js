$(function () {
    //从URL中获取productId的参数值
    var productId = getQueryString('productId');
    //通过productId获取商品信息的URL
    var infoUrl = '/o2o/shop/getproductbyid?productId=' + productId;
    //获取当前店铺设定的商品类别列表的URL
    var categoryUrl = '/o2o/shop/getproductcategory';
    //更新商品信息的URL
    var productPostUrl = '/o2o/shop/modifyproduct';

    //判断是编辑商品信息还是增加
    var isEdit = false;
    if (productId) {
        //如有productId则为编辑
        getInfo(productId);
        isEdit = true;
    } else {
        getCategory();
        productPostUrl = '/o2o/shop/addproduct';
    }

    //获取需要编辑的商品的商品信息，并赋值给表单
    function getInfo(id) {
        $.getJSON(infoUrl, function (data) {
            if (data.success) {
                //从返回的json中获取product对象的信息
                var product = data.product;
                $('#product-name').val(product.productName);
                $('#product-desc').val(product.productDesc);
                $('#priority').val(product.priority);
                $('#normal-price').val(product.normalPrice);
                $('#promotion-price').val(product.promotionPrice);
                //获取原本的商品类别以及该店铺的所有商品类别列表
                var optionHtml = '';
                var optionArr = data.productCategories;
                var optionSelected = product.productCategory.productCategoryId;
                optionArr.map(function (item, index) {
                    var isSelect = optionSelected === item.productCategoryId ? 'selected' : '';
                    //生成前端的HTML商品类别列表，并默认选择编辑前的商品类别
                    optionHtml += '<option data-value="'
                        + item.productCategoryId
                        + '"'
                        + isSelect
                        + '>'
                        + item.productCategoryName
                        + '</option>';
                });
                $('#category').html(optionHtml);
            }
        });
    }

    function getCategory() {
        $.getJSON(categoryUrl, function (data) {
            if (data.success) {
                var optionHtml = '';
                data.data.map(function (item, index) {
                    optionHtml += '<option data-value="'
                        + item.productCategoryId + '">'
                        + item.productCategoryName + '</option>';
                });
                $('#category').html(optionHtml);
            }
        });
    }

    /*
    针对商品详情图控件组，若该控件组的最后一个元素发生变化（即上传了图片），且总控件数达未到了6个，
    则生成一个新的文件上传控件
     */
    $('.detail-img-div').on('change', '.detail-img:last-child', function () {
        if ($('.detail-img').length < 6) {
            $('#detail-img').append('<input type="file" class="detail-img">');
        }
    });

    //提交事件按钮
    $('#submit').click(
        function () {
        	//创建商品json对象
            var product = {};
            product.productName = $('#product-name').val();
            product.productDesc = $('#product-desc').val();
            product.priority = $('#priority').val();
            product.normalPrice = $('#normal-price').val();
            product.promotionPrice = $('#promotion-price').val();
            product.productCategory = {
                productCategoryId: $('#category').find('option').not(
                    function () {
                        return !this.selected;
                    }).data('value')
            };
            product.productId = productId;

            //获取缩略图文件流
            var thumbnail = $('#small-img')[0].files[0];
            console.log(thumbnail);
            //生成表单对象，用于接收参数并传递给后台
            var formData = new FormData();
            formData.append('thumbnail', thumbnail);
            //遍历商品详情图控件，获取里面的文件流
            $('.detail-img').map(
                function (index, item) {
                	//判断该控件是否被选中
                    if ($('.detail-img')[index].files.length > 0) {
                    	//将第i个文件流赋值给key为productImg的表单键值对里
                        formData.append('productImg' + index,
                            $('.detail-img')[index].files[0]);
                    }
                });
            //将product json对象转换为字符流保存在表单对象key为productStr的键值对中
            formData.append('productStr', JSON.stringify(product));
            var verifyCodeActual = $('#j_captcha').val();
            if (!verifyCodeActual) {
                $.toast('请输入验证码');
                return;
            }
            formData.append("verifyCodeActual", verifyCodeActual);
            $.ajax({
                url: productPostUrl,
                type: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                cache: false,
                success: function (data) {
                    if (data.success) {
                        $.toast('提交成功');
                        $('#captcha_img').click();
                    } else {
                        $.toast('提交失败');
                        $('#captcha_img').click();
                    }
                }
            });
        });

});