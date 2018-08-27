// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiConditional extends PsiInstruction {

  @NotNull
  PsiBlockInstruction getTrueBranch();

  @Nullable
  PsiBlockInstruction getFalseBranch();

  @NotNull
  List<PsiBlockInstruction> getInstructionBlocks();

}
