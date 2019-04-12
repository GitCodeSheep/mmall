<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<h2>Hello World</h2>

SpringMVC上传文件
<form name="form1" action="/manager/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="SpringMVC上传文件" />
</form>

富文本图片上传文件
<form name="form1" action="/manager/product/richtext_img_uploaddo" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="SpringMVC上传文件" />
</form>
</body>
</html>
