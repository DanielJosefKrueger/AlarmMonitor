package de.alarm_monitor.email;

import org.aeonbits.owner.Config;

public interface EMailConfiguration extends Config {

    @Key("username")
    String username();

    @Key("password")
    String password();

    @Key("SMTP_Host")
    String smtpHost();

    @Key("SMTP_Port")
    String smtpPort();

    @Key("Start_Tls")
    String startTls();

    @Key("SMTP_Auths")
    String smtpAuth();


}
