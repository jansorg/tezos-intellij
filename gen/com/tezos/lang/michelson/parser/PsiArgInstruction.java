// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.parser;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiArgInstruction extends PsiInstruction {

  @Nullable
  PsiBlockInstruction getBlockInstruction();

  @Nullable
  PsiData getData();

  @Nullable
  PsiType getType();

  @Nullable
  PsiElement getInt();

  @Nullable
  PsiElement getString();

}
