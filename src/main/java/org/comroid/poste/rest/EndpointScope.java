package org.comroid.poste.rest;

import org.intellij.lang.annotations.Language;

public enum EndpointScope {
    // mailbox scopes
    MAILBOXES("boxes"),
    MAILBOX_ID("boxes/%s", Constants.EMAIL),
    MAILBOX_QUOTA("boxes/%s/quota", Constants.EMAIL),
    MAILBOX_SIEVE("boxes/%s/sieve", Constants.EMAIL),
    MAILBOX_STATS("boxes/%s/stats", Constants.EMAIL),

    // letsencrypt scopes
    LETSENCRYPT_ISSUE("le/issue"),
    LETSENCRYPT_LOG("le/log"),
    LETSENCRYPT_SETTINGS("le/settings");

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

    private static class Constants {
        private static final String EMAIL = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    }
}
