// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.parser;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.tezos.lang.michelson.psi.MichelsonComposite;

public class PsiVisitor<R> extends PsiElementVisitor {

  public R visitArgInstruction(@NotNull PsiArgInstruction o) {
    return visitInstruction(o);
  }

  public R visitAssertMacro(@NotNull PsiAssertMacro o) {
    return visitMacroInstruction(o);
  }

  public R visitBlockInstruction(@NotNull PsiBlockInstruction o) {
    return visitInstruction(o);
  }

  public R visitCmpMacro(@NotNull PsiCmpMacro o) {
    return visitMacroInstruction(o);
  }

  public R visitCodeSection(@NotNull PsiCodeSection o) {
    return visitSection(o);
  }

  public R visitComparableType(@NotNull PsiComparableType o) {
    return visitType(o);
  }

  public R visitConditionalMacro(@NotNull PsiConditionalMacro o) {
    return visitMacroInstruction(o);
  }

  public R visitData(@NotNull PsiData o) {
    return visitMichelsonComposite(o);
  }

  public R visitFailMacro(@NotNull PsiFailMacro o) {
    return visitMacroInstruction(o);
  }

  public R visitIfInstruction(@NotNull PsiIfInstruction o) {
    return visitInstruction(o);
  }

  public R visitInstruction(@NotNull PsiInstruction o) {
    return visitMichelsonComposite(o);
  }

  public R visitMacroInstruction(@NotNull PsiMacroInstruction o) {
    return visitInstruction(o);
  }

  public R visitMapAccessMacro(@NotNull PsiMapAccessMacro o) {
    return visitMapMacro(o);
  }

  public R visitMapMacro(@NotNull PsiMapMacro o) {
    return visitMacroInstruction(o);
  }

  public R visitPairAccessMacro(@NotNull PsiPairAccessMacro o) {
    return visitMacroInstruction(o);
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

  public R visitSetAccessMacro(@NotNull PsiSetAccessMacro o) {
    return visitSetMacro(o);
  }

  public R visitSetMacro(@NotNull PsiSetMacro o) {
    return visitMacroInstruction(o);
  }

  public R visitSimpleInstruction(@NotNull PsiSimpleInstruction o) {
    return visitInstruction(o);
  }

  public R visitStorageSection(@NotNull PsiStorageSection o) {
    return visitSection(o);
  }

  public R visitType(@NotNull PsiType o) {
    return visitMichelsonComposite(o);
  }

  public R visitUnknownInstruction(@NotNull PsiUnknownInstruction o) {
    return visitInstruction(o);
  }

  public R visitMichelsonComposite(@NotNull MichelsonComposite o) {
    visitElement(o);
    return null;
  }

}
