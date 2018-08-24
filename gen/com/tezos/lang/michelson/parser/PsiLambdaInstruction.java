// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.parser;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiLambdaInstruction extends PsiInstruction {

  @NotNull
  List<PsiAnnotation> getAnnotationList();

  @NotNull
  PsiBlockInstruction getBlockInstruction();

  @NotNull
  List<PsiType> getTypeList();

}
