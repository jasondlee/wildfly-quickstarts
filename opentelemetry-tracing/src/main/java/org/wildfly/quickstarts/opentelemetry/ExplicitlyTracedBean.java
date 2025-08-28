package org.wildfly.quickstarts.opentelemetry;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;

@RequestScoped
public class ExplicitlyTracedBean {

    @Inject
    private Tracer tracer;

    public String getHello() {
        Span prepareHelloSpan = tracer.spanBuilder("prepare-hello").startSpan();
        prepareHelloSpan.makeCurrent();

        Span processHelloSpan = tracer.spanBuilder("process-hello").startSpan();
        processHelloSpan.makeCurrent();

        long millis = (long) (Math.random() * 10_000);

        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        processHelloSpan.end();
        prepareHelloSpan.end();

        return ("hello after " + millis + " ms\n").toUpperCase();
    }
}
