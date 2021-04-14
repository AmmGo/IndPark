
 var fs = 0 ;
 var fq = 0 ;
 var sum = 0 ;
 function loadChart(res) {
var obj = JSON.parse(res);
   fs=obj.fs;
   fq=obj.fq;
   sum=obj.sum;
   setChart('pie', makePieChartOption());
 }
 function makePieChartOption() {
   // 指定图表的配置项和数据
         var option = {
 				title:{
 					text:sum,
 					left:"center",
 					top:"43%",
 						textStyle:{
 						color:"#000000",
 						fontSize:9,
 						align:"center"
 						}
 					},
 				color:['#F4B188','#3688FF'],
 				graphic:{
 					type:"text",
 					left:"center",
 					top:"50%",
 						style:{
 						text:"环保报警",
 						textAlign:"center",
 						fill:"#333",
 						fontSize:10,
 						}
                  },
 			 	series: [
 					{
 					name: '环保报警',
 					type: 'pie',
 					 roseType: 'angle',
 					radius: ['25%', '50%'],       //饼图大小
 					labelLine: {    //图形外文字线
 						normal: {
 							length: 25,
 							length2:70
 						}
 					},
 					label: {
 						normal: {
 							formatter:function(params){
                            str = '{a|'+ params.data.flag + '}\n'+'{b|'+params.name+'}';
                            return str
                            },
                             //图形外文字上下显示
 							borderWidth: 20,
 							borderRadius: 4,
 							padding: [0, -70],          //文字和图的边距
 							rich: {
 								a: {
 									color: '#000000',
 									fontSize: 10,
 									lineHeight: 30
 								},
 								b: {                        //name 文字样式
 									fontSize: 11,
 									lineHeight: 30,
 									color: '#000000',
 								},
 								c: {                   //value 文字样式
 									fontSize: 11,
 									lineHeight: 30,
 									color: '#000000',
 									align:"center"
 								}
 							}
 						}
 					},
 					data: [
 						{value: 40, name: '废气报警',flag:fq},
 						{value: 60, name: '废水报警',flag:fs},
 					]
             }
         ]
 				};
     return option;
 }


 function setChart(id, option) {
     // 基于准备好的dom，初始化echarts实例
     var myChart = echarts.init(document.getElementById(id));
     // 使用刚指定的配置项和数据显示图表。
     myChart.setOption(option);
 }

