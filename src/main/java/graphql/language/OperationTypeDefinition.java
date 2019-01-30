package graphql.language;


import graphql.Internal;
import graphql.PublicApi;
import graphql.util.TraversalControl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import graphql.util.TraverserContext;

import static graphql.language.NodeChildrenContainer.newNodeChildrenContainer;

@PublicApi
public class OperationTypeDefinition extends AbstractNode<OperationTypeDefinition> implements NamedNode<OperationTypeDefinition> {

    private final String name;
    private final TypeName typeName;

    public static final String CHILD_TYPE_NAME = "typeName";

    @Internal
    protected OperationTypeDefinition(String name, TypeName typeName, SourceLocation sourceLocation, List<Comment> comments, IgnoredChars ignoredChars) {
        super(sourceLocation, comments, ignoredChars);
        this.name = name;
        this.typeName = typeName;
    }

    /**
     * alternative to using a Builder for convenience
     *
     * @param name of the operation
     * @param typeName the type in play
     */
    public OperationTypeDefinition(String name, TypeName typeName) {
        this(name, typeName, null, new ArrayList<>(), IgnoredChars.EMPTY);
    }

    public TypeName getTypeName() {
        return typeName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.add(typeName);
        return result;
    }

    @Override
    public NodeChildrenContainer getNamedChildren() {
        return newNodeChildrenContainer()
                .child(CHILD_TYPE_NAME, typeName)
                .build();
    }

    @Override
    public OperationTypeDefinition withNewChildren(NodeChildrenContainer newChildren) {
        return transform(builder -> builder
                .typeName(newChildren.getChildOrNull(CHILD_TYPE_NAME))
        );
    }

    @Override
    public boolean isEqualTo(Node o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OperationTypeDefinition that = (OperationTypeDefinition) o;

        return NodeUtil.isEqualTo(this.name, that.name);
    }

    @Override
    public OperationTypeDefinition deepCopy() {
        return new OperationTypeDefinition(name, deepCopy(typeName), getSourceLocation(), getComments(), getIgnoredChars());
    }

    @Override
    public String toString() {
        return "OperationTypeDefinition{" +
                "name='" + name + "'" +
                ", typeName=" + typeName +
                "}";
    }

    @Override
    public TraversalControl accept(TraverserContext<Node> context, NodeVisitor visitor) {
        return visitor.visitOperationTypeDefinition(this, context);
    }

    public static Builder newOperationTypeDefinition() {
        return new Builder();
    }

    public OperationTypeDefinition transform(Consumer<Builder> builderConsumer) {
        Builder builder = new Builder(this);
        builderConsumer.accept(builder);
        return builder.build();
    }

    public static final class Builder implements NodeBuilder {
        private SourceLocation sourceLocation;
        private List<Comment> comments = new ArrayList<>();
        private String name;
        private TypeName typeName;
        private IgnoredChars ignoredChars = IgnoredChars.EMPTY;

        private Builder() {
        }


        private Builder(OperationTypeDefinition existing) {
            this.sourceLocation = existing.getSourceLocation();
            this.comments = existing.getComments();
            this.name = existing.getName();
            this.typeName = existing.getTypeName();
            this.ignoredChars = existing.getIgnoredChars();
        }


        public Builder sourceLocation(SourceLocation sourceLocation) {
            this.sourceLocation = sourceLocation;
            return this;
        }

        public Builder comments(List<Comment> comments) {
            this.comments = comments;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder typeName(TypeName type) {
            this.typeName = type;
            return this;
        }

        public Builder ignoredChars(IgnoredChars ignoredChars) {
            this.ignoredChars = ignoredChars;
            return this;
        }

        public OperationTypeDefinition build() {
            OperationTypeDefinition operationTypeDefinition = new OperationTypeDefinition(name, typeName, sourceLocation, comments, ignoredChars);
            return operationTypeDefinition;
        }
    }
}
