<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>${msg("updatePasswordTitle")}</title>
    <link rel="stylesheet" href="${url.resourcesPath}/css/styles.css" />
    <link rel="icon" href="${url.resourcesPath}/img/favicon.ico" type="image/x-icon" />

    <style>
        .password-toggle {
            position: absolute;
            right: 12px;
            top: 50%;
            transform: translateY(-50%);
            background: transparent !important;
            border: none !important;
            cursor: pointer;
            padding: 0;
            width: 24px;
            height: 24px;
            margin: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            outline: none !important;
            box-shadow: none !important;
        }
        .password-toggle i {
            display: block;
            width: 20px;
            height: 20px;
            background-size: contain;
            background-repeat: no-repeat;
            background-position: center;
            transition: opacity 0.2s ease;
        }
        .password-toggle:hover i {
            opacity: 0.8;
        }
        .show-password-icon {
            background-image: url("${url.resourcesPath}/img/show.png");
        }
        .hide-password-icon {
            background-image: url("${url.resourcesPath}/img/hide.png");
        }
        .input-field {
            padding-right: 40px !important;
        }

        /* Password strength and requirements */
        .password-strength {
            margin-top: 10px;
        }
        .strength-meter {
            height: 4px;
            background: #eee;
            border-radius: 2px;
            overflow: hidden;
            margin-bottom: 8px;
        }
        .strength-bar {
            height: 100%;
            width: 0;
            background-color: #ff5252;
            transition: width 0.3s ease, background-color 0.3s ease;
        }
        .requirements-list {
            list-style: none;
            padding: 0;
            margin: 0;
            font-size: 13px;
        }
        .requirements-list li {
            display: flex;
            align-items: center;
            margin-bottom: 4px;
        }
        .requirement-icon {
            margin-right: 8px;
            font-weight: bold;
        }
        .valid {
            color: #4CAF50;
        }
        .invalid {
            color: #f44336;
        }
    </style>
</head>
<body>
<div class="background"></div>

<div class="login-container">
    <div class="login-card">
        <img src="${url.resourcesPath}/img/logo.png" class="logo" alt="Logo" />
        <h2>${msg("updatePasswordTitle")}</h2>

        <#if message?has_content>
            <div class="message ${message.type}">
                ${kcSanitize(message.summary)?no_esc}
            </div>
        </#if>

        <form id="kc-passwd-update-form" action="${url.loginAction}" method="post"
              onsubmit="login.disabled = true; return true;">

            <div style="position: relative;">
                <input type="password" id="password-new" name="password-new" class="input-field"
                       autofocus autocomplete="new-password" placeholder="Enter new password"
                       oninput="updatePasswordUI(this.value)"
                       aria-invalid="<#if messagesPerField.existsError('password','password-confirm')>true</#if>" />
                <button type="button" class="password-toggle" aria-label="${msg('hidePassword')}"
                        aria-controls="password-new" onclick="togglePasswordVisibility('password-new', this)">
                    <i class="hide-password-icon"></i>
                </button>
            </div>

            <!-- Password Strength UI -->
            <div class="password-strength">
                <div class="strength-meter"><div class="strength-bar"></div></div>
                <ul class="requirements-list">
                    <li data-requirement="length" class="invalid"><span class="requirement-icon">✗</span> 8-20 characters</li>
                    <li data-requirement="uppercase" class="invalid"><span class="requirement-icon">✗</span> 1 uppercase letter</li>
                    <li data-requirement="lowercase" class="invalid"><span class="requirement-icon">✗</span> 1 lowercase letter</li>
                    <li data-requirement="special" class="invalid"><span class="requirement-icon">✗</span> 1 special character</li>
                    <li data-requirement="notEmail" class="invalid"><span class="requirement-icon">✗</span> Not your email</li>
                </ul>
            </div>

            <#if messagesPerField.existsError('password')>
                <span class="input-error" aria-live="polite">
                    ${kcSanitize(messagesPerField.get('password'))?no_esc}
                </span>
            </#if>

            <div style="position: relative;">
                <input type="password" id="password-confirm" name="password-confirm" class="input-field"
                       autocomplete="new-password" placeholder="Confirm new password"
                       aria-invalid="<#if messagesPerField.existsError('password-confirm')>true</#if>" />
                <button type="button" class="password-toggle" aria-label="${msg('hidePassword')}"
                        aria-controls="password-confirm" onclick="togglePasswordVisibility('password-confirm', this)">
                    <i class="hide-password-icon"></i>
                </button>
            </div>
            <#if messagesPerField.existsError('password-confirm')>
                <span class="input-error" aria-live="polite">
                    ${kcSanitize(messagesPerField.get('password-confirm'))?no_esc}
                </span>
            </#if>

            <div class="remember-me" style="margin-top: 15px;">
                <label>
                    <input type="checkbox" name="logout-sessions" checked />
                    ${msg("logoutOtherSessions")}
                </label>
            </div>

            <button type="submit" class="btn-primary" style="margin-top: 25px; width: 100%;">
                ${msg("doSubmit")}
            </button>
        </form>
    </div>
</div>

[#noparse]
<script>
    function togglePasswordVisibility(fieldId, button) {
        const passwordField = document.getElementById(fieldId);
        const icon = button.querySelector('i');

        if (passwordField.type === 'password') {
            passwordField.type = 'text';
            icon.classList.remove('hide-password-icon');
            icon.classList.add('show-password-icon');
            button.setAttribute('aria-label', '${msg("showPassword")}');
        } else {
            passwordField.type = 'password';
            icon.classList.remove('show-password-icon');
            icon.classList.add('hide-password-icon');
            button.setAttribute('aria-label', '${msg("hidePassword")}');
        }
    }

    function isEmail(value) {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
    }

    function validatePassword(password) {
        return {
            length: password.length >= 8 && password.length <= 20,
            uppercase: /[A-Z]/.test(password),
            lowercase: /[a-z]/.test(password),
            special: /[^A-Za-z0-9]/.test(password),
            notEmail: !isEmail(password)
        };
    }

    function updatePasswordUI(password) {
        const requirements = validatePassword(password);
        let validCount = 0;
        const total = Object.keys(requirements).length;

        Object.keys(requirements).forEach(key => {
            const el = document.querySelector('[data-requirement="' + key + '"]');
            if (!el) return;
            const icon = el.querySelector('.requirement-icon');
            if (requirements[key]) {
                el.classList.add('valid');
                el.classList.remove('invalid');
                icon.textContent = '✓';
                validCount++;
            } else {
                el.classList.remove('valid');
                el.classList.add('invalid');
                icon.textContent = '✗';
            }
        });

        const bar = document.querySelector('.strength-bar');
        if (bar) {
            const pct = (validCount / total) * 100;
            bar.style.width = pct + '%';
            bar.style.backgroundColor = pct < 40 ? '#ff5252' : pct < 70 ? '#ffb142' : '#4CAF50';
        }
    }

    document.getElementById('password-confirm')?.addEventListener('input', function (e) {
        const password = document.getElementById('password-new').value;
        e.target.setCustomValidity(e.target.value !== password ? "Passwords don't match" : '');
    });
</script>
[/#noparse]

</body>
</html>