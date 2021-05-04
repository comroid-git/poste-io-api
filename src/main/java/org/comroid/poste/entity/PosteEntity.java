package org.comroid.poste.entity;

import org.comroid.api.ContextualProvider;
import org.comroid.mutatio.model.Ref;
import org.comroid.poste.PosteIO;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.comroid.varbind.container.DataContainerBase;
import org.jetbrains.annotations.Nullable;

public abstract class PosteEntity extends DataContainerBase<PosteEntity> {
    public static final GroupBind<PosteEntity> Type
            = new GroupBind<>(PosteIO.CONTEXT, "poste-entity");
    public static final VarBind<PosteEntity, String, String, String> REFERENCE_ID
            = Type.createBind("reference_id")
            .extractAs(StandardValueType.STRING)
            .build();
    public final Ref<String> referenceId = getComputedReference(REFERENCE_ID);

    public PosteEntity(ContextualProvider context, @Nullable UniObjectNode initialData) {
        super(context, initialData);
    }
}
