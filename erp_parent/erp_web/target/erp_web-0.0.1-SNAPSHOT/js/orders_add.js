
var index = -1;

$(function () {
    $('#ordersgrid').datagrid({
        singleSelect:true,
        showFooter:true,
        columns: [[
            {field: 'goodsuuid', title: '商品编号', width: 100,editor:{
                type:'numberbox',
                options:{
                    disabled:'true'
                }
                }},
            {field: 'goodsname', title: '商品名称', width: 100,editor:{
                type:'combobox',
                options:{
                    url:'goods_list.action',
                    valueField:'name',
                    textField:'name',
                onSelect:function (record) {

                    var gu = $('#ordersgrid').datagrid('getEditor',{
                        index:index,
                        field:'goodsuuid'
                    });
                    var pr = $('#ordersgrid').datagrid('getEditor',{
                        index:index,
                        field:'price'
                    })

                    $(gu.target).val(record.uuid);

                    if(type*1==1) {
                        $(pr.target).val(record.inprice);
                    }
                    if(type*1==2) {
                        $(pr.target).val(record.outprice);
                    }
                    count();
                    sum();
                }
                }}},
            {field: 'price', title: '单价', width: 100,editor:{
                type:'numberbox',
                options:{
                    disabled:'true',
                    precision:2
                }
                }},
            {field: 'num', title: '数量', width: 100,editor:'numberbox'},
            {field: 'money', title: '金额', width: 100,editor:{
                    type:'numberbox',
                    options:{
                        disabled:'true',
                        precision:2
                    }
                }},
            {field: '-', title: '操作', width: 100,formatter: function(value,row,ind){
                if(row.num != '合计'){
                    value="<a href='javascript:void(0)' onclick=del("+ind+")>删除</a>"
                    return value;
                }
                }
            }
        ]],
        toolbar:[{
            iconCls: 'icon-add',
            text:'新增',
            handler: function(){

                $('#ordersgrid').datagrid('appendRow',{
                    price: 0,
                    num: 0,
                });

                if (index > -1){//此时index记录的是我上次的行的索引 不存在时其值为-1
                    $('#ordersgrid').datagrid('endEdit',index)
                }
                //每次点击新增时 会在表格最后一行添加 获取当前表格的最后一行并将此值赋值给index用于记录该行 然后使其进入编辑状态
                index = $('#ordersgrid').datagrid('getRows').length - 1;
                $('#ordersgrid').datagrid('beginEdit',index);
                keyUpEvent();
            }
        },'-',{
            iconCls: 'icon-save',
            text:'保存',
            handler: function(){
                if (index > -1){
                    $('#ordersgrid').datagrid('endEdit',index);
                }
                var submitData = $('#ccinput').serializeJSON();
                if (submitData['t.supplieruuid'] == ''){
                    $.messager.alert("提示!","请选择供应商!","info")
                    return;
                }

                var rows = $('#ordersgrid').datagrid('getRows');
                submitData['jsonString'] = JSON.stringify(rows);

                submitData['t.type'] = type;

                $.post('orders_add.action',submitData,function (result) {
                    $.messager.alert('提示',result.message,'info')
                    if(result.success){
                        $('#cc').form('clear');
                        $('#ordersgrid').datagrid('loadData',{
                            total:0,
                            rows:[],
                            footer:[
                            {num: '合计', money: 0}
                        ]})

                        $('#addOrdersDlg').window('close');
                        $('#grid').datagrid('reload');

                    }
                },"json")
            }
        }],
        onClickRow:function (rowIndex, rowData) {
            //rowIndex 当前点击的行的索引
            //rowData 当前点击行的数据
            //当每的点击其他的行时 首先关闭之前的所选中的行 index 记录的是上一次所点击的行
            $('#ordersgrid').datagrid('endEdit',index)
            index = rowIndex;//记录下当前所点击行的值 并且开启编辑状态
            $('#ordersgrid').datagrid('beginEdit',rowIndex)
            keyUpEvent();
        }
    });

    $('#ordersgrid').datagrid('reloadFooter',[
        {num: '合计', money: 0}
    ]);


    $('#cc').combogrid({
        panelWidth:625,
        idField:'uuid',
        textField:'name',
        url: 'supplier_list?t1.type='+type,
        columns: [[
            {field:'uuid',title:'编号',width:100},
            {field:'name',title:'名称',width:100},
            {field:'address',title:'联系地址',width:100},
            {field:'contact',title:'联系人',width:100},
            {field:'tele',title:'联系电话',width:100},
            {field:'email',title:'邮件地址',width:100}
        ]],
        mode:'remote'
    });
});

function count() {

    var numEditor = getEditor('num');
    var num = $(numEditor.target).val() * 1;

    var priceEditor = getEditor('price');
    var price = $(priceEditor.target).val() * 1;

    var money = (num * price).toFixed(2);

    var moneyEditor = getEditor('money');
   $(moneyEditor.target).val(money);

    var rows = $('#ordersgrid').datagrid('getRows')
    rows[index].money = money;
}

function getEditor(_field) {
    return $('#ordersgrid').datagrid('getEditor',{
        index:index,
        field:_field
    })
}

function keyUpEvent() {
    var numEditor = getEditor('num');
    $(numEditor.target).bind('keyup',function () {
        count();
        sum();
    });
}

function del(ind) {
    $('#ordersgrid').datagrid('endEdit',index);
    $('#ordersgrid').datagrid('deleteRow',ind);
    var data = $('#ordersgrid').datagrid('getData');
    $('#ordersgrid').datagrid('loadData',data);
    sum();
}

function sum() {
    var rows = $('#ordersgrid').datagrid('getRows');
    var total = 0;
    $(rows).each(function (index,row) {
        total += row.money * 1;
    })
    $('#ordersgrid').datagrid('reloadFooter',[
        {num: '合计', money: total.toFixed(2)}
    ]);
}