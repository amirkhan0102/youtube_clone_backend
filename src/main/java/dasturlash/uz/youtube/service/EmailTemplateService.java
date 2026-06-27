package dasturlash.uz.youtube.service;

import org.springframework.stereotype.Service;

@Service
public class EmailTemplateService {

    public String buildRegistrationTemplate(String name, String verificationLink) {

        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Email Verification</title>
                </head>
                <body style="margin:0;padding:0;background:#f5f5f5;font-family:Arial,sans-serif;">

                <table width="100%%" cellpadding="0" cellspacing="0">
                    <tr>
                        <td align="center">

                            <table width="600"
                                   cellpadding="0"
                                   cellspacing="0"
                                   style="background:#ffffff;
                                          margin-top:40px;
                                          border-radius:10px;
                                          padding:40px;">

                                <tr>
                                    <td align="center">

                                        <h1 style="color:#ff0000;">
                                            YouTube Clone
                                        </h1>

                                        <h2>
                                            Hello, %s 👋
                                        </h2>

                                        <p style="font-size:16px;color:#555;">
                                            Thank you for registering.
                                        </p>

                                        <p style="font-size:16px;color:#555;">
                                            Please verify your email address by clicking the button below.
                                        </p>

                                        <br>

                                        <a href="%s"
                                           style="
                                                background:#ff0000;
                                                color:white;
                                                padding:15px 30px;
                                                text-decoration:none;
                                                border-radius:6px;
                                                display:inline-block;
                                                font-weight:bold;">
                                            Verify Email
                                        </a>

                                        <br><br>

                                        <p style="color:#999;">
                                            If you didn't create this account,
                                            simply ignore this email.
                                        </p>

                                    </td>
                                </tr>

                            </table>

                        </td>
                    </tr>
                </table>

                </body>
                </html>
                """.formatted(name, verificationLink);
    }

}