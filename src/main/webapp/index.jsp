<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Home Page</title>
</head>
<body>
    <h1>Welcome to the Home Page!</h1>

    <c:if test="${not empty user}">
        <h2>User Information:</h2>
        <p>ID: ${user.maNguoiDung}</p>
        <p>Name: ${user.tenNguoiDung}</p>
        <p>Date of Birth: ${user.ngaySinh}</p>
        <p>Phone: ${user.SDT}</p>
        <p>Email: ${user.email}</p>
        <p>Account Balance: ${user.soDuTaiKhoan}</p>
        <p>Role: ${user.maVaiTro}</p>
    </c:if>

    <c:if test="${empty user}">
        <p>You are not logged in.</p>
    </c:if>
</body>
</html>