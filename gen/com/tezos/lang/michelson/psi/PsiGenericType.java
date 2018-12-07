// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiGenericType extends PsiType {

  @Nullable
  PsiElement getTypeName();

  @Nullable
  PsiElement getTypeNameComparable();

  @NotNull
  PsiElement getTypeToken();

  @NotNull
  List<PsiAnnotation> getAnnotations();

}
