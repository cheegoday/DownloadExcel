# DownloadExcel
### 思路:
1、在ajax中，将页面获取的二维json通过post传给后台controller-1，controller-1中将传过来的数据传入excel，生成excel临时文件，保存在服务器本地。给前端返回excel临时文件地址。

2、在ajax回调函数中，将excel临时文件地址和文件名传到后台controller-2，controller-2根据文件地址，找到文件，将其以流的形式写入response中。

> 代码没有以标准工程形式组织，不可运行，仅供思路参考