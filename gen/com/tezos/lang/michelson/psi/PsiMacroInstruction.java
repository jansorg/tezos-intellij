// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.tezos.lang.michelson.lang.macro.MacroMetadata;

public interface PsiMacroInstruction extends PsiInstruction {

  @Nullable
  PsiAnnotationList getAnnotationList();

  @NotNull
  PsiElement getMacroToken();

  @NotNull
  List<PsiBlockInstruction> getInstructionBlocks();

  @Nullable
  MacroMetadata getMacroMetadata();

}
