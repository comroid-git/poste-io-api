package org.comroid.poste.rest;

import org.comroid.poste.PosteIO;
import org.intellij.lang.annotations.Language;

public enum EndpointScope {
    // mailbox scopes
    MAILBOXES("/boxes"),
    MAILBOX_ID("/boxes/%s", PosteIO.EMAIL_PATTERN),
    MAILBOX_QUOTA("/boxes/%s/quota", PosteIO.EMAIL_PATTERN),
    MAILBOX_SIEVE("/boxes/%s/sieve", PosteIO.EMAIL_PATTERN),
    MAILBOX_STATS("/boxes/%s/stats", PosteIO.EMAIL_PATTERN),

    // domain scopes
    DOMAINS("/domains"),
    DOMAIN_ID("/domains/%s", PosteIO.DOMAIN_PATTERN),
    DOMAIN_DKIM("/domains/%s/dkim", PosteIO.DOMAIN_PATTERN),
    DOMAIN_STATS("/domains/%s/stats", PosteIO.DOMAIN_PATTERN),

    // email scopes
    EMAIL_STATUS("/emails/%s/status", ".+?"),

    // letsencrypt scopes
    LETSENCRYPT_ISSUE("/le/issue"),
    LETSENCRYPT_LOG("/le/log"),
    LETSENCRYPT_SETTINGS("/le/settings");

    private final String extension;
    private final String[] regExp;

    EndpointScope(String extension, @Language("RegExp") String... regExp) {
        this.extension = extension;
        this.regExp = regExp;
    }

    public String getExtension() {
        return extension;
    }

    public String[] getRegExp() {
        return regExp;
    }
}
