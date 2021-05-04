package org.comroid.poste.entity;

import org.comroid.api.ContextualProvider;
import org.comroid.common.io.FileHandle;
import org.comroid.mutatio.model.Ref;
import org.comroid.poste.PosteIO;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.comroid.varbind.container.DataContainerBase;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public abstract class PosteEntity extends DataContainerBase<PosteEntity> {
    public static final GroupBind<PosteEntity> Type
            = new GroupBind<>(PosteIO.CONTEXT, "poste-entity");
    public static final VarBind<PosteEntity, String, FileHandle, FileHandle> HOME
            = Type.createBind("home")
            .extractAs(StandardValueType.STRING)
            .andRemap(FileHandle::new)
            .build();
    public static final VarBind<PosteEntity, String, Instant, Instant> CREATED
            = Type.createBind("created")
            .extractAs(StandardValueType.STRING)
            .andRemap(Instant::parse)
            .build();
    public static final VarBind<PosteEntity, String, Instant, Instant> UPDATED
            = Type.createBind("updated")
            .extractAs(StandardValueType.STRING)
            .andRemap(Instant::parse)
            .build();
    public static final VarBind<PosteEntity, String, String, String> REFERENCE_ID
            = Type.createBind("reference_id")
            .extractAs(StandardValueType.STRING)
            .build();
    public final Ref<FileHandle> home = getComputedReference(HOME);
    public final Ref<Instant> created = getComputedReference(CREATED);
    public final Ref<Instant> updated = getComputedReference(UPDATED);
    public final Ref<String> referenceId = getComputedReference(REFERENCE_ID);

    public Instant getCreatedTimestamp() {
        return created.assertion("Creation Timestamp");
    }

    public @Nullable Instant getLastUpdatedTimestamp() {
        return updated.get();
    }

    public PosteEntity(ContextualProvider context, @Nullable UniObjectNode initialData) {
        super(context, initialData);
    }
}
