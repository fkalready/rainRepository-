var name="orders";

var url = "";

var rowId = "";

var type = "";

$(function(){

    var ordersDlgToolbar = [];//定义一个toolbar

    //对于不同的请求对toobar进行设置
    url ='orders_listByPage.action';
    type = Request['type'];

    if(Request['oper']=='myorders'){
        url='orders_myListByPage.action?t1.type=' + type;
    }
    if(Request['oper']=='orders'){
        if (type *1 == 1){
            document.title='采购订单查询'
        }
        if (type *1 == 2){
            document.title='销售订单查询'
        }
        url += '?t1.type=' + type;
    }
    if (Request['oper']=='doCheck'){
        url += '?t1.type=1&t1.state=0';
        document.title='订单审核'

        ordersDlgToolbar.push({
            text:'审核',
            iconCls:'icon-search',
            handler:doCheck
        })
    }
    if (Request['oper']=='doStart'){
        url += '?t1.type=1&t1.state=1';
        document.title='订单确认'

        ordersDlgToolbar.push({
            text:'确认',
            iconCls:'icon-search',
            handler:doStart
        })
    }

    ordersDlgToolbar.push({
        text:'导出',
        iconCls:'icon-excel',
        handler:function () {
            $.download(name+"_exportById",{id:$('#uuid').html()})
        }
    })

    if(type == 2){
        ordersDlgToolbar.push({
            text:'物流详情',
            iconCls:'icon-search',
            handler:function () {
                var waybillsn = $('#waybillsn').html();
                if (!waybillsn){
                    $.messager.alert('提示', '没有物流信息!', 'info');
                    return;
                }
                $('#waybillDlg').dialog({
                    title: '物流详情',//窗口标题
                    width: 500,//窗口宽度
                    height: 300,//窗口高度
                    closed: false,//窗口是是否为关闭状态, true：表示关闭
                    modal: true,//模式窗口
                    buttons:[{
                        text:'确定',
                        handler:function () {
                            $('#waybillDlg').dialog('close');
                        }
                    }]
                });
                $('#waybillGrid').datagrid({
                    url:'orders_waybilldetailList?sn='+waybillsn,
                    columns:[[
                        {field:'exedate',title:'执行日期',width:100},
                        {field:'exetime',title:'执行时间',width:100},
                        {field:'info',title:'执行信息',width:100}
                    ]],
                    singleSelect: true
                })
            }
        })
    }


    var inOutTitle = "", inOutButton = "";
    if (Request['oper']=='doInStore'){
        url += '?t1.type=1&t1.state=2';
        inOutTitle = '采购入库';
        inOutButton = '入库';
        document.title='采购入库'
    }
    if (Request['oper']=='doOutStore'){
        url += '?t1.type=2&t1.state=0';
        inOutTitle = '销售出库';
        inOutButton = '出库'
        document.title='销售出库'
    }


    //加载表格数据
    $('#grid').datagrid({
        url:url,
        columns:getColumns(),
        singleSelect: true,
        pagination: true,
        onDblClickRow:function (rowIndex, rowData) {
            $('#ordersDlg').dialog('open');

            $('#uuid').html(rowData.uuid)
            rowId = rowData.uuid;

            $('#supplierName').html(rowData.supplierName)
            $('#state').html(ordersFormatter(rowData.state))

            $('#createrName').html(rowData.createrName)
            $('#checkerName').html(rowData.checkerName)
            $('#starterName').html(rowData.starterName)
            $('#enderName').html(rowData.enderName)
            $('#createtime').html(dataFormatter(rowData.createtime))
            $('#checktime').html(dataFormatter(rowData.checktime))
            $('#starttime').html(dataFormatter(rowData.starttime))
            $('#endtime').html(dataFormatter(rowData.endtime))
            $('#waybillsn').html(rowData.waybillsn)

            $('#itemgrid').datagrid('loadData',rowData.orderdetails)
        },
    });

    // || Request['oper'] == orders
    if (Request['oper'] == 'myorders'){
        var addText = "";
        if (type == '1'){
            addText = "订单申请";
            $('#ordersupplier').html('供应商');
        }
        if (type == '2'){
            addText = "销售录入";
            $('#ordersupplier').html('客户');
        }

        $('#grid').datagrid({
            toolbar:[{
            text:addText,
            iconCls: 'icon-add',
            handler: function(){
                $('#addOrdersDlg').dialog('open')
            }
            }]
        });
    }

    //明细窗口的配置
    var ordersDlgCfg={
        title: '订单详情',//窗口标题
        width: 800,//窗口宽度
        height: 400,//窗口高度
        closed: true,//窗口是是否为关闭状态, true：表示关闭
        modal: true,//模式窗口
    }

    //向明细窗口中添加toolbar
    ordersDlgCfg['toolbar'] = ordersDlgToolbar;

    $('#ordersDlg').dialog(ordersDlgCfg);

    $('#itemDlg').dialog({
        title: inOutTitle,//窗口标题
        width: 300,//窗口宽度
        height: 200,//窗口高度
        closed: true,//窗口是是否为关闭状态, true：表示关闭
        modal: true,//模式窗口
        buttons:[{
            text:inOutButton,
            iconCls:'icon-save',
            handler:doInOutStore
        }]
    });

    $('#addOrdersDlg').dialog({
        title: '增加订单',//窗口标题
        width: 700,//窗口宽度
        height: 400,//窗口高度
        closed: true,//窗口是是否为关闭状态, true：表示关闭
        modal: true//模式窗口
    });

    var itemgridCfg = {
        columns:[[
            {field:'uuid',title:'编号',width:100},
            {field:'goodsuuid',title:'商品编号',width:100},
            {field:'goodsname',title:'商品名称',width:150},
            {field:'price',title:'价格',width:100},
            {field:'num',title:'数量',width:100},
            {field:'money',title:'金额',width:100},
            {field:'state',title:'状态',width:100,formatter:detailFormatter}
        ]],
        singleSelect: true
    }

    if (Request['oper']=='doInStore' || Request['oper']=='doOutStore' ){
        itemgridCfg['onDblClickRow'] = function (rowIndex, rowData) {
            $('#itemDlg').window('open');

            $('#id').val(rowData.uuid)
            $('#goodsuuid').html(rowData.goodsuuid)
            $('#goodsname').html(rowData.goodsname)
            $('#num').html(rowData.num)
        }
    }

    $('#itemgrid').datagrid(itemgridCfg)
});

