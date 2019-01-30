/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphql.util;

import static graphql.Assert.assertTrue;
import static graphql.Assert.assertNotNull;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a Vertex in a DependencyGraph
 * 
 * @param <N> the actual subtype of the Vertex
 */
public abstract class Vertex<N extends Vertex<N>> {
    public Object getId () {
        return id;
    }
    
    protected N id (Object id) {
        this.id = id;
        return (N)this;
    }
    
    public N addEdge (Edge<? extends N, ?> edge) {
        assertNotNull(edge);
        assertTrue(edge.getSink() == this, "Edge MUST sink to this vertex");
        
        edge.connectEndpoints();
        return (N)this;
    }
    
    public <C extends DependencyGraphContext> N dependsOn (N source, BiConsumer<? super C, ? super Edge<N, ?>> edgeAction) {
        assertNotNull(source);
        assertNotNull(edgeAction);
        
        return addEdge(new Edge<>(source, (N)this, edgeAction));
    }

    private static void emptyEdgeConsumer (Edge<?, ?> edge) {
    }
    
    public N undependsOn (N source) {
        return undependsOn(source, Vertex::emptyEdgeConsumer);
    }
    
    public N undependsOn (N source, Consumer<? super Edge<N, ?>> whenDisconnecting) {
        Objects.requireNonNull(source);
        
        new ArrayList<>(outdegrees)
            .stream()
            .filter(edge -> edge.getSource() == source)
            .peek(whenDisconnecting)
            .forEach(Edge::disconnectEndpoints);
        
        return (N)this;
    }
    
    public N disconnect () {
        Stream.concat(
            new ArrayList<>(indegrees).stream(), 
            new ArrayList<>(outdegrees).stream()
        )
        .forEach(Edge::disconnectEndpoints);
        
        return (N)this;
    }
    
    private static boolean alwaysTrue (Object v) {
        return true;
    }
    
    public List<N> adjacencySet () {
        return adjacencySet(Vertex::alwaysTrue);
    }

    public List<N> adjacencySet (Predicate<? super N> filter) {
        assertNotNull(filter);
        
        return indegrees
            .stream()
            .map(Edge::getSink)
            .filter(filter)
            .collect(Collectors.toList());
    }

    public List<N> dependencySet () {
        return dependencySet(Vertex::alwaysTrue);
    }
    
    public List<N> dependencySet (Predicate<? super N> filter) {
        assertNotNull(filter);
        
        return outdegrees
            .stream()
            .map(Edge::getSource)
            .filter(filter)
            .collect(Collectors.toList());
    }
    
    public boolean resolve (DependencyGraphContext context) {
        return false;
    }
    
    protected void fireResolved (DependencyGraphContext context) {
        indegrees.forEach(edge -> edge.fire(context));
    }
    
    @Override
    public String toString() {
        return toString(new StringBuilder(getClass().getSimpleName()).append('{'))
                .append('}')
                .toString();
    }
    
    protected StringBuilder toString (StringBuilder builder) {
        return builder
            .append("id=").append(id)
            .append(", dependencies=").append(
                outdegrees
                    .stream()
                    .map(Edge::getSource)
                    .map(Vertex::toString)
                    .collect(Collectors.joining(", ", "on ->", " "))
            );
    }
    
    protected Object id;
    protected final Set<Edge<N, ?>> outdegrees = new LinkedHashSet<>();
    protected final Set<Edge<N, ?>> indegrees = new LinkedHashSet<>();
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Vertex.class);
}
