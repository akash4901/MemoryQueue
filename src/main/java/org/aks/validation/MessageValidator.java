package org.aks.validation;

public interface MessageValidator<T> {
    boolean validate(T t);
    String getDetails();
}
