<%@ page contentType="text/html; charset=windows-1251" language="java" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>

<title>Dealer</title>    
    
</head>
<body>
<table width="200" border="1">
  <tr>
    <td>���������</td>
    <td>�����</td>
    <td>����� ������������</td>
    <td>�������</td>
    <td><a href="Exit.html">�����</a></td>
  </tr>
  <tr>
    <td><a href="GetSubDealer.html">��������</a></td>
    <td><a href="GetPoint.html">��������</a></td>
    <td><a href="GetDealerUser.html">��������</a></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td><a href="ViewSubDealerList.html">��������</a></td>
    <td><a href="ViewPointList.html">��������</a></td>
    <td><a href="ViewDealerUserList.html?dealerId=${myDealerId}">�������� ���� �������������</a></td>
    <td>&nbsp;</td>    
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td><a href="ViewDealerPaymentList.html">�������� ��������</a></td>
    <td><a href="ViewPointPaymentList.html">�������� ��������</a></td>
    <td></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
</table>
<decorator:body />
</body>
</html>
