// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.parser;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiGenericInstruction extends PsiInstruction {

  @NotNull
  List<PsiAnnotation> getAnnotationList();

  @NotNull
  List<PsiBlockInstruction> getBlockInstructionList();

  @NotNull
  List<PsiData> getDataList();

  @NotNull
  List<PsiType> getTypeList();

  @NotNull
  PsiElement getInstructionToken();

}
