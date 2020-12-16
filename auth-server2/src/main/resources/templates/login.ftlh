<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>湖北浩方恒科科技有限公司-登录</title>
</head>
<style>
  * {
    margin: 0;
    padding: 0;
    font-size: 14px;
  }

  .loginBox {
    width: 100%;
    min-height: 100vh;
    position: relative;
  }

  .login {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background: #fff;
    width: 45%;
    height: 50%;
    font-size: 0;
  }

  .login_wrap {
    display: inline-block;
    height: 100%;
    vertical-align: top;
  }

  .login_desc {
    width: 60%;
    background: url(http://www.airseo.cn/images/BI.jpg);
    background-size: cover;
    font-size: 14px;
  }

  .login_form {
    width: 40%;
    font-size: 14px;
    box-sizing: border-box;
    box-shadow: 1px 1px 1px #CCC;
    text-align: center;
  }

  .login_pic {
    width: 60%;
    height: 15%;
    margin-top: 20%;
  }

  .form {
    width: 100%;
    margin-top: 10%;
  }

  .inputs {
    width: 80%;
    margin: 5% auto;
  }

  .inputs > span {
    display: inline-block;
    width: 30%;
    text-align: right;
    font-size: 16px;
    margin-right: 1%;
  }

  .inputs > input {
    width: 60%;
    height: 2rem;
    box-sizing: border-box;
    border: 1px solid #DCDFE6;
    border-radius: 3px;
    text-indent: 5px;
    color: #606266;
  }

  .inputs > input::-webkit-input-placeholder {
    color: #DCDFE6;
  }

  .inputs input:nth-child(2) {
    margin-bottom: 10%;
  }

  .copyright {
    position: absolute;
    bottom: 5%;
    width: 100%;
  }

  .copyright_desc {
    width: 80%;
    height: 45px;
    margin: 0 auto;
    text-align: center;
    font-size: 18px;
  }

  /*按钮样式*/
  .btn {
    font-size: 14px;
    letter-spacing: 2px;
    color: #fff;
    background: #409EFF;
    padding: 8px 20%;
    box-sizing: border-box;
    border-radius: 3px;
    cursor: pointer;
    margin-left: 15%;
  }

  .btn:hover {
    background: #66b1ff;
  }

  .btn:active {
    background: #3a8ee6;
  }
</style>
<body>
<div class="loginBox">
  <div class="login">
    <div class="login_wrap login_desc"></div>
    <div class="login_wrap login_form">
      <img src="/image/logo.png" alt="logo" class="login_pic" title="logo">
      <form name="login" action="/login" method="post" class="form">
        <div class="inputs">
          <span>账 号:</span>
          <input name="username" type="text" placeholder="用户名/邮箱/手机号" maxlength="18">
        </div>
        <div class="inputs">
          <span>密 码:</span>
          <input name="password" type="password" placeholder="请输入登录密码:" maxlength="16">
        </div>
        <span class="btn" onclick="document.forms[0].submit()">登 录</span>
      </form>
    </div>
  </div>
  <div class="copyright">
    <div class="copyright_desc">© CopyRight by HaoFangHengKeng in 2020. Infringement shall investigate</div>
  </div>
</div>
</body>
</html>
