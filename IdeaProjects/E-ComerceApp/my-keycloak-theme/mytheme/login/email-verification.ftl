<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Perform the following action(s)</title>
    <link rel="stylesheet" href="${url.resourcesPath}/css/styles.css">
</head>
<body>
<div class="login-container">
    <div class="login-card">
        <img src="${url.resourcesPath}/img/logo.png" class="logo" alt="Logo">
        <h2>Perform the following action(s)</h2>

        <div class="required-actions-container">
            <div class="message info">
                <p>Perform the following action(s):</p>
                <ul class="required-actions-list">
                    <#list requiredActions as action>
                        <li>
                            <#if action == "UPDATE_PASSWORD">
                                Update Password
                            <#elseif action == "VERIFY_EMAIL">
                                Verify Email
                            <#else>
                                ${action}
                            </#if>
                        </li>
                    </#list>
                </ul>
            </div>

            <form id="required-actions-form" action="${url.loginAction}" method="POST">
                <div class="form-actions">
                    <button type="submit" class="btn-primary">
                        » Click here to proceed
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>