// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.parser;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiConditionalInstruction extends PsiInstruction {

  @NotNull
  List<PsiBlockInstruction> getBlockInstructionList();

  @NotNull
  PsiBlockInstruction getTrueBranch();

  @Nullable
  PsiBlockInstruction getFalseBranch();

}
