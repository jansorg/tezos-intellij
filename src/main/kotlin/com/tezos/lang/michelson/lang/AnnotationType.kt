package com.tezos.lang.michelson.lang

/**
 * The possible types of an annotation.
 * @author jansorg
 */
enum class AnnotationType {
    /**
     * Each type can be annotated with at most one type annotation.
     * They are used to give names to types. For types to be equal,
     * their unnamed version must be equal and their names must be the same or at least one type must be unnamed.
     */
    TYPE,
    /**
     * Variable annotations can only be used on instructions that produce elements on the stack.
     * An instruction that produces n elements on the stack can be given at most n variable annotations.
     */
    VARIABLE,
    /**
     * Components of pair types, option types and or types can be annotated with a field or constructor annotation.
     * This feature is useful to encode records fields and constructors of sum types.
     */
    FIELD
}