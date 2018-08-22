// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.parser;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class Visitor<R> extends PsiElementVisitor {

  public R visitCodeSection(@NotNull CodeSection o) {
    return visitPsiElement(o);
  }

  public R visitComparableType(@NotNull ComparableType o) {
    return visitPsiElement(o);
  }

  public R visitData(@NotNull Data o) {
    return visitPsiElement(o);
  }

  public R visitDataToplevel(@NotNull DataToplevel o) {
    return visitPsiElement(o);
  }

  public R visitInstruction(@NotNull Instruction o) {
    return visitPsiElement(o);
  }

  public R visitInstructions(@NotNull Instructions o) {
    return visitPsiElement(o);
  }

  public R visitParameterSection(@NotNull ParameterSection o) {
    return visitPsiElement(o);
  }

  public R visitReturnSection(@NotNull ReturnSection o) {
    return visitPsiElement(o);
  }

  public R visitSection(@NotNull Section o) {
    return visitPsiElement(o);
  }

  public R visitStorageSection(@NotNull StorageSection o) {
    return visitPsiElement(o);
  }

  public R visitType(@NotNull Type o) {
    return visitPsiElement(o);
  }

  public R visitTypeToplevel(@NotNull TypeToplevel o) {
    return visitPsiElement(o);
  }

  public R visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
    return null;
  }

}
