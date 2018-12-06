// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiComplexType extends PsiType, PsiNamedType {

  @NotNull
  List<PsiType> getTypeList();

  @NotNull
  List<PsiAnnotation> getAnnotations();

  @NotNull
  String getTypeNameString();

  @NotNull
  PsiElement getTypeToken();

  boolean hasSimpleTypes();

  boolean hasComplexTypes();

}
