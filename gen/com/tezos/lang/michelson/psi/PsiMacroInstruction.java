// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiMacroInstruction extends PsiInstruction {

  @NotNull
  List<PsiBlockInstruction> getBlockInstructionList();

  @NotNull
  PsiElement getMacroToken();

  @NotNull
  List<PsiAnnotation> getAnnotations();

}
