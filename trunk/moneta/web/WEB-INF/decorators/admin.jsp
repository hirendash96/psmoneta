<%@ page contentType="text/html; charset=windows-1251" language="java" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1251"/>
</head>
<body>
<table>
    <tr>        
        <td>������</td>
        <td>������ ����</td>
        <td>������� � ������</td>
        <td>������������</td>
        <td><a href="/Logout.html" >����� </a> </td>
    </tr>
    <tr>
        <td><a href="/admin/ArticleForm.html">��������</a></td>
        <td><a href="/admin/MenuItemForm.html">��������</a></td>
        <td><a href="/admin/faq/QuestionForm.html">�������� ������</a></td>
        <td><a href="/admin/UserForm.html">��������</a></td>
        <td></td>
    </tr>
    <tr>
        <td><a href="/admin/ViewArticleList.html">��������</a></td>
        <td><a href="/admin/ViewMenuItemList.html">��������</a></td>
        <td><a href="/admin/faq/ViewQuestionList.html">�������� ��������</a></td>
        <td><a href="/admin/ViewUserList.html">��������</a></td>
        <td></td>
    </tr>
</table>

<decorator:body/>
</body>

</html>
