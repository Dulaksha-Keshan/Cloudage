package com.keshan.cloudage.org.mail;

import org.springframework.stereotype.Component;

@Component
public class ReactivationMailBody {
    public String buildReactivationEmail(String link) {
        return """
                <html>
                  <body style="font-family: Arial, sans-serif; line-height: 1.6;">
                    <h2>Hey,</h2>
                    <p>This is your account reactivation link. Click the button below to reactivate your account:</p>
                    
                    <p style="text-align: center; margin: 20px 0;">
                      <a href="%s" 
                         style="background-color: #4CAF50; 
                                color: white; 
                                padding: 12px 20px; 
                                text-decoration: none; 
                                border-radius: 5px;">
                        Reactivate Account
                      </a>
                    </p>
                    
                    <p>If you didn’t request this, you can safely ignore this email.</p>
                    <br>
                    <p>Thanks,<br>Cloudage Team</p>
                  </body>
                </html>
                """.formatted(link);
    }
}
