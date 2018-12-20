// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.tezos.lang.michelson.lang.instruction.InstructionMetadata;

public interface PsiGenericInstruction extends PsiInstruction {

  @Nullable
  PsiAnnotationList getAnnotationList();

  @NotNull
  List<PsiData> getDataList();

  @NotNull
  List<PsiEmptyBlock> getEmptyBlockList();

  @Nullable
  PsiTrailingAnnotationList getTrailingAnnotationList();

  @NotNull
  List<PsiType> getTypeList();

  @NotNull
  PsiElement getInstructionToken();

  @NotNull
  List<PsiBlockInstruction> getInstructionBlocks();

  @Nullable
  InstructionMetadata getInstructionMetadata();

}
