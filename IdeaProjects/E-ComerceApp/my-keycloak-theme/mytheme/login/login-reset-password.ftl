<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Reset Password | Keycloak</title>
    <link rel="stylesheet" href="${url.resourcesPath}/css/styles.css" />
    <link rel="icon" href="${url.resourcesPath}/img/favicon.ico" type="image/x-icon">
</head>
<body>
<div class="background"></div>
<div class="login-container">
    <div class="login-card">
        <img src="${url.resourcesPath}/img/logo.png" class="logo" alt="Logo" />
        <h2>Reset Your Password</h2>

        <#if message?has_content>
            <div class="message ${message.type}">
                ${kcSanitize(message.summary)?no_esc}
            </div>
        </#if>

        <form id="kc-reset-password-form" action="${url.loginAction}" method="post">
            <#if auth?has_content && auth.showUsername()>
                <input type="text" id="username" name="username" placeholder="Email or Username" autofocus value="${auth.attemptedUsername}" />
            <#else>
                <input type="text" id="username" name="username" placeholder="Email or Username" autofocus />
            </#if>

            <div class="form-group">
                <div class="${properties.kcLabelWrapperClass!}">
                    <label for="username" class="${properties.kcLabelClass!}"><#if !realm.loginWithEmailAllowed>Username<#elseif !realm.registrationEmailAsUsername>Username or Email<#else></#if></label>
                </div>
            </div>

            <button type="submit" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}">
                ${msg("doSubmit")}
            </button>
        </form>

        <div class="back-to-login">
            <a href="${url.loginUrl}">Back to Login</a>
        </div>
    </div>
</div>
</body>
</html>