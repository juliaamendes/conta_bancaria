package com.senai.conta_bancaria.interface_ui.exception;

import com.senai.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.exception.ValoresNegativosException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ValoresNegativosException.class)
    public ProblemDetail handleValoresNegativo(ValoresNegativosException ex,
                                               HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Valores negativos não são permitidos.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<String> handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}