/**
 * 定义初始化列
 */
function getColumns(){
    if (type * 1 == 1){
        return [[
            {field:'uuid',title:'编号',align:'center',width:100},
            {field:'createtime',title:'生成日期',width:100,align:'center',formatter: dataFormatter},
            {field:'checktime',title:'审核日期',width:100,align:'center',formatter: dataFormatter},
            {field:'starttime',title:'确认日期',width:100,align:'center',formatter: dataFormatter},
            {field:'endtime',title:'结束日期',width:100,align:'center',formatter: dataFormatter},
            {field:'createrName',title:'创建',align:'center',width:100},
            {field:'checkerName',title:'审核',align:'center',width:100},
            {field:'starterName',title:'确认',align:'center',width:100},
            {field:'enderName',title:'入库',align:'center',width:100},
            {field:'supplierName',title:'供应商',align:'center',width:100},
            {field:'state',title:'订单状态',width:100,align:'center',formatter:ordersFormatter}
        ]];
    }
    if (type * 1 == 2){
        return [[
            {field:'uuid',title:'编号',align:'center',width:100},
            {field:'createtime',title:'生成日期',width:200,align:'center',formatter: dataFormatter},
            {field:'endtime',title:'出库日期',width:200,align:'center',formatter: dataFormatter},
            {field:'createrName',title:'创建',align:'center',width:100},
            {field:'enderName',title:'出库',align:'center',width:100},
            {field:'supplierName',title:'客户',align:'center',width:200},
            {field:'state',title:'订单状态',align:'center',width:100,formatter:ordersFormatter},
            {field:'waybillsn',align:'center',title:'运单号',width:100}
        ]];
    }

}


//日期的格式化器
function dataFormatter(value) {
    return new Date(value).Format("yyyy-MM-dd");
}


//订单状态的格式化器
function ordersFormatter (value) {
    if(type * 1 == 1){
        switch(value*1){
            case 0:
                return '未审核';
            case 1:
                return '已审核';
            case 2:
                return '已确定';
            case 3:
                return '已入库';
            default:return "";
        }
    }
    if(type * 1 == 2){
        switch(value*1){
            case 0:
                return '未出库';
            case 1:
                return '已出库';
            default:return "";
        }
    }


}

//订单详细信息的格式化器
function detailFormatter (value) {
    if(type * 1 == 1){
        switch(value*1){
            case 0:
                return '未入库';
            case 1:
                return '已入库';
            default:return "";
        }
    }
    if(type * 1 == 2){
        switch(value*1){
            case 0:
                return '未出库';
            case 1:
                return '已出库';
            default:return "";
        }
    }
}

//请求服务器方法
function doCheck(){
    $.messager.confirm("提示!","是否确认审核?",function (r) {
        if(r){
            $.post('orders_doCheck', {id:rowId}, function (result) {
                $.messager.alert("提示",result.message,'info')
                if(result.success){
                    $('#ordersDlg').dialog('close');
                    $('#grid').datagrid('reload');
                }
            }, 'json');
        }
    })
}
function doStart(){
    $.messager.confirm("提示!","是否确认该订单?",function (r) {
        if(r){
            $.post('orders_doStart', {id:rowId}, function (result) {
                $.messager.alert("提示",result.message,'info')
                if(result.success){
                    $('#ordersDlg').dialog('close');
                    $('#grid').datagrid('reload');
                }
            }, 'json');
        }
    })
}
function doInOutStore(){
    var formData = $('#itemForm').serializeJSON();

    if (formData['storeuuid'] == ''){
        $.messager.alert("提示",'请选择仓库!','info')
        return;
    }

    var ajaxUrl = "";
    var msg = "";

    if(type * 1 == 1){
        ajaxUrl = 'orders_doInStore';
        msg = "是否确认入库?";
    }
    if(type * 1 == 2){
        ajaxUrl = 'orders_doOutStore';
        msg = "是否确认出库?";
    }

    $.messager.confirm("提示!",msg,function (r) {
        if(r){
            $.post(ajaxUrl,formData, function (result) {
                $.messager.alert("提示",result.message,'info')
                if(result.success){
                    $('#itemDlg').dialog('close');

                    var selectRow = $('#itemgrid').datagrid('getSelected');
                    //手动设置所选中的行的状态为'1'
                    selectRow.state = '1';

                    //获取所有的订单明细商品行 然后手动加载数据 让我们手动设置的状态加载到明细表当中
                    var rowData = $('#itemgrid').datagrid('getData')
                    $('#itemgrid').datagrid('loadData', rowData);

                    var flag = true;
                    //判断是否所有的行的状态都不为'0' 当有一个为'0' 时 将我们的flag变为false
                    $(rowData.rows).each(function (index, row) {
                        if (row.state == '0') {
                            flag = false;
                            return false;
                        }
                    })

                    if (flag) {
                        $('#ordersDlg').window('close');
                        $('#grid').datagrid('reload');
                    }
                }
            }, 'json');
        }
    })
}
