<!DOCTYPE html>
<html lang="zh">
<head>
  <meta charset="UTF-8"/>
  <title>登录</title>
</head>
<body>

<div class="container">
  <form class="form-signin" method="post" action="/login">
    <h2 class="form-signin-heading">Please sign in</h2>
    <p>
      <label for="username" class="sr-only">Username</label>
      <input type="text" id="username" name="username" class="form-control" placeholder="Username" required autofocus>
    </p>
    <p>
      <label for="password" class="sr-only">Password</label>
      <input type="password" id="password" name="password" class="form-control" placeholder="Password" required>
    </p>
    <p>
      <label>
        <input type='checkbox' name='remember-me'/>
      </label> Remember me on this computer.</p>
    <input name="_csrf" type="hidden" value="325732b8-d87b-4079-bcd5-44ed78e4d644"/>
    <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
  </form>
</div>

</body>
</html>
