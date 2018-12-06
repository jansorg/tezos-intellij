// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.psi;

import org.jetbrains.annotations.*;
import com.tezos.lang.michelson.lang.AnnotationType;

public interface PsiAnnotation extends MichelsonComposite {

  boolean isTypeAnnotation();

  boolean isVariableAnnotation();

  boolean isFieldAnnotation();

  @NotNull
  AnnotationType getAnnotationType();

  @Nullable
  PsiInstruction findParentInstruction();

  @Nullable
  PsiType findParentType();

  @Nullable
  PsiData findParentData();

}
