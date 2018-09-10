// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiType extends PsiAnnotated {

  @Nullable
  PsiType getType();

  @NotNull
  String getTypeNameString();

  boolean isComparable();

  @Nullable
  PsiType findComposedParentType();

  @NotNull
  List<PsiType> findChildrenTypes();

  boolean hasComposedParentType();

}
