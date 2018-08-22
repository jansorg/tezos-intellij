// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.parser;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.tezos.lang.michelson.psi.MichelsonComposite;

public class PsiVisitor<R> extends PsiElementVisitor {

  public R visitCodeSection(@NotNull PsiCodeSection o) {
    return visitSection(o);
  }

  public R visitComparableType(@NotNull PsiComparableType o) {
    return visitType(o);
  }

  public R visitData(@NotNull PsiData o) {
    return visitMichelsonComposite(o);
  }

  public R visitInstruction(@NotNull PsiInstruction o) {
    return visitMichelsonComposite(o);
  }

  public R visitInstructions(@NotNull PsiInstructions o) {
    return visitMichelsonComposite(o);
  }

  public R visitParameterSection(@NotNull PsiParameterSection o) {
    return visitSection(o);
  }

  public R visitReturnSection(@NotNull PsiReturnSection o) {
    return visitSection(o);
  }

  public R visitSection(@NotNull PsiSection o) {
    return visitMichelsonComposite(o);
  }

  public R visitStorageSection(@NotNull PsiStorageSection o) {
    return visitSection(o);
  }

  public R visitType(@NotNull PsiType o) {
    return visitMichelsonComposite(o);
  }

  public R visitMichelsonComposite(@NotNull MichelsonComposite o) {
    visitElement(o);
    return null;
  }

}
