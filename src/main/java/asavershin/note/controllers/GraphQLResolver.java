package asavershin.note.controllers;

import asavershin.note.exceptions.EntityNotFoundException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class GraphQLResolver extends DataFetcherExceptionResolverAdapter {
    @Override
    @GraphQlExceptionHandler
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {

        if (ex instanceof EntityNotFoundException e) {

            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.NOT_FOUND)
                    .message(e.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        }

        if (ex instanceof MethodArgumentNotValidException e) {
            Map<String, Object> body = new HashMap<>();
            body.put("errors",
                    e.getBindingResult().getAllErrors().stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .filter(Objects::nonNull)
                            .toList());

            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.BAD_REQUEST)
                    .message("Validation failed")
                    .location(env.getField().getSourceLocation())
                    .extensions(body)
                    .build();
        }

        return null;
    }
}
