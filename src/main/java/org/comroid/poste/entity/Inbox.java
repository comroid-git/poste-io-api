package org.comroid.poste.entity;

import org.comroid.api.ContextualProvider;
import org.comroid.common.io.FileHandle;
import org.comroid.mail.EMailAddress;
import org.comroid.poste.PosteIO;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.comroid.varbind.container.DataContainerBase;

import java.time.Instant;
import java.util.ArrayList;
import java.util.function.Supplier;

public final class Inbox extends DataContainerBase<Inbox> {
    public static final GroupBind<Inbox> Type
            = new GroupBind<>(PosteIO.CONTEXT, "inbox");
    public static final VarBind<Inbox, String, EMailAddress, EMailAddress> ADDRESS
            = Type.createBind("address")
            .extractAs(StandardValueType.STRING)
            .andRemap(EMailAddress::parse)
            .build();
    public static final VarBind<Inbox, String, String, String> USER
            = Type.createBind("user")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Inbox, String, String, String> DOMAIN
            = Type.createBind("domain")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Inbox, String, String, String> PASSWORD
            = Type.createBind("password")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Inbox, String, String, String> PASSWORD_PLAINTEXT
            = Type.createBind("passwordPlaintext")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Inbox, String, FileHandle, FileHandle> HOME
            = Type.createBind("home")
            .extractAs(StandardValueType.STRING)
            .andRemap(FileHandle::new)
            .build();
    public static final VarBind<Inbox, String, Instant, Instant> CREATED
            = Type.createBind("created")
            .extractAs(StandardValueType.STRING)
            .andRemap(Instant::parse)
            .build();
    public static final VarBind<Inbox, String, Instant, Instant> UPDATED
            = Type.createBind("updated")
            .extractAs(StandardValueType.STRING)
            .andRemap(Instant::parse)
            .build();
    public static final VarBind<Inbox, String, String, String> NAME
            = Type.createBind("name")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Inbox, Boolean, Boolean, Boolean> IS_DISABLED
            = Type.createBind("disabled")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<Inbox, Boolean, Boolean, Boolean> IS_DOMAIN_ADMIN
            = Type.createBind("domain_admin")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<Inbox, Boolean, Boolean, Boolean> IS_SUPER_ADMIN
            = Type.createBind("super_admin")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<Inbox, Boolean, Boolean, Boolean> IS_STRICT_FROM_HEADER_DISABLED
            = Type.createBind("strict_from_disabled")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<Inbox, String, String, String> REFERENCE_ID
            = Type.createBind("reference_id")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Inbox, Boolean, Boolean, Boolean> IS_REDIRECT_ONLY
            = Type.createBind("redirect_only")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<Inbox, String, EMailAddress, ArrayList<EMailAddress>> REDIRECT_TO
            = Type.createBind("redirect_to")
            .extractAsArray(StandardValueType.STRING)
            .andRemap(EMailAddress::new)
            .intoCollection((Supplier<ArrayList<EMailAddress>>) ArrayList::new)
            .build();
    public static final VarBind<Inbox, Boolean, Boolean, Boolean> IS_DISCARD
            = Type.createBind("discard")
            .extractAs(StandardValueType.BOOLEAN)
            .build();

    public Inbox(ContextualProvider context, UniObjectNode initialData) {
        super(context, initialData);
    }
}
