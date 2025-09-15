<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Sign In</title>
    <link rel="stylesheet" href="${url.resourcesPath}/css/styles.css" />
    <link rel="icon" href="${url.resourcesPath}/img/favicon.ico" type="image/x-icon">
    <#-- Auto-redirect if UPDATE_PASSWORD is required -->
    <#if requiredActions?? && requiredActions?seq_contains("UPDATE_PASSWORD")>
        <meta http-equiv="refresh" content="0;url=${url.loginResetCredentialsUrl}">
    </#if>
</head>
<body>
<div class="background"></div>
<div class="login-container">
    <div class="login-card">
        <img src="${url.resourcesPath}/img/logo.png" class="logo" alt="Logo" />

        <#-- Show normal login if no required actions or not UPDATE_PASSWORD -->
        <#if !requiredActions?? || !requiredActions?seq_contains("UPDATE_PASSWORD")>
            <h2>Sign In to Your Account</h2>

            <#if message?has_content>
                <div class="message ${message.type}">
                    ${kcSanitize(message.summary)?no_esc}
                </div>
            </#if>

            <form id="kc-form-login" action="${url.loginAction}" method="post">
                <input type="text" id="username" name="username" placeholder="Email or Username" autofocus value="${(login.username!'')}" required />
                <input type="password" id="password" name="password" placeholder="Password" required />

                <#if realm.resetPasswordAllowed>
                    <div class="forgot-password">
                        <a href="${url.loginResetCredentialsUrl}">Forgot Password?</a>
                    </div>
                </#if>

                <#if realm.rememberMe>
                    <label class="remember-me">
                        <input type="checkbox" name="rememberMe" <#if login.rememberMe??>checked</#if> />
                        Remember me
                    </label>
                </#if>

                <button type="submit">Sign In</button>
            </form>

            <#if realm.registrationAllowed>
                <div class="new-user-signup">
                    New User? <a href="http://localhost:5173/register">Register</a>
                </div>
            </#if>
        <#else>
        <#-- Fallback if JavaScript is disabled (should not happen due to meta-refresh) -->
            <div class="message info">
                <p>You need to update your password. Redirecting...</p>
                <p>If you are not redirected, <a href="${url.loginResetCredentialsUrl}">click here</a>.</p>
            </div>
        </#if>
    </div>
</div>
</body>
</html>