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
public class EnumValueDefinition extends AbstractNode<EnumValueDefinition> implements DirectivesContainer<EnumValueDefinition> {
    private final String name;
    private final Description description;
    private final List<Directive> directives;

    public static final String CHILD_DIRECTIVES = "directives";

    @Internal
    protected EnumValueDefinition(String name,
                                  List<Directive> directives,
                                  Description description,
                                  SourceLocation sourceLocation,
                                  List<Comment> comments,
                                  IgnoredChars ignoredChars) {
        super(sourceLocation, comments, ignoredChars);
        this.name = name;
        this.description = description;
        this.directives = (null == directives) ? new ArrayList<>() : directives;
    }

    /**
     * alternative to using a Builder for convenience
     *
     * @param name of the enum value
     */
    public EnumValueDefinition(String name) {
        this(name, new ArrayList<>(), null, null, new ArrayList<>(), IgnoredChars.EMPTY);
    }

    /**
     * alternative to using a Builder for convenience
     *
     * @param name       of the enum value
     * @param directives the directives on the enum value
     */
    public EnumValueDefinition(String name, List<Directive> directives) {
        this(name, directives, null, null, new ArrayList<>(), IgnoredChars.EMPTY);
    }

    @Override
    public String getName() {
        return name;
    }

    public Description getDescription() {
        return description;
    }

    @Override
    public List<Directive> getDirectives() {
        return new ArrayList<>(directives);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.addAll(directives);
        return result;
    }

    @Override
    public NodeChildrenContainer getNamedChildren() {
        return newNodeChildrenContainer()
                .children(CHILD_DIRECTIVES, directives)
                .build();
    }

    @Override
    public EnumValueDefinition withNewChildren(NodeChildrenContainer newChildren) {
        return transform(builder -> builder
                .directives(newChildren.getChildren(CHILD_DIRECTIVES))
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

        EnumValueDefinition that = (EnumValueDefinition) o;

        return NodeUtil.isEqualTo(this.name, that.name);

    }

    @Override
    public EnumValueDefinition deepCopy() {
        return new EnumValueDefinition(name, deepCopy(directives), description, getSourceLocation(), getComments(), getIgnoredChars());
    }

    @Override
    public String toString() {
        return "EnumValueDefinition{" +
                "name='" + name + '\'' +
                ", directives=" + directives +
                '}';
    }

    @Override
    public TraversalControl accept(TraverserContext<Node> context, NodeVisitor visitor) {
        return visitor.visitEnumValueDefinition(this, context);
    }

    public static Builder newEnumValueDefinition() {
        return new Builder();
    }

    public EnumValueDefinition transform(Consumer<Builder> builderConsumer) {
        Builder builder = new Builder(this);
        builderConsumer.accept(builder);
        return builder.build();
    }

    public static final class Builder implements NodeDirectivesBuilder {
        private SourceLocation sourceLocation;
        private List<Comment> comments = new ArrayList<>();
        private String name;
        private Description description;
        private List<Directive> directives;
        private IgnoredChars ignoredChars = IgnoredChars.EMPTY;

        private Builder() {
        }

        private Builder(EnumValueDefinition existing) {
            this.sourceLocation = existing.getSourceLocation();
            this.comments = existing.getComments();
            this.name = existing.getName();
            this.description = existing.getDescription();
            this.directives = existing.getDirectives();
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

        public Builder description(Description description) {
            this.description = description;
            return this;
        }

        public Builder directives(List<Directive> directives) {
            this.directives = directives;
            return this;
        }

        public Builder ignoredChars(IgnoredChars ignoredChars) {
            this.ignoredChars = ignoredChars;
            return this;
        }

        public EnumValueDefinition build() {
            EnumValueDefinition enumValueDefinition = new EnumValueDefinition(name, directives, description, sourceLocation, comments, ignoredChars);
            return enumValueDefinition;
        }
    }
}
