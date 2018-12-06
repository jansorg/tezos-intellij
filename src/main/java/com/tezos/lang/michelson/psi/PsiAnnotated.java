package com.tezos.lang.michelson.psi;

import com.tezos.lang.michelson.lang.AnnotationType;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * PSI element which have attached annotations implement this interface.
 *
 * @author jansorg
 */
interface PsiAnnotated extends MichelsonComposite {
    /**
     * @return The annotations of the current element, default to an empty list.
     */
    default List<PsiAnnotation> getAnnotations() {
        return Collections.emptyList();
    }

    default List<PsiAnnotation> findAnnotations(AnnotationType type) {
        List<PsiAnnotation> result = new LinkedList<>();

        acceptChildren(new PsiVisitor<Void>() {
            @Override
            public Void visitAnnotation(@NotNull PsiAnnotation o) {
                if (o.getAnnotationType() == type) {
                    result.add(o);
                }
                return null;
            }
        });

        return result;
    }

    default List<PsiAnnotation> getTypeAnnotations() {
        return findAnnotations(AnnotationType.TYPE);
    }

    default List<PsiAnnotation> getVariableAnnotations() {
        return findAnnotations(AnnotationType.VARIABLE);
    }

    default List<PsiAnnotation> getFieldAnnotations() {
        return findAnnotations(AnnotationType.FIELD);
    }
}