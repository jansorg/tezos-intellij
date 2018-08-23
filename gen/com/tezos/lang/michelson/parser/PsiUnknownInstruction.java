// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.parser;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiUnknownInstruction extends PsiInstruction {

  @NotNull
  List<PsiBlockInstruction> getBlockInstructionList();

  @NotNull
  List<PsiData> getDataList();

  @NotNull
  List<PsiType> getTypeList();

  @NotNull
  PsiElement getInstructionToken();

}
