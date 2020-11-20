<!DOCTYPE html>
<html lang="zh">
<head>
  <meta charset="UTF-8"/>
  <title>Insert title here</title>
</head>
<body>

<#if principal??>
  <div>
    <p> 用户信息: ${principal}</p>
    <p> 名言信息: ${proverb}</p>
    <p> 其他信息: ${name}</p>
  </div>
<#else>
  <script>
    window.location.href = "/login";
  </script>
</#if>
<script type="text/javascript">

</script>
</body>
</html>
