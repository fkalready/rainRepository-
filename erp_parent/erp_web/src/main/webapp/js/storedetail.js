$(function () {
    $('#grid').datagrid({
        url:'storedetail_listByPage',
        columns:[[
            {field:'uuid',title:'编号',width:100},
            {field:'storeName',title:'仓库编号',width:100},
            {field:'goodsName',title:'商品编号',width:100},
            {field:'num',title:'数量',width:100}
        ]],
        singleSelect: true,
        pagination: true,
        onDblClickRow:function (rowIndex, rowData) {
            $('#storeoperDlg').dialog('open');

            $('#storeName').html(rowData.storeName)
            $('#goodsName').html(rowData.goodsName)

            $('#itemgrid').datagrid({
                url:'storeoper_listByPage.action?t1.storeuuid='+rowData.storeuuid+'&t1.goodsuuid='+rowData.goodsuuid,
                columns:[[
                    {field:'uuid',title:'编号',width:100},
                    {field:'empName',title:'操作员工编号',width:100},
                    {field:'opertime',title:'操作日期',width:100,formatter: function(value,row,index){
                            return new Date(value).Format('yyyy-MM-dd')
                        }},
                    {field:'storeName',title:'仓库编号',width:100},
                    {field:'goodsName',title:'商品编号',width:100},
                    {field:'num',title:'数量',width:100},
                    {field:'type',title:'类型',width:100,formatter: function(value,row,index){
                            if (value == '1'){
                                return '入库';
                            } if(value == '2') {
                                return '出库';
                            }
                        }
                    }
                ]],
                pagination: true
            })
        }
    })

    $('#storeoperDlg').dialog({
        title: '仓库变动记录',
        width: 740,
        height: 400,
        closed: true,
        modal: true,
    });


})
