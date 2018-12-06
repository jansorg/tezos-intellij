// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.tezos.lang.michelson.lang.type.TypeMetadata;

public interface PsiType extends PsiAnnotated {

  boolean isComparable();

  @Nullable
  PsiNamedType findComposedParentType();

  @NotNull
  List<PsiNamedType> findChildrenTypes();

  boolean hasComposedParentType();

  @Nullable
  TypeMetadata getTypeMetadata();

}
