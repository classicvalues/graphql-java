package graphql.util;

import graphql.Internal;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

@Internal
public class SimpleTraverserContext<T> implements TraverserContext<T> {

    private final Map<Class<?>, Object> vars = new LinkedHashMap<>();
    private final T node;
    private Object result;

    public SimpleTraverserContext(T node) {
        this.node = node;
    }

    @Override
    public T thisNode() {
        return node;
    }

    @Override
    public TraverserContext<T> getParentContext() {
        return null;
    }

    @Override
    public Object getParentResult() {
        return null;
    }

    @Override
    public boolean isVisited() {
        return false;
    }

    @Override
    public Set<T> visitedNodes() {
        return null;
    }

    @Override
    public <S> S computeVarIfAbsent(Class<? super S> key, BiFunction<? super TraverserContext<T>, ? super Class<S>, ? extends S> provider) {
        return null;
    }

    @Override
    public <S> TraverserContext<T> setVar(Class<? super S> key, S value) {
        return null;
    }

    @Override
    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public Object computeResultIfAbsent(Function<? super TraverserContext<T>, ? extends Object> provider) {
        return this.result;
    }

    @Override
    public Object getInitialData() {
        return null;
    }
}
