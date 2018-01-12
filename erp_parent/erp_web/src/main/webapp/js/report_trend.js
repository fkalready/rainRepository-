$(function () {

    var year = new Date().getFullYear();

    $('#year').combobox('setValue',year);

    $('#grid').datagrid({
        url:'report_trendReport',
        columns:[[
            {field:'name',title:'月份',width:100},
            {field:'y',title:'销售额',width:100},
        ]],
        singleSelect: true,
        queryParams:{'year':year},
        pagination: true,
        onLoadSuccess:function (data) {
            showChar(data.rows)
        }
    });

    //点击查询按钮
    $('#btnSearch').bind('click',function(){
        //把表单数据转换成json对象
        var formData = $('#searchForm').serializeJSON();
        $('#grid').datagrid('load',formData);
    });


})

function showChar(charData){
    var month = [];
    for (var i = 1;i <=12;i++){
        month.push(i+'月')
    };
    Highcharts.chart('chart', {
        chart: {
            type: 'line'
        },
        title: {
            text: $('#year').combobox('getValue')+'销售趋势'
        },
        subtitle: {
            text: 'Source: itcast.cn'
        },
        xAxis: {
            categories: month
        },
        yAxis: {
            title: {
                text: 'RMB (￥)'
            }
        },
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true
                },
                enableMouseTracking: false
            }
        },
        series: [{
            name: '销售趋势',
            data: charData
        }]
    });
}
