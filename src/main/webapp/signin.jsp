<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Sign into your account</title>
<meta name="description" content="">
<meta name="viewport" content="width=1024">
<!--[if lt IE 9]><script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
<!-- I tried to upgrade this to bootstrap 2.0 but didnt work.
     It looks like a lot of custom css was added to the 1.4.0 boostrap css file that was checked in (css/bootstrap-1.4.0.min.css) by WrapBootstrap
     In order to do this need to take all the custom divs (topbar, page_signin, etc.) and put them in a seperate file
     Then would need to modify span to 12 and tweak the form because that is different in bootstrap 2.0  -->
<link href="css/validationEngine.jquery.css" rel="stylesheet"/>
<link href="css/bootstrap-1.4.0.min.css" rel="stylesheet">
<script src="js-external/jquery-1.6.3.min.js"></script>
<script src="js-external/jquery.validationEngine-en.2.5.2.js" type="text/javascript" charset="utf-8"></script>
<script src="js-external/jquery.validationEngine.2.5.2.js" type="text/javascript" charset="utf-8"></script>
</head>
<body>
  <div id="main">
    <div class="topbar">
      <div class="fill">
        <div class="container">
          <a class="brand" href="/" title="WrapBootstrap: Themes for Twitter Bootstrap">
          <!-- <img src="//s3.amazonaws.com/wrapbootstrap/live/imgs/logo.png" width="187" height="28" alt="WrapBootstrap" title="WrapBootstrap: Themes for Twitter Bootstrap"> -->
          </a>
        </div>
      </div>
    </div>
    <div id="page_signin">
      <div class="page-header-top">
        <div class="container">
          <h1>
            Sign in &amp; sign up <small>log into or create an account</small>
          </h1>
        </div>
      </div>
      <div class="container">
        <div class="content">
          <div class="row">
            <div class="span8">
            <c:if test="${param.error == 'true'}">
              <div class="alert-message error">
                Login failed due to: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>
               </div>
            </c:if>          
            </div>
          </div>
          <c:if test="${param.statusSuccess == 'false'}">
            <div class="row">
              <div class="span8">
              <c:if test="${not empty param.statusMsg}">
                <div class="alert-message error">
                  <c:out value="${param.statusMsg}"/>
                 </div>
              </c:if>          
              </div>
            </div>          
          </c:if>          
          <c:if test="${param.statusSuccess == 'true'}">
            <div class="row">
              <div class="span8">
              <c:if test="${not empty param.statusMsg}">
                <div class="alert-message success">
                  <c:out value="${param.statusMsg}"/>
                 </div>
              </c:if>          
              </div>
            </div>          
          </c:if>          
          <div class="row">
            <div class="span16">
              <div id="signin">
                <div class="title">Already a member? Sign in:</div>
                <form id="loginForm" name="loginForm" action="j_spring_security_check" method="post">
                  <input type="hidden" name="next" value="/">
                  <fieldset>
                    <div class="clearfix">
                      <label for="iusername"><span>Username:</span>
                      </label>
                      <div class="input">
                        <input class="validate[required,custom[onlyLetterDigitDashUnderscore],minSize[6], maxSize[20]]" tabindex="1" id="iusername" name="j_username" label="Username" value="" type="text">
                      </div>
                    </div>
                    <div class="clearfix">
                      <label for="ipassword"><span>Password:</span>
                      </label>
                      <div class="input">
                        <input tabindex="2" class="validate[required,custom[onlyLetterDigitDashUnderscore],minSize[6], maxSize[64]]" id="ipassword" name="j_password" label="Password" value="" type="password">
                      </div>
                    </div>
                    <div class="actions">
                      <input tabindex="3" class="btn primary large" type="submit" value="Sign into your account">
                    </div>
                  </fieldset>
                </form>
              </div>
              <div id="signup">
                <div class="title">Create an account. It's free!</div>
                <form id="signupForm" action="/users/create" method="post" class="form-stacked">
                  <fieldset>
                    <div class="clearfix">
                      <label for="isignup_username">Username:</label>
                      <div class="input">
                        <input id="isignup_username" class="validate[required,custom[onlyLetterDigitDashUnderscore],minSize[6], maxSize[20]]" tabindex="5" name="username" value="" type="text">
                        <span class="help-block">May contain letters, digits, dashes and underscores, and should be between 6 and 20 characters long.</span>
                      </div>
                    </div>
                    <div class="clearfix">
                      <label for="isignup_email">Email address:</label>
                      <div class="input">
                        <input id="isignup_email" class="validate[required,custom[email]]"  tabindex="6" name="email" label="Email address" value="" type="text">
                          <span class="help-block"><strong>Type carefully.</strong> You will be sent a confirmation email.</span>
                      </div>
                    </div>
                    <div class="clearfix">
                      <label for="isignup_password">Password:</label>
                      <div class="input">
                        <input id="isignup_password" class="validate[required,custom[onlyLetterDigitDashUnderscore],minSize[6], maxSize[64]]" tabindex="7" name="password" value="" type="password">
                        <span class="help-block">May contain letters, digits, dashes and underscores, and should be between 6 and 64 characters long.</span>
                      </div>
                    </div>
                    <div class="clearfix">
                      <label for="isignup_confirmPassword">Confirm Password:</label>
                      <div class="input">
                        <input id="isignup_confirmPassword" class="validate[required,custom[onlyLetterDigitDashUnderscore],minSize[6], maxSize[64], funcCall[checkPassword]]]" tabindex="7" name="confirmPassword" value="" type="password">
                        <span class="help-block">May contain letters, digits, dashes and underscores, and should be between 6 and 64 characters long.</span>
                      </div>
                    </div>
                    <div class="actions">
                      <input tabindex="9" class="btn success large" type="submit" value="Create your account">
                    </div>
                  </fieldset>
                </form>
              </div>
              <script>
                <!-- startup form validation -->
                $(document).ready(function(){
                  $("#loginForm").validationEngine();
                  $("#signupForm").validationEngine();
                });
                function checkPassword(field, rules, i, options) {
                  var password=$('#isignup_password').val();
                  var confirmPassword=$('#isignup_confirmPassword').val();
                  if(password != confirmPassword) {
                    return 'Passwords do not match';
                  }
                }
              </script>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>
