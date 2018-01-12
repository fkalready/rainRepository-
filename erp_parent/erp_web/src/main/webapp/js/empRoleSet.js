var data = "";
$(function () {

    $('#grid').datagrid({
        url : 'emp_list.action',
        columns : [ [
            {field:'uuid',title:'编号',width:100},
            {field:'name',title:'名称',width:100}
        ] ],
        singleSelect : true,
        onClickRow:function (rowIndex, rowData) {
            data = rowData.uuid;
            $('#tt').tree({
                url:'emp_getRole?id='+data,
                checkbox:true,
                animate:true
            });
        }
    });

    $('#btnSave').bind('click',function () {
        var treeData = $('#tt').tree('getChecked')
        var ids = [];
        $(treeData).each(function (index, obj) {
            ids.push(obj.id)
        })

        $.ajax({
            url: 'emp_updateEmpRoles.action',
            data: {id:data,ids:ids.toString()},
            type: 'post',
            dataType:'json',
            success:function (res) {
                $.messager.alert('提示', res.message, 'info');
            }
        });
    })
})