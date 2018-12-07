// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiComplexType extends PsiType {

  @Nullable
  PsiAnnotationList getAnnotationList();

  @Nullable
  PsiElement getTypeName();

  @Nullable
  PsiElement getTypeNameComparable();

  @NotNull
  List<PsiType> getTypeArguments();

  @Nullable
  PsiElement getTypeToken();

  boolean hasSimpleTypes();

  boolean hasComplexTypes();

}
