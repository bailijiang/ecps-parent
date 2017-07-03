<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ include file="/ecps/console/common/taglibs.jsp"%>

<head>
<title>写卡单_<fmt:message key="OrderMgmtMenu.title"/></title>
<meta name="heading" content="<fmt:message key='mainMenu.heading'/>"/>
<meta name="menu" content="OrderMgmtMenu"/>
<script type="text/javascript" src="<c:url value='/ecps/console/res/js/jquery.form.js'/>"></script>
<script type="text/javascript" src="<c:url value='/ecps/console/res/js/order.js'/>"></script>
<script type="text/javascript" src="<c:url value='/ecps/console/res/plugins/My97DatePicker/WdatePicker.js'/>"></script>
<script language="javascript" type="text/javascript">
$(function(){	
	var orderState=$("#orderState").val();
	switch(orderState){
	 case "":$("#label1").attr("class","here");break;
	 case "4":$("#label2").attr("class","here");break;
	 case "5":$("#label3").attr("class","here");break;
	 case "6,7,9,10,11,12,13,14,15,16,18,20,21,22,24,25,27,28,29,30,31,32,33,34,19":$("#label4").attr("class","here");break;
	 case "23,8,26":$("#label5").attr("class","here");break;
	}
	
});
function exportOrder(s){
    var form = document.getElementById("form1");
    form.action="${path}/order/orderExport.do?s="+s+"&type=2";
    subVerify4Title($("#orderNo"));
    subVerify4Title($("#phone"));
    subVerify4Title($("#shipName"));
    subVerify4Title($("#userName"));
    form.submit();
    form.action="${path}/order/orderCard.do";
}

</script>
<jsp:include page="/ecps/console/common/jscript.jsp"/>
</head>
<body id="main">

<div class="frameL"><div class="menu icon">
    <jsp:include page="/${system}/common/ordermenu.jsp"/>
</div></div>

<div class="frameR"><div class="content">
    <div class="loc icon"><samp class="t12"></samp><fmt:message key='menu.current.loc'/>：<fmt:message key="OrderMgmtMenu.title"/>&nbsp;&raquo;&nbsp;<span class="gray" title="写卡单">写卡单</span></div>
	 <h2 class="h2_ch">
      <span id="tabs" class="l">
        <a id="label1" href="${path}/order/orderCard.do"   title="全部" class="nor">全部</a>
        <a id="label2" href="${path}/order/orderCard.do?orderState=4"   title="待写卡" class="nor">待写卡</a>  
        <a id="label3" href="${path}/order/orderCard.do?orderState=5"   title="写卡待确认" class="nor">写卡待确认</a>
        <a id="label4" href="${path}/order/orderCard.do?orderState=6,7,9,10,11,12,13,14,15,16,18,20,21,22,24,25,27,28,29,30,31,32,33,34,19"   title="写卡成功" class="nor">写卡成功</a>  
        <a id="label5" href="${path}/order/orderCard.do?orderState=23,8,26"   title="写卡异常" class="nor">写卡异常</a>
      </span>
      <span class="r inb_a">
            <input id="button1" type="button" class="hand btn120x20" onclick="exportOrder(1)" value="导出当前页订单"/>&nbsp;&nbsp;<input id="button2" type="button" class="hand btn120x20" onclick="exportOrder(2)" value="导出全部订单"/>
        </span>

     </h2>
	
	<form action="${path}/order/orderPay.do" id="form1" name="form1" method="post">
     

   <table cellspacing="0" summary="" class="tab" id="myTable">
    <tr>
    <th>订单号</th>
    <th>收货人</th>
    <th>联系电话</th>
    <th>支付金额</th>
    <th>支付方式</th>
    <th>支付状态</th>
    <th>下单时间</th>
    <th>操作时间</th>
    <th>订单状态</th>
    <th>操作</th>
    </tr>
    <c:forEach items="${tbList }" var="tb">
	    <tr>
	    <td><a href="${path }/shop/order/orderPay/detail.jsp?orderId=3123"></a>${tb.order.orderNum }</td>
	    <td>${tb.order.shipName }</td>
	    <td>${tb.order.phone }</td>
	    <td>${tb.order.orderSum }</td>
	    <td>在线支付</td>
	    <td>
	    	<c:if test="${tb.order.isCall == 1 }">已支付</c:if>
	    	<c:if test="${tb.order.isCall == 0 }">未支付</c:if>
	    </td>
	    <td class="nwp">
	    	<fmt:formatDate value="${tb.order.orderTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
	    </td>
	    <td class="nwp"><fmt:formatDate value="${tb.order.orderTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	    <td class="nwp">
	    	未付款单
	    </td>
	    <td><a href="${path }/order/selectOrderAndDetail.do?orderId=${tb.order.orderId}">查看</a></td>
	    </tr>
    
    </c:forEach>

    </table>
  	<input type="hidden"  id="orderState" value="${orderState }">
    </form>


</div></div>
</body>

